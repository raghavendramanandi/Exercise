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
import repository.AccountRepository;
import repository.UserAccountRepository;
import repository.UserRepository;
import utils.Util;
import validator.CreateAccountValidator;
import validator.TransactionRequestValidator;

import java.sql.SQLException;
import java.util.List;

public class BankServiceImpl implements BankService{
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private TransactionRequestValidator transactionRequestValidator;
    private CreateAccountValidator createAccountValidator;
    private JdbcConnectionSource connectionSource;
    private UserAccountRepository userAccountRepository;

    public BankServiceImpl(AccountRepository accountRepository, UserRepository userRepository
            , TransactionRequestValidator transactionRequestValidator, CreateAccountValidator createAccountValidator
    , JdbcConnectionSource connectionSource, UserAccountRepository userAccountRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRequestValidator = transactionRequestValidator;
        this.createAccountValidator = createAccountValidator;
        this.connectionSource = connectionSource;
        this.userAccountRepository = userAccountRepository;
    }

    public void createUser(CreateUserRequest createUserRequest) throws Exception {
        userRepository.createUser(new User(createUserRequest.getUserName()));
    }

    public void createAccount(CreateAccountRequest createAccountRequest) throws Exception, InvalidUserException,
            InvalidTypeForAccountException, InvalidDescriptionForAccountException, InvalidUsernameForAccountException {
        createAccountValidator.validate(createAccountRequest, userRepository);
        /*
        * validation need not be inside transaction as if when checked, if the user does not exist an exception will be thrown.
        * If the user is already there, since we are not modifying user table it is fine
        */
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            transactionManager.callInTransaction(() -> {
                accountRepository.createAccount(new Account(
                        createAccountRequest.getDescription(),
                        createAccountRequest.getType(), 0.0));
                userAccountRepository.createUserAccount(
                        new UserAccount(
                                userRepository.selectUsersForName(createAccountRequest.getUsername()).get(0),
                                accountRepository.getAccountForDescription(createAccountRequest.getDescription()
                                )));
                return true;
            });
        }finally {
        }
    }

    public List<User> getAllUsers() throws Exception {
        return userRepository.selectAllUsers();
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountRepository.getAllAccounts();
    }

    public synchronized void transfer(TransferRequest transferRequest)
            throws Exception, SameFromAndToAccountException, InvalidUserException,
            InvalidAmountException, UserDoesNotOwnTheAccountToTransfer {
        transactionRequestValidator.validate(transferRequest, userRepository, userAccountRepository);
        double amount = Util.round(transferRequest.getAmount());
        TransactionManager transactionManager = new TransactionManager(connectionSource);
        try {
            transactionManager.callInTransaction(() -> {
                Account fromAccount = accountRepository.getAccountForId(transferRequest.getFromAccountId());
                Account toAccount = accountRepository.getAccountForId(transferRequest.getToAccountId());

                if(fromAccount.getBalance() < amount){
                    throw new InsufficientBalanceException("Insufficient funds");
                }
                accountRepository.updateAccount(fromAccount, debit(amount));
                accountRepository.updateAccount(toAccount, credit(amount));
                return true;
            });
        } finally {
        }
    }

    public List<UserAccount> getAllUserAccount() throws SQLException {
        return userAccountRepository.getAllUserAccounts();
    }

    private double credit(double amount) {
        return amount;
    }

    private double debit(double amount) {
        return amount*-1;
    }
}
