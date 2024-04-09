package in.myfin.service;

import in.myfin.dao.TransactionDAO;
import in.myfin.dao.TransactionDAOImpl;
import in.myfin.exception.CustomException;
import in.myfin.model.Transaction;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionDAO transactionDAO;

    public TransactionServiceImpl( ) {
        this.transactionDAO = new TransactionDAOImpl();
    }

    @Override
    public void addTransaction(Transaction transaction) throws CustomException {
        transactionDAO.addTransaction(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(int accountId) throws CustomException {
        return transactionDAO.getTransactionsByAccountId(accountId);
    }

    @Override
    public List<Transaction> getAllTransactions() throws CustomException {
        return transactionDAO.getAllTransactions();
    }
}
