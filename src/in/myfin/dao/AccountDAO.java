package in.myfin.dao;

import in.myfin.exception.CustomException;
import in.myfin.model.Account;

import java.util.List;

public interface AccountDAO {
    void createAccount(Account account, String username) throws CustomException;
    void updateAccount(Account account) throws CustomException;
    void deleteAccount(int accountId) throws CustomException;
    Account getAccountById(int accountId) throws CustomException;
    List<Account> getAccountsByUserId(int userId) throws CustomException;



}

