package repository;

import com.j256.ormlite.dao.Dao;
import model.UserAccount;

import java.sql.SQLException;
import java.util.List;

public class UserAccountRepository {
    private Dao<UserAccount, Integer> userAccountDao;

    public UserAccountRepository(Dao<UserAccount, Integer> userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    public void createUserAccount(UserAccount userAccount) throws SQLException {
        userAccountDao.create(userAccount);
    }

    public List<UserAccount> getUsersForAccount(int accountId) throws SQLException {
        return userAccountDao.queryForEq(UserAccount.ACCOUNT_ID_FIELD_NAME, accountId);
    }

    public List<UserAccount> getAccountsForUser(int userId) throws SQLException {
        return userAccountDao.queryForEq(UserAccount.USER_ID_FIELD_NAME, userId);
    }

    public List<UserAccount> getAllUserAccounts() throws SQLException {
        return userAccountDao.queryForAll();
    }
}
