package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Account;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountDaoTest {

    private Dao<Account, Integer> accountDao;
    private JdbcConnectionSource connectionSource = null;

    @Before
    public void setup() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:bank-test");
        TableUtils.createTable(connectionSource, Account.class);
        accountDao = DaoManager.createDao(connectionSource, Account.class);
    }
    @Test
    public void shouldReturnTheAccountWhenTheAccountExists() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        Account account =  null;
        accountDao.create(new Account("account-1", "savings", 0.0));
        account = dao.getAccountForId(1);
        assertEquals("account-1", account.getDescription());
        assertEquals("savings", account.getType());
        assertEquals(0.0, account.getBalance().doubleValue(), 0.0);
    }

    @Test(expected = java.lang.AssertionError.class)
    public void shouldThrowExceptionWhenTheAccountDoesNotExists() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        dao.getAccountForId(1);
    }

    @Test
    public void shouldBeAbleToTransferFromOneAccountToAnotherIfFundIsAvailable() throws Exception {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        accountDao.create(new Account("acc-1", "savings", 200.0));
        accountDao.create(new Account("acc-2", "savings", 0.0));
        Account acc1 = dao.getAccountForId(1);
        Account acc2 = dao.getAccountForId(2);

        assertEquals("acc-1", acc1.getDescription());
        assertEquals("acc-2", acc2.getDescription());
        assertEquals(200.0, acc1.getBalance(), 0.0);
        assertEquals(000.0, acc2.getBalance(), 0.0);

        dao.transfer(acc1, acc2, 100);

        acc1 = dao.getAccountForId(1);
        acc2 = dao.getAccountForId(2);

        assertEquals("acc-1", acc1.getDescription());
        assertEquals("acc-2", acc2.getDescription());
        assertEquals(100.0, acc1.getBalance(), 0.0);
        assertEquals(100.0, acc2.getBalance(), 0.0);
    }

    @Test(expected = SQLException.class)
    public void shouldThrowExceptionIfFundsAreNotAvailable() throws Exception {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        accountDao.create(new Account("acc-1", "savings", 0.0));
        accountDao.create(new Account("acc-2", "savings", 0.0));
        Account acc1 = dao.getAccountForId(1);
        Account acc2 = dao.getAccountForId(2);

        assertEquals("acc-1", acc1.getDescription());
        assertEquals("acc-2", acc2.getDescription());
        assertEquals(0.0, acc1.getBalance(), 0.0);
        assertEquals(000.0, acc2.getBalance(), 0.0);

        dao.transfer(acc1, acc2, 100);
    }

    @Test
    public void shouldBeAbleToCreateAccountForValidDescription() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        dao.createAccount(new Account("desc", "type", 0.0));
        assertEquals("desc", dao.getAccountForId(1).getDescription());
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToCreateAccountIfDescriptionIsDuplicate() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        dao.createAccount(new Account("desc", "type", 0.0));
        dao.createAccount(new Account("desc", "type", 0.0));
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToCreateAccountIfDescriptionIsnull() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        dao.createAccount(new Account(null, "type", 0.0));
    }

    @Test
    public void shouldListAllAccounts() throws SQLException {
        AccountDao dao = new AccountDao(connectionSource, accountDao);
        dao.createAccount(new Account("desc1", "t1", 0.0));
        dao.createAccount(new Account("desc2", "t2", 10.0));
        List<Account> accounts = dao.getAllAccounts();

        assertEquals(2, accounts.size());
    }
}