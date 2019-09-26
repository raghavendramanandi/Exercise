package model.Request;

public class CreateAccountRequest {
    private String username;
    private String description;
    private String type;

    public CreateAccountRequest(String username, String description, String type) {
        this.username = username;
        this.description = description;
        this.type = type;
    }

    public CreateAccountRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
