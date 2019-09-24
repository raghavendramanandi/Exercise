import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import model.Account;
import model.User;
import model.UserAccount;

import java.sql.SQLException;
import java.util.List;

public class SetupData {

    // we are using the in-memory H2 database
    private final static String DATABASE_URL = "jdbc:h2:mem:bank";

    private Dao<User, Integer> userDao;
    private Dao<Account, Integer> accountDao;
    private Dao<UserAccount, Integer> userAccountDao;

    public static void main(String[] args) throws Exception {
        // turn our static method into an instance of Main
        new SetupData().doMain(args);
    }

    void doMain(String[] args) throws Exception {
        JdbcConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            // setup our database and DAOs
            setupDatabase(connectionSource);
            // read and write some data
            readWriteData();
            System.out.println("\n\nIt seems to have worked\n\n");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    private void setupDatabase(ConnectionSource connectionSource) throws Exception {
        userDao = DaoManager.createDao(connectionSource, User.class);
        accountDao = DaoManager.createDao(connectionSource, Account.class);
        userAccountDao = DaoManager.createDao(connectionSource, UserAccount.class);
        TableUtils.createTable(connectionSource, User.class);
        TableUtils.createTable(connectionSource, Account.class);
        TableUtils.createTable(connectionSource, UserAccount.class);
    }

    private void readWriteData() throws Exception {
        //Simple account
        User user1 = new User("Jim Coakley");
        userDao.create(user1);

        Account account1 = new Account("Jim's simple account", "savings", 0.0);
        accountDao.create(account1);

        UserAccount userAccount1 = new UserAccount(user1, account1);
        userAccountDao.create(userAccount1);

        //Joint account

        User user2 = new User("Mike");
        userDao.create(user2);

        User user3 = new User("Merry");
        userDao.create(user3);

        Account account2 = new Account("Mike and Merry's joint account", "savings", 0.0);
        accountDao.create(account2);
        UserAccount user2Account2 = new UserAccount(user2, account2);
        UserAccount user3Account2 = new UserAccount(user3, account2);
        userAccountDao.create(user2Account2);
        userAccountDao.create(user3Account2);

        List<Account> accounts = lookupAccountsForUser(user1);
        for (Account a: accounts) {
            System.out.println(a.toString());
        }

        List<User> users = lookupUsersForAccount(account2);
        for (User u : users) {
            System.out.println(u.toString());
        }
    }

    private PreparedQuery<Account> accountsForUserQuery = null;
    private PreparedQuery<User> usersForAccountQuery = null;

    private List<Account> lookupAccountsForUser(User user) throws SQLException {
        if (accountsForUserQuery == null) {
            accountsForUserQuery = makeAccountsForUserQuery();
        }
        accountsForUserQuery.setArgumentHolderValue(0, user);
        return accountDao.query(accountsForUserQuery);
    }

    private List<User> lookupUsersForAccount(Account account) throws SQLException {
        if (usersForAccountQuery == null) {
            usersForAccountQuery = makeUsersForAccountQuery();
        }
        usersForAccountQuery.setArgumentHolderValue(0, account);
        return userDao.query(usersForAccountQuery);
    }

    private PreparedQuery<Account> makeAccountsForUserQuery() throws SQLException {
        QueryBuilder<UserAccount, Integer> userAccountQb = userAccountDao.queryBuilder();
        userAccountQb.selectColumns(UserAccount.ACCOUNT_ID_FIELD_NAME);
        SelectArg userSelectArg = new SelectArg();
        userAccountQb.where().eq(UserAccount.USER_ID_FIELD_NAME, userSelectArg);

        QueryBuilder<Account, Integer> accountQb = accountDao.queryBuilder();
        accountQb.where().in(Account.ACC_ID, userAccountQb);
        return accountQb.prepare();
    }

    private PreparedQuery<User> makeUsersForAccountQuery() throws SQLException {
        QueryBuilder<UserAccount, Integer> userAccountQb = userAccountDao.queryBuilder();
        userAccountQb.selectColumns(UserAccount.USER_ID_FIELD_NAME);
        SelectArg accountSelectArg = new SelectArg();
        userAccountQb.where().eq(UserAccount.ACCOUNT_ID_FIELD_NAME, accountSelectArg);

        QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
        userQb.where().in(Account.ACC_ID, userAccountQb);
        return userQb.prepare();
    }
}