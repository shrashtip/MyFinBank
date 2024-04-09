package in.myfin.dao;


import in.myfin.exception.CustomException;
import in.myfin.model.Account;
import in.myfin.util.DbUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {

    public void createAccount(Account account, String username) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts (userId, accountType, balance, username) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, account.getUserId());
            statement.setString(2, account.getAccountType());
            statement.setDouble(3, account.getBalance());
            statement.setString(4, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomException("Error creating account: " + ex.getMessage());
        }
    }


    // Add method to check if account number is already taken

    public boolean isAccountNumberTaken(int accountNumber) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM accounts WHERE accountId = ?")) {
            statement.setInt(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            throw new CustomException("Error checking account number availability: " + ex.getMessage());
        }
        return false;
    }


    @Override
    public void updateAccount(Account account) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE accountId = ?")) {
            statement.setDouble(1, account.getBalance());
            statement.setInt(2, account.getAccountId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error updating account", e);
        }
    }

    @Override
    public void deleteAccount(int accountId) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE accountId = ?")) {
            statement.setInt(1, accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Error deleting account", e);
        }
    }

    @Override
    public Account getAccountById(int accountId) throws CustomException {
        Account account = null;
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE accountId = ?")) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    account = new Account();
                    account.setAccountId(resultSet.getInt("accountId"));
                    account.setUserId(resultSet.getInt("userId"));
                    account.setAccountType(resultSet.getString("accountType"));
                    account.setBalance(resultSet.getDouble("balance"));
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching account by ID", e);
        }
        return account;
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) throws CustomException {
        List<Account> accountList = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM accounts WHERE userId = ?")) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setAccountId(resultSet.getInt("accountId"));
                    account.setUserId(resultSet.getInt("userId"));
                    account.setAccountType(resultSet.getString("accountType"));
                    account.setBalance(resultSet.getDouble("balance"));
                    accountList.add(account);
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching accounts by user ID", e);
        }
        return accountList;
    }
}


