package model.Request;

public class CreateUserRequest {
    private String userName;

    public CreateUserRequest(String userName) {
        this.userName = userName;
    }

    public CreateUserRequest() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
