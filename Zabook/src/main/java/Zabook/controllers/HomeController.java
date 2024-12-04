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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import Zabook.dto.AuthResponse;
import Zabook.dto.UserRequest;
import Zabook.models.User;
import Zabook.repository.UserRepository;
import Zabook.services.IUserService;
import Zabook.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



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

	

}
