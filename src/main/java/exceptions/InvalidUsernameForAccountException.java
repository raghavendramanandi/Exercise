package exceptions;

public class InvalidUsernameForAccountException extends Throwable {
    public InvalidUsernameForAccountException(String invalid_username) {
        super(invalid_username);
    }
}
