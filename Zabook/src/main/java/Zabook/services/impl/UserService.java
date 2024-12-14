package Zabook.services.impl;


import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Zabook.configs.CustomUserDetails;
import Zabook.dto.UserRequest;
import Zabook.models.User;
import Zabook.repository.UserRepository;
import Zabook.services.IUserService;
import Zabook.services.JwtService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Service
public class UserService implements IUserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    private String generateRandomString(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Override
    public boolean checkEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public User createUser(User user, String url) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        user.setEnabled(false);
        user.setAccounNonLocked(true);

        String verificationCode = generateRandomString(64);
        user.setVerificationCode(verificationCode);

        User us = userRepo.save(user);

        sendVerificationMail(user, url);

        return us;
    }

    public void sendVerificationMail(User user, String url) {
        String from = "nguyenthanhan26.qngai@gmail.com";
        String to = user.getEmail();
        String subject = "Account Verification";
        String content = "Dear, [[name]],<br>" + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "ThanhAn";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

            mimeMessageHelper.setFrom(from, "4P");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);

            String name = user.getFirstName() + " " + user.getLastName();

            content = content.replace("[[name]]", name);

            String siteUrl = url + "/verify?code=" + user.getVerificationCode();

            content = content.replace("[[URL]]", siteUrl);

            mimeMessageHelper.setText(content, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user;
    }

    

    @Override
    public boolean verifyAccount(String code) {
        User user = userRepo.findByVerificationCode(code);

        if (user != null) {
            user.setEnabled(true);
            user.setVerificationCode(code);
            userRepo.save(user);
            return true;
        }
        return false;
    }
    //lam

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("Người dùng chưa đăng nhập");
		}
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // Lấy User từ UserService
		User user = this.getUserByEmail(userDetails.getUsername ());
		return user;
	}
    
@Override
    public void updateUser(User user) {
        userRepo.save(user);
    }

    // lâm
	@Override
	public User getUserById(String id) {
		ObjectId id1 = new ObjectId(id);
		return userRepo.findById(id1).orElse(null);
	}

	@Override
	public String login(UserRequest request) {
		try {
			Authentication authenticate = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			if (authenticate.isAuthenticated()) {
				System.out.println("Authentication successful for email: " + request.getEmail());
				return jwtService.generateToken(request.getEmail());
			}
		} catch (BadCredentialsException e) {
			System.out.println("Bad credentials for email: " + request.getEmail());
			throw new RuntimeException("Đăng nhập thất bại: Sai thông tin tài khoản hoặc mật khẩu");
		} catch (UsernameNotFoundException e) {
			System.out.println("User not found: " + request.getEmail());
			throw new RuntimeException("Đăng nhập thất bại: Người dùng không tồn tại");
		} catch (Exception e) {
			System.out.println("Unexpected error during login: " + e.getMessage());
			throw new RuntimeException("Đăng nhập thất bại: " + e.getMessage());
		}

		throw new RuntimeException("Xác thực thất bại");
	}

	
	@Override
	public boolean sendOTP(String email, HttpSession session) {
		 User user = userRepo.findByEmail(email);
        if (user == null) {
            return false; 
        }

  
        int otp = new Random().nextInt(900000) + 100000;
        long otpTimestamp = System.currentTimeMillis();
        session.setAttribute("otp", otp);
        session.setAttribute("otpTimestamp", otpTimestamp);
        session.setAttribute("email", email);

        try {
 
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Reset Password OTP");
            helper.setText("<p>Your OTP code is: <strong>" + otp + "</strong></p>", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;  
        }

        return true; 
    }

	public boolean isOtpExpired(HttpSession session) {
        long otpTimestamp = (long) session.getAttribute("otpTimestamp");
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - otpTimestamp;
        return elapsedTime >= 5 * 60 * 1000; // 5 phút
    }

    public boolean isOtpValid(String otp, int generatedOtp) {
        return otp.equals(String.valueOf(generatedOtp));
    }
	
	@Override
	public String verifyOTP(String otp1, String otp2, String otp3, String otp4, String otp5, String otp6,
			HttpSession session, RedirectAttributes redirectAttributes) {
				String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
				int generatedOtp = (int) session.getAttribute("otp");
		
				if (isOtpExpired(session)) {
					redirectAttributes.addFlashAttribute("msg", "OTP đã hết hạn. Vui lòng yêu cầu mã OTP mới.");
					return "redirect:/verifyOTP";
				}
		
				if (isOtpValid(otp, generatedOtp)) {
					return "redirect:/resetPassword";
				} else {
					redirectAttributes.addFlashAttribute("msg", "Mã OTP không đúng!");
					return "redirect:/verifyOTP";
				}
	}
	@Override
	public boolean resetPassword(String email, String newPassword) {
		User user = userRepo.findByEmail(email);

        if (user != null) {
          
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return true;
        } else {
            return false;
        }
	}

	@Override
	public ObjectId getCurrentBuyerId(Principal principal) {
    	String username = principal.getName();
	    
	    // Tìm User theo email hoặc username để lấy ID
	    User user = getUserByEmail(username);
	    
	    return user.getUserID();
 	}

	@Override
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}
    

}