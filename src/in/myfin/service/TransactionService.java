package in.myfin.service;

import in.myfin.exception.CustomException;
import in.myfin.model.Transaction;

import java.util.List;

public interface TransactionService {
    void addTransaction(Transaction transaction) throws CustomException;
    List<Transaction> getTransactionsByAccountId(int accountId) throws CustomException;
    List<Transaction> getAllTransactions() throws CustomException;
}
