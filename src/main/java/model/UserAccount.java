package model;

import com.j256.ormlite.field.DatabaseField;

public class UserAccount {

    public final static String USER_ID_FIELD_NAME = "user_id";
    public final static String ACCOUNT_ID_FIELD_NAME = "account_id";

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
    User user;

    @DatabaseField(foreign = true, columnName = ACCOUNT_ID_FIELD_NAME)
    Account account;

    UserAccount() {
    }

    public UserAccount(User user, Account account) {
        this.user = user;
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", account=" + account.getId() +
                '}';
    }
}