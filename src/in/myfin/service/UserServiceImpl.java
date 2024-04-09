package in.myfin.service;


import in.myfin.dao.UserDAO;
import in.myfin.dao.UserDAOImpl;
import in.myfin.exception.CustomException;
import in.myfin.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {



    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public void registerUser(User user) throws CustomException {
        System.out.println("here");
        userDAO.createUser(user);
    }

    @Override
    public User loginUser(String username, String password) throws CustomException {
        User user = userDAO.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setStatus(true);
            user.setAttempts(0);
            userDAO.updateUser(user);
            return user;
        } else {
            updateUserAttempts(userDAO.getUserByUsername(username));
            System.out.println(userDAO.getUserByUsername(username));
            throw new CustomException("Invalid username or password");
        }
    }

    @Override
    public void logoutUser(User user) throws CustomException {
        user.setStatus(false);
        userDAO.updateUser(user);
    }

    @Override
    public void blockUser(User user) throws CustomException {
        user.setStatus(false);
        userDAO.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() throws CustomException {
        return userDAO.getAllUsers();
    }

    @Override
    public void updateUserAttempts(User user) throws CustomException {

        user.setAttempts(user.getAttempts() + 1);
        System.out.println(user);
        if (user.getAttempts() >= 3) {
            blockUser(user);
            throw new CustomException("User blocked due to multiple unsuccessful attempts");
        } else {
            System.out.println("im here"+user);
            userDAO.updateUser(user);
        }
    }


    @Override
    public User getUserById(int userId) throws CustomException {
        try {
            return userDAO.getUserById(userId);
        } catch (CustomException e) {
            throw new CustomException("Error fetching user by ID", e);
        }
    }


    public void deleteUser(int userId) throws CustomException{
        try {
            userDAO.deleteUser(userId);
        } catch (CustomException e) {
            throw new CustomException("Error fetching user by ID", e);
        }
    }
   public List<User> getAllCustomers() throws CustomException{
       List<User> users;
       try {
       users= userDAO.getAllUsers();
       } catch (CustomException e) {
           throw new CustomException("Error fetching user by ID", e);
       }
       return users;
   }

}
