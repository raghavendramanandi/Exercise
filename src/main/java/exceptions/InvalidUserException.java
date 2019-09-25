package exceptions;

public class InvalidUserException extends Throwable {
    public InvalidUserException(String message) {
        super(message);
    }
}
