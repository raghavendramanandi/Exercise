package service;

import enums.Status;
import exceptions.InvalidAmountException;
import exceptions.InvalidUserException;
import exceptions.SameFromAndToAccountException;
import model.Account;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;
import model.Response.CreateAccountResponse;
import model.Response.CreateUserResponse;
import model.Response.TransactionResponse;
import model.User;
import repository.AccountDao;
import repository.UserDao;
import validator.TransactionRequestValidator;

import java.sql.SQLException;
import java.util.List;

public class BankServiceImpl {
    private AccountDao accountDao;
    private UserDao userDao;
    private TransactionRequestValidator transactionRequestValidator;

    public BankServiceImpl(AccountDao accountDao, UserDao userDao, TransactionRequestValidator transactionRequestValidator) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transactionRequestValidator = transactionRequestValidator;
    }

    public CreateUserResponse createUser(CreateUserRequest createUserRequest) throws Exception {
        userDao.createUser(new User(createUserRequest.getUserName()));
        return new CreateUserResponse(Status.OK);
    }

    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        return null;
    }

    public List<User> getAllUsers() throws Exception {
        return userDao.selectAllUsers();
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.getAllAccounts();
    }

    public TransactionResponse transfer(TransferRequest transferRequest) throws Exception, SameFromAndToAccountException, InvalidUserException, InvalidAmountException {
        transactionRequestValidator.validate(transferRequest, userDao);
        accountDao.transfer(
                accountDao.getAccountForId(transferRequest.getFromAccountId()),
                accountDao.getAccountForId(transferRequest.getToAccountId()),
                transferRequest.getAmount()
        );
        return new TransactionResponse(Status.OK);
    }
}
