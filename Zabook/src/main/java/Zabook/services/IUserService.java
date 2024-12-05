package Zabook.services;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Zabook.dto.UserRequest;
import Zabook.models.User;
import jakarta.servlet.http.HttpSession;

public interface IUserService {
    public User createUser(User user, String url);
	
	public boolean checkEmail(String email);
	
	User getUserById(String id);
	
	User getUserByEmail(String email);

	boolean checkPassword(String rawPassword, String encodedPassword);
	
	public boolean verifyAccount(String code);

	public String login(UserRequest request);

	public boolean sendOTP(String email, HttpSession session);

	public String verifyOTP(String otp1, String otp2, String otp3, String otp4, String otp5, String otp6,
                            HttpSession session, RedirectAttributes redirectAttributes);

	public boolean resetPassword(String email, String password);
}
