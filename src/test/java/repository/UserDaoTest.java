package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {

    private Dao<User, Integer> userDao;

    @Before
    public void setup() throws SQLException {
        JdbcConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource("jdbc:h2:mem:bank-test");
            TableUtils.createTable(connectionSource, User.class);
            userDao = DaoManager.createDao(connectionSource, User.class);
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
    }

    @Test
    public void shouldBeAbleToCraeteUserWhenValidNameIsProvided() throws Exception {
        List<User> users = null;
        PreparedQuery<User> allUsers = null;

        UserDao dao = new UserDao(userDao);
        try{
            dao.createUser(new User("mike"));
            QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
            allUsers = userQb.prepare();
            users = userDao.query(allUsers);
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(1, users.size());
        assertEquals("mike", users.get(0).getName());
    }

    @Test(expected = SQLException.class)
    public void shouldThrowExceptionWhenNameIsNull() throws Exception {
        List<User> users;
        PreparedQuery<User> allUsers;

        UserDao dao = new UserDao(userDao);
        try{
            dao.createUser(new User(null));
            QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
            allUsers = userQb.prepare();
            users = userDao.query(allUsers);
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(1, users.size());
        assertEquals("mike", users.get(0).getName());
    }

    @Test
    public void shouldListAllMatchingUsersWhenSomeAreMatchingTheName() throws Exception {
        List<User> users = null;
        UserDao dao = new UserDao(userDao);
        try{
            userDao.create(new User("mike"));
            userDao.create(new User("mike1"));
            users = dao.selectUsersForName("mike");
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(1, users.size());
        assertEquals("mike", users.get(0).getName());
    }

    @Test
    public void shouldListEmptyUsersWhenNoneAreMatchingTheName() throws Exception {
        List<User> users = null;
        UserDao dao = new UserDao(userDao);
        try{
            userDao.create(new User("mike"));
            userDao.create(new User("mike1"));
            users = dao.selectUsersForName("mike2");
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(0, users.size());
    }

    @Test
    public void shouldListAllUsersWhenUsersAreProvided() throws Exception {
        List<User> users = null;
        UserDao dao = new UserDao(userDao);
        try{
            userDao.create(new User("mike"));
            users = dao.selectAllUsers();
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(1, users.size());
        assertEquals("mike", users.get(0).getName());
    }

    @Test
    public void shouldListEmptyUsersWhenNoUsersAreProvided() throws Exception {
        List<User> users = null;
        UserDao dao = new UserDao(userDao);
        try{
            users = dao.selectAllUsers();
        }catch (Exception e){
            e.printStackTrace(); throw e;
        }
        assertEquals(0, users.size());
    }
}