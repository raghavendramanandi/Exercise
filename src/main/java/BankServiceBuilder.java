import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import exceptions.ApplicationException;
import model.Account;
import model.User;
import model.UserAccount;
import repository.AccountDao;
import repository.UserDao;
import service.BankServiceImpl;
import validator.TransactionRequestValidator;

import java.io.InputStream;
import java.util.Properties;

public class BankServiceBuilder {
    public BankServiceImpl build() throws ApplicationException, Exception {
        Properties prop = new Properties();
        InputStream input = App2.class.getClassLoader().getResourceAsStream("application.properties");
        if (input == null) {
            throw new ApplicationException("Issue while loading the properties");
        }
        prop.load(input);

        JdbcConnectionSource connectionSource;
        String DATABASE_URL  = prop.getProperty("db.url");
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        Dao<User, Integer> userDao;
        Dao<Account, Integer> accountDao;
        Dao<UserAccount, Integer> userAccountDao;

        userDao = DaoManager.createDao(connectionSource, User.class);
        accountDao = DaoManager.createDao(connectionSource, Account.class);
        userAccountDao = DaoManager.createDao(connectionSource, UserAccount.class);

        SetupData setupData = new SetupData();
        setupData.setupDatabase(connectionSource);
        setupData.readWriteData();

        return new BankServiceImpl(
                new AccountDao(connectionSource, accountDao),
                new UserDao(userDao),
                new TransactionRequestValidator());
    }
}
