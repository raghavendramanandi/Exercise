package validator;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import exceptions.InvalidAmountException;
import exceptions.InvalidUserException;
import exceptions.SameFromAndToAccountException;
import exceptions.UserDoesNotOwnTheAccountToTransfer;
import model.Account;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import repository.UserAccountDao;
import repository.UserDao;

import java.io.IOException;
import java.sql.SQLException;

public class TransactionRequestValidatorTest {
    private Dao<User, Integer> userDao;
    private Dao<Account, Integer> accountDao;
    private Dao<UserAccount, Integer> userAccountDao;
    private JdbcConnectionSource connectionSource = null;
    private TransactionRequestValidator taTransactionRequestValidator;

    @Before
    public void setup() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:bank-test");
        TableUtils.createTable(connectionSource, User.class);
        TableUtils.createTable(connectionSource, Account.class);
        TableUtils.createTable(connectionSource, UserAccount.class);
        userDao = DaoManager.createDao(connectionSource, User.class);
        accountDao = DaoManager.createDao(connectionSource, Account.class);
        userAccountDao = DaoManager.createDao(connectionSource, UserAccount.class);
        taTransactionRequestValidator = new TransactionRequestValidator();
    }

    @After
    public void taredown() throws IOException {
        if (connectionSource != null) {
            connectionSource.close();
        }
    }

    @Test(expected = InvalidAmountException.class)
    public void shouldThrowInvalidAmountExceptionIfAmountIsLessThan1() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
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

        TransferRequest transferRequest =  new TransferRequest(1,2,0.20, "u1");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test(expected = SameFromAndToAccountException.class)
    public void shouldNotAllowTransferFormSameAccounts() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
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

        TransferRequest transferRequest =  new TransferRequest(1,1,10.00, "u1");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test(expected = InvalidUserException.class)
    public void shouldNotAllowTransferIfTheUserIsInvalid() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
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

        TransferRequest transferRequest =  new TransferRequest(1,2,10.00, "u11");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test(expected = UserDoesNotOwnTheAccountToTransfer.class)
    public void shouldNotBeAbleToTransferIfUserDoesNotOwnTheAccount() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
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

        TransferRequest transferRequest =  new TransferRequest(1,2,10.00, "u2");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test
    public void shouldPassValidationIfThereAreNoIssues() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
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

        TransferRequest transferRequest =  new TransferRequest(1,2,10.00, "u1");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test
    public void shouldBeAbleToTransferIfFromAccountIsJointAccountAndOneOfTheUserIsTransferring() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
        User user1 = new User("u1");
        User user2 = new User("u2");
        User user3 = new User("u3");

        Account fromAccount = new Account("d1", "t", 100.0);
        Account toAccount = new Account("d2", "t", 0.0);

        userDao.create(user1);
        userDao.create(user2);
        accountDao.create(fromAccount);
        accountDao.create(toAccount);
        userAccountDao.create(new UserAccount(user1, fromAccount));
        userAccountDao.create(new UserAccount(user3, fromAccount));
        userAccountDao.create(new UserAccount(user2, toAccount));

        TransferRequest transferRequest =  new TransferRequest(1,2,10.00, "u1");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }

    @Test
    public void shouldBeAbleToTransferIfUserOwnsMoreThanOneAccountAndOwnsTheFromAccount() throws Exception, InvalidUserException, InvalidAmountException, UserDoesNotOwnTheAccountToTransfer, SameFromAndToAccountException {
        User user1 = new User("u1");
        User user2 = new User("u2");

        Account fromAccount = new Account("d1", "t", 100.0);
        Account toAccount = new Account("d2", "t", 0.0);
        Account otherAccount = new Account("d2", "t", 0.0);

        userDao.create(user1);
        userDao.create(user2);
        accountDao.create(fromAccount);
        accountDao.create(toAccount);
        userAccountDao.create(new UserAccount(user1, fromAccount));
        userAccountDao.create(new UserAccount(user1, otherAccount));
        userAccountDao.create(new UserAccount(user2, toAccount));

        TransferRequest transferRequest =  new TransferRequest(1,2,10.00, "u1");
        taTransactionRequestValidator.validate(transferRequest, new UserDao(userDao), new UserAccountDao(userAccountDao));
    }
}