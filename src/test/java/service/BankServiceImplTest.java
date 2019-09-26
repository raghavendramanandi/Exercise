package service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import exceptions.*;
import model.Account;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.AccountDao;
import repository.UserAccountDao;
import repository.UserDao;
import validator.CreateAccountValidator;
import validator.TransactionRequestValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BankServiceImplTest {

    private Dao<User, Integer> userDao;
    private Dao<Account, Integer> accountDao;
    private Dao<UserAccount, Integer> userAccountDao;
    private JdbcConnectionSource connectionSource = null;
    private TransactionRequestValidator transactionRequestValidator;
    private CreateAccountValidator createAccountValidator;
    private BankService bankService;

    @Before
    public void setup() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:bank-test");

        TableUtils.createTable(connectionSource, User.class);
        TableUtils.createTable(connectionSource, Account.class);
        TableUtils.createTable(connectionSource, UserAccount.class);
        userDao = DaoManager.createDao(connectionSource, User.class);
        accountDao = DaoManager.createDao(connectionSource, Account.class);
        userAccountDao = DaoManager.createDao(connectionSource, UserAccount.class);
        transactionRequestValidator = new TransactionRequestValidator();
        createAccountValidator = new CreateAccountValidator();
        bankService = new BankServiceImpl(new AccountDao(connectionSource, accountDao), new UserDao(userDao),
                transactionRequestValidator, createAccountValidator, connectionSource, new UserAccountDao(userAccountDao));
    }

    @After
    public void taredown() throws IOException {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }

    @Test
    public void createUser() throws Exception {
        bankService.createUser(new CreateUserRequest("u1"));
        assertEquals("u1", userDao.queryForId(1).getName());
    }

    @Test
    public void createAccount() throws InvalidUserException, InvalidTypeForAccountException, InvalidDescriptionForAccountException, Exception, InvalidUsernameForAccountException {
        bankService.createUser(new CreateUserRequest("u1"));
        bankService.createAccount(new CreateAccountRequest("u1", "d1", "t1"));
        assertEquals("d1", accountDao.queryForId(1).getDescription());
        assertEquals("t1", accountDao.queryForId(1).getType());
        assertEquals(0.0, accountDao.queryForId(1).getBalance(), 0.0);
        assertEquals(1, userAccountDao.queryForId(1).getUser().getId());
        assertEquals(1, userAccountDao.queryForId(1).getAccount().getId());
    }

    @Test
    public void getAllUsers() throws Exception, InvalidUserException, InvalidTypeForAccountException, InvalidDescriptionForAccountException, InvalidUsernameForAccountException {
        bankService.createUser(new CreateUserRequest("u1"));
        bankService.createAccount(new CreateAccountRequest("u1", "d1", "t1"));
        List<User> allUsers = bankService.getAllUsers();
        assertEquals(1, allUsers.size());
        assertEquals("u1", allUsers.get(0).getName());
    }

    @Test
    public void getAllAccounts()  throws Exception, InvalidUserException, InvalidTypeForAccountException, InvalidDescriptionForAccountException, InvalidUsernameForAccountException {
        bankService.createUser(new CreateUserRequest("u1"));
        bankService.createAccount(new CreateAccountRequest("u1", "d1", "t1"));
        List<Account> allAccounts = bankService.getAllAccounts();
        assertEquals(1, allAccounts.size());
        assertEquals("d1", allAccounts.get(0).getDescription());
    }

    @Test
    public void transfer() throws InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, Exception, SameFromAndToAccountException {
        User user1 = new User("u1");
        User user2 = new User("u2");

        Account fromAccount = new Account("d1", "t", 100.0);
        Account toAccount = new Account("d2", "t", 0.0);

        userDao.create(user1);
        userDao.create(user2);
        accountDao.create(fromAccount);
        accountDao.create(toAccount);
        userAccountDao.create(new UserAccount(user1, fromAccount));
        userAccountDao.create(new UserAccount(user2, toAccount));

        bankService.transfer(new TransferRequest(1, 2, 10.0, "u1"));

        assertEquals(90.0, accountDao.queryForId(1).getBalance(), 0.0);
        assertEquals(10.0, accountDao.queryForId(2).getBalance(), 0.0);
    }

    @Test
    public void getAllUserAccount() throws InvalidUserException, InvalidTypeForAccountException, InvalidDescriptionForAccountException, Exception, InvalidUsernameForAccountException {
        bankService.createUser(new CreateUserRequest("u1"));
        bankService.createAccount(new CreateAccountRequest("u1", "d1", "t1"));
        List<UserAccount> allUserAccount = bankService.getAllUserAccount();

        assertEquals(1, allUserAccount.size());
        assertEquals(1, allUserAccount.get(0).getUser().getId());
        assertEquals(1, allUserAccount.get(0).getAccount().getId());
    }
}