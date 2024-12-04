package Zabook.services;


import Zabook.dto.UserRequest;
import Zabook.models.User;

public interface IUserService {
    public User createUser(User user, String url);
	
	public boolean checkEmail(String email);
	
	User getUserById(String id);
	
	User getUserByEmail(String email);

	boolean checkPassword(String rawPassword, String encodedPassword);
	
	public boolean verifyAccount(String code);

	public String login(UserRequest request);

}
