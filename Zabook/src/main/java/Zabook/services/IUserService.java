package Zabook.services;

import java.util.List;

import org.bson.types.ObjectId;

import Zabook.models.User;

public interface IUserService {


    void deleteUser(ObjectId userID);

    void updateUser(User user);

    User createUser(User user);

    User getUserById(ObjectId userID);

    List<User> getAllUsers();
    
    User getUserByEmail(String email);

    
}
