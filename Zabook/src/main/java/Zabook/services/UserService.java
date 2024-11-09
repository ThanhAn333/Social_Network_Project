package Zabook.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.User;
import Zabook.repository.UserRepository;

@Service
public class UserService implements IUserService {

	@Autowired
    private UserRepository userRepository;

    @Override
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
	public User getUserById(int userID) {
        return userRepository.findById(userID).orElse(null);
    }

    @Override
	public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
	public User updateUser(int userID, User user) {
        if (userRepository.existsById(userID)) {
            user.setUserID(userID);
            return userRepository.save(user);
        }
        return null;
    }

    @Override
	public void deleteUser(int userID) {
        userRepository.deleteById(userID);
    }

}
