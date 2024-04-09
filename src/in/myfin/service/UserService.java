package in.myfin.service;


import in.myfin.exception.CustomException;
import in.myfin.model.User;

import java.util.List;

public interface UserService {
    void registerUser(User user) throws CustomException;
    User loginUser(String username, String password) throws CustomException;
    void logoutUser(User user) throws CustomException;
    void blockUser(User user) throws CustomException;
    List<User> getAllUsers() throws CustomException;
    void updateUserAttempts(User user) throws CustomException;
    User getUserById(int userId) throws CustomException;

    void deleteUser(int userID)  throws CustomException;


    List<User> getAllCustomers() throws CustomException;
}
