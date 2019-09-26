package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.UpdateBuilder;
import model.Account;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountRepository {
    private JdbcConnectionSource connectionSource;
    private Dao<Account, Integer> accountDao;

    public AccountRepository(JdbcConnectionSource connectionSource, Dao<Account, Integer> accountDao) {
        this.connectionSource = connectionSource;
        this.accountDao = accountDao;
    }

    public Account getAccountForId(int id) throws SQLException {
        List<Account> accounts = accountDao.queryForEq("id", id);
        assertEquals("More than one account with same id", false, accounts.size() > 1);
        assertEquals("No account for the given id", false, accounts.size() == 0);
        return accounts.get(0);
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.queryForAll();
    }

    public void createAccount(Account account) throws SQLException {
        accountDao.create(account);
    }

    public void updateAccount(Account account, double amount) throws SQLException {
        UpdateBuilder<Account, Integer> accountUb = accountDao.updateBuilder();
        accountUb.where().eq(Account.ACC_ID, account.getId());
        accountUb.updateColumnValue(Account.ACC_BALANCE, account.getBalance() + amount);
        accountUb.update();
    }

    public Account getAccountForDescription(String description) throws SQLException {
        List<Account> accounts = accountDao.queryForEq
                (Account.ACC_DESC, description);
        return accounts.get(0) /* description is unique*/;
    }
}
