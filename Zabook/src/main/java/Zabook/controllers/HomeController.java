package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Zabook.dto.AuthResponse;
import Zabook.dto.UserRequest;
import Zabook.models.User;
import Zabook.repository.UserRepository;
import Zabook.services.IUserService;
import Zabook.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController {

	@Autowired
	IUserService userService;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService detailsService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/")
	public String redirectToLogin() {
		return "redirect:/login";
	}
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}

	@GetMapping("/notifyVerify")
	public String getVerify() {
		return "notifyVerify";
	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam("code") String code) {
		if (userService.verifyAccount(code)) {
			return "verifySuccessfull";
		} else {
			return "fail";
		}
	}

	@GetMapping("/forgotPassword")
	public String getForgorPassword(Model model, HttpSession session) {
		String msg = (String) session.getAttribute("msg");
		if (msg != null) {
			model.addAttribute("msg", msg);
			session.removeAttribute("msg");
		}
		return "forgotPassword";
	}

	@GetMapping("/resetPassword")
	public String getMethodName() {
		return "resetPassword";
	}
	


	@GetMapping("/verifyOTP")
	public String verifyOTP(Model model, HttpSession session) {
		String msg = (String) session.getAttribute("msg");
		if (msg != null) {
			model.addAttribute("msg", msg);
			session.removeAttribute("msg");
		}
	
		Long otpTimestamp = (Long) session.getAttribute("otpTimestamp");
		long remainingTime = 0;
		if (otpTimestamp != null) {
			long currentTime = System.currentTimeMillis();
			remainingTime = (5 * 60 * 1000) - (currentTime - otpTimestamp);
			if (remainingTime < 0) {
				remainingTime = 0;
			}
		}
		model.addAttribute("remainingTime", remainingTime);
		return "verifyOTP";
	}
	
	
	

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserRequest authRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		UserDetails userDetails = detailsService.loadUserByUsername(authRequest.getEmail());

		final String jwt = jwtService.generateToken(userDetails.getUsername());

		return ResponseEntity.ok(new AuthResponse(jwt));
	}

	@PostMapping("/createUser")
	public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes,
			@RequestParam("password") String password, @RequestParam("confirmPassword") String password1,
			HttpServletRequest request) {

		System.out.println("User :"+user);
		String url = request.getRequestURL().toString();
		url = url.replace(request.getServletPath(), "");

		boolean emailExists = userService.checkEmail(user.getEmail());
		if (!emailExists) {
			if (password.equals(password1)) {
				User createdUser = userService.createUser(user, url);
				if (createdUser != null) {
					redirectAttributes.addFlashAttribute("msg", "Register successful!");
					return "redirect:/notifyVerify";
				} else {
					redirectAttributes.addFlashAttribute("msg", "Something went wrong! Please try again.");
				}
			} else {
				redirectAttributes.addFlashAttribute("msg", "Mật khẩu không khớp");
			}
		} else {
			redirectAttributes.addFlashAttribute("msg", "Email already exists!");
		}
		return "redirect:/login";
	}

	
	@PostMapping("/forgotPassword")
	public String postForgotPassword(@RequestParam("email") String email,HttpSession session, RedirectAttributes redirectAttributes) {
		
        boolean otpSent = userService.sendOTP(email, session);
        if (!otpSent) {
            redirectAttributes.addFlashAttribute("msg", "Email không tồn tại trong hệ thống. Vui lòng kiểm tra lại!");
            return "redirect:/forgotPassword";
        }
        return "verifyOTP";
	}
	


	@PostMapping("/verifyOTP")
	public String verifyOtp(@RequestParam("otp1") String otp1, @RequestParam("otp2") String otp2,
                            @RequestParam("otp3") String otp3, @RequestParam("otp4") String otp4,
                            @RequestParam("otp5") String otp5, @RequestParam("otp6") String otp6,
                            HttpSession session, RedirectAttributes redirectAttributes) {

        return userService.verifyOTP(otp1, otp2, otp3, otp4, otp5, otp6, session, redirectAttributes);
    }
	

	@PostMapping("/resetPassword")
	public String postResetPassword(@RequestParam("newPassword") String password, @RequestParam("confirmPassword") String password1, HttpSession session, RedirectAttributes redirectAttributes) {
		 String email = (String) session.getAttribute("email");

		 if (!password.equals(password1)) {
			redirectAttributes.addFlashAttribute("msg", "Mật khẩu và mật khẩu xác nhận không khớp!");
			return "redirect:/resetPassword";
		}

		boolean isPasswordUpdated = userService.resetPassword(email, password);
	
		if (isPasswordUpdated) {
			session.setAttribute("msg", "Mật khẩu đã được thay đổi thành công!");
			return "redirect:/login"; 
		} else {
			session.setAttribute("msg", "Đã xảy ra lỗi. Vui lòng thử lại sau!");
			return "redirect:/resetPassword";
		}
	
	}
		
	@PostMapping("/resendOTP")
	public ResponseEntity<String> resendOtp(HttpSession session) {
		String email = (String) session.getAttribute("email");

		if (email == null || email.isEmpty()) {
			return ResponseEntity.badRequest().body("Email không hợp lệ.");
		}

		boolean isSent = userService.sendOTP(email, session);
		if (isSent) {
			return ResponseEntity.ok("OTP mới đã được gửi tới email của bạn.");
		} else {
			return ResponseEntity.status(500).body("Không thể gửi OTP. Vui lòng thử lại sau.");
		}
	}

	
	

}