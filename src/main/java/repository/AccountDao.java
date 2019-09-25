package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.UpdateBuilder;
import exceptions.InsufficientBalanceException;
import model.Account;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountDao {
    private JdbcConnectionSource connectionSource = null;
    private Dao<Account, Integer> accountDao;

    public AccountDao(JdbcConnectionSource connectionSource, Dao<Account, Integer> accountDao) {
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

    public void transfer(Account fromAccount, Account toAccount, double amount) throws Exception {
        TransactionManager transactionManager = new TransactionManager(connectionSource);

        try {
            transactionManager.callInTransaction(() -> {
                Account reloadedFromAccount = getAccountForId(fromAccount.getId());
                Account reloadedToAccount = getAccountForId(toAccount.getId());

                if(reloadedFromAccount.getBalance() < amount){
                    throw new InsufficientBalanceException("Insufficient funds");
                }
                updateAccount(reloadedFromAccount, debit(amount));
                updateAccount(reloadedToAccount, credit(amount));
                return true;
            });
        } finally {
        }
    }

    private void updateAccount(Account account, double amount) throws SQLException {
        UpdateBuilder<Account, Integer> accountUb = accountDao.updateBuilder();
        accountUb.where().eq(Account.ACC_ID, account.getId());
        accountUb.updateColumnValue(Account.ACC_BALANCE, account.getBalance() + amount);
        accountUb.update();
    }

    private double credit(double amount) {
        return amount;
    }

    private double debit(double amount) {
        return amount*-1;
    }
}
