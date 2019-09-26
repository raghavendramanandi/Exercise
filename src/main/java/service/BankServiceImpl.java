package service;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import exceptions.*;
import model.Account;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;
import repository.AccountDao;
import repository.UserAccountDao;
import repository.UserDao;
import utils.Util;
import validator.CreateAccountValidator;
import validator.TransactionRequestValidator;

import java.sql.SQLException;
import java.util.List;

public class BankServiceImpl implements BankService{
    private AccountDao accountDao;
    private UserDao userDao;
    private TransactionRequestValidator transactionRequestValidator;
    private CreateAccountValidator createAccountValidator;
    private JdbcConnectionSource connectionSource;
    private UserAccountDao userAccountDao;

    public BankServiceImpl(AccountDao accountDao, UserDao userDao
            , TransactionRequestValidator transactionRequestValidator, CreateAccountValidator createAccountValidator
    , JdbcConnectionSource connectionSource, UserAccountDao userAccountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transactionRequestValidator = transactionRequestValidator;
        this.createAccountValidator = createAccountValidator;
        this.connectionSource = connectionSource;
        this.userAccountDao = userAccountDao;
    }

    public void createUser(CreateUserRequest createUserRequest) throws Exception {
        userDao.createUser(new User(createUserRequest.getUserName()));
    }

    public void createAccount(CreateAccountRequest createAccountRequest) throws Exception, InvalidUserException,
            InvalidTypeForAccountException, InvalidDescriptionForAccountException, InvalidUsernameForAccountException {
        createAccountValidator.validate(createAccountRequest, userDao);
        /*
        * validation need not be inside transaction as if when checked, if the user does not exist an exception will be thrown.
        * If the user is already there, since we are not modifying user table it is fine
        */
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            transactionManager.callInTransaction(() -> {
                accountDao.createAccount(new Account(
                        createAccountRequest.getDescription(),
                        createAccountRequest.getType(), 0.0));
                userAccountDao.createUserAccount(
                        new UserAccount(
                                userDao.selectUsersForName(createAccountRequest.getUsername()).get(0),
                                accountDao.getAccountForDescription(createAccountRequest.getDescription()
                                )));
                return true;
            });
        }finally {
        }
    }

    public List<User> getAllUsers() throws Exception {
        return userDao.selectAllUsers();
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.getAllAccounts();
    }

    public void transfer(TransferRequest transferRequest)
            throws Exception, SameFromAndToAccountException, InvalidUserException,
            InvalidAmountException, UserDoesNotOwnTheAccountToTransfer {
        transactionRequestValidator.validate(transferRequest, userDao, userAccountDao);
        double amount = Util.round(transferRequest.getAmount());
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            transactionManager.callInTransaction(() -> {
                Account fromAccount = accountDao.getAccountForId(transferRequest.getFromAccountId());
                Account toAccount = accountDao.getAccountForId(transferRequest.getToAccountId());

                if(fromAccount.getBalance() < amount){
                    throw new InsufficientBalanceException("Insufficient funds");
                }
                accountDao.updateAccount(fromAccount, debit(amount));
                accountDao.updateAccount(toAccount, credit(amount));
                return true;
            });
        } finally {
        }
    }

    public List<UserAccount> getAllUserAccount() throws SQLException {
        return userAccountDao.getAllUserAccounts();
    }

    private double credit(double amount) {
        return amount;
    }

    private double debit(double amount) {
        return amount*-1;
    }
}
