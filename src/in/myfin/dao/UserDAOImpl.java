package in.myfin.dao;


import in.myfin.exception.CustomException;
import in.myfin.model.User;
import in.myfin.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDAOImpl implements UserDAO {
    @Override
    public void createUser(User user) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, userType) VALUES (?, ?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getUserType());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomException("Error creating user: " + ex.getMessage());
        }
    }
    @Override
    public void updateUser(User user) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET password = ?, userType = ?, attempts=?  WHERE userId = ?")) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getUserType());
            statement.setInt(3, user.getAttempts());
            statement.setInt(4, user.getUserId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error updating user", e);
        }
    }

    @Override
    public void deleteUser(int userId) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE userId = ?")) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error deleting user", e);
        }
    }


    @Override
    public User getUserByUsername(String username) throws CustomException {
        User user = null;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("userId"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setUserType(resultSet.getString("userType"));
                    user.setStatus(resultSet.getBoolean("status"));
                    user.setAttempts(resultSet.getInt("attempts"));
                }
            }
        } catch (SQLException ex) {
            throw new CustomException("Error retrieving user by username: " + ex.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() throws CustomException {
        List<User> userList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserType(resultSet.getString("userType"));
                user.setStatus(resultSet.getBoolean("status"));
                user.setAttempts(resultSet.getInt("attempts"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching all users", e);
        }
        return userList;
    }

    @Override
    public User getUserById(int userId) throws CustomException {
        User user = null;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE userId = ?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("userId"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    user.setUserType(resultSet.getString("userType"));
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching user by ID", e);
        }
        return user;
    }

}

