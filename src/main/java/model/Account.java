package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.Objects;


@DatabaseTable(tableName = "account")
public class Account {

    public final static String ACC_ID = "id";
    public final static String ACC_DESC = "description";
    public final static String ACC_TYPE = "type";
    public final static String ACC_BALANCE = "balance";


    @DatabaseField(columnName = ACC_ID, generatedId = true)
    private int id;

    @DatabaseField(columnName = ACC_DESC, canBeNull = false, unique = true)
    private String description;

    @DatabaseField(columnName = ACC_TYPE, canBeNull = false)
    private String type;

    @DatabaseField(columnName = ACC_BALANCE, canBeNull = false)
    private Double balance;

    @DatabaseField(columnName = "modified_date_time", canBeNull = false)
    private Date MDT;

    @DatabaseField(columnName = "created_date_time", canBeNull = false)
    private Date CDT;

    Account() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Account(String description, String type, Double balance) {
        this.balance = balance;
        this.description = description;
        this.type = type;
        this.CDT = new Date(System.currentTimeMillis());
        this.MDT = new Date(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getMDT() {
        return MDT;
    }

    public void setMDT(Date MDT) {
        this.MDT = MDT;
    }

    public Date getCDT() {
        return CDT;
    }

    public void setCDT(Date CDT) {
        this.CDT = CDT;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getType(), getBalance(), getMDT(), getCDT());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", balance=" + balance +
                ", MDT=" + MDT +
                ", CDT=" + CDT +
                '}';
    }
}