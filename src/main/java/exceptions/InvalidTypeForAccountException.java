package exceptions;

public class InvalidTypeForAccountException extends Throwable {
    public InvalidTypeForAccountException(String invalid_type) {
        super(invalid_type);
    }
}
