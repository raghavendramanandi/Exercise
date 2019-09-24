package model;

import com.j256.ormlite.field.DatabaseField;

public class User {

    // we use this field-name so we can query for users with a certain id
    public final static String ID_FIELD_NAME = "id";

    // this id is generated by the database and set on the object when it is passed to the create method
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    int id;

    @DatabaseField
    String name;

    User() {
        // for ormlite
    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}