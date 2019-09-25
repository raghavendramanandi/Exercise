package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import model.User;

import java.sql.SQLException;
import java.util.List;

public class UserDao {

    private Dao<User, Integer> userDao;
    private PreparedQuery<User> userForName = null;
    private PreparedQuery<User> allUsers = null;

    public UserDao(Dao<User, Integer> userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) throws Exception {
        userDao.create(user);
    }

    public List<User> selectUsersForName(String name) throws Exception{
        if (userForName == null) {
            userForName = makeAccountsForUserQuery();
        }
        userForName.setArgumentHolderValue(0, name);
        return userDao.query(userForName);
    }

    private PreparedQuery<User> makeAccountsForUserQuery() throws SQLException {
        QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
        SelectArg userSelectArg = new SelectArg();
        userQb.where().eq(User.NAME, userSelectArg);
        return userQb.prepare();
    }

    public List<User> selectAllUsers() throws Exception{
        if (allUsers == null) {
            QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
            allUsers = userQb.prepare();
        }
        return userDao.query(allUsers);
    }

}
