package in.myfin.dao;
import in.myfin.exception.CustomException;
import in.myfin.exception.InvalidUserNameException;
import in.myfin.model.User;

import java.util.List;

public interface UserDAO {
    void createUser(User user) throws CustomException;
    void updateUser(User user) throws CustomException;
    void deleteUser(int userId) throws CustomException;
    User getUserByUsername(String username) throws CustomException;
    List<User> getAllUsers() throws CustomException;

    User getUserById(int userId) throws CustomException;
}
