package Zabook.services.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Zabook.models.User;
import Zabook.repository.UserRepository;
import Zabook.services.IUserService;

@Service
public class UserService implements IUserService {

	@Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

	@Override
	public void deleteUser(ObjectId userID) {
		 userRepository.deleteById(userID);
		
	}

	@Override
	public void updateUser(User user) {
		
    	userRepository.save(user);
        
	}

	@Override
	public User createUser(User user) {
		 return userRepository.save(user);
	}

	@Override
	public User getUserById(ObjectId userID) {
		 return userRepository.findById(userID).orElse(null);
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

    

}
