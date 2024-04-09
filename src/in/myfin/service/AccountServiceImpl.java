package in.myfin.service;


import in.myfin.dao.AccountDAO;
import in.myfin.dao.AccountDAOImpl;
import in.myfin.exception.CustomException;
import in.myfin.model.Account;

import java.util.List;

public class AccountServiceImpl implements AccountService {
    private final AccountDAO accountDAO;

    public AccountServiceImpl() {
        this.accountDAO = new AccountDAOImpl();
    }

    // AccountServiceImpl.java
    @Override
    public void createAccount(Account account, String username) throws CustomException {
        accountDAO.createAccount(account, username);
    }

    @Override
    public void updateAccount(Account account) throws CustomException {
        accountDAO.updateAccount(account);
    }

    @Override
    public void deleteAccount(int accountId) throws CustomException {
        accountDAO.deleteAccount(accountId);
    }

    @Override
    public Account getAccountById(int accountId) throws CustomException {
        return accountDAO.getAccountById(accountId);
    }

    @Override
    public List<Account> getAccountsByUserId(int userId) throws CustomException {
        return accountDAO.getAccountsByUserId(userId);
    }
}
