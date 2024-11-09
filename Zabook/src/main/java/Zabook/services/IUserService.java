package Zabook.services;

import java.util.List;

import Zabook.models.User;

public interface IUserService {


    void deleteUser(int userID);

    User updateUser(int userID, User user);

    User createUser(User user);

    User getUserById(int userID);

    List<User> getAllUsers();

}
