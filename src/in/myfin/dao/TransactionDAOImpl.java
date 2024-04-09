package in.myfin.dao;

import in.myfin.exception.CustomException;
import in.myfin.model.Transaction;
import in.myfin.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void addTransaction(Transaction transaction) throws CustomException {
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO transactions (accountId, amount, transactionType) VALUES (?, ?, ?)")) {
            statement.setInt(1, transaction.getAccountId());
            statement.setDouble(2, transaction.getAmount());
            statement.setString(3, transaction.getTransactionType());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomException("Error adding transaction: " + ex.getMessage());
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) throws CustomException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE accountId = ?")) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(resultSet.getInt("transactionId"));
                    transaction.setAccountId(resultSet.getInt("accountId"));
                    transaction.setAmount(resultSet.getDouble("amount"));
                    transaction.setTransactionType(resultSet.getString("transactionType"));
                    transaction.setTransactionDate(resultSet.getTimestamp("transactionDate").toLocalDateTime());
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching transactions by account ID", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getAllTransactions() throws CustomException {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setTransactionId(resultSet.getInt("transactionId"));
                    transaction.setAccountId(resultSet.getInt("accountId"));
                    transaction.setAmount(resultSet.getDouble("amount"));
                    transaction.setTransactionType(resultSet.getString("transactionType"));
                    transaction.setTransactionDate(resultSet.getTimestamp("transactionDate").toLocalDateTime());
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            throw new CustomException("Error fetching all transactions", e);
        }
        return transactions;
    }
}

