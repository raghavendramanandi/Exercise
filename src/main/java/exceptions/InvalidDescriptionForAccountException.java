package exceptions;

public class InvalidDescriptionForAccountException extends Throwable {
    public InvalidDescriptionForAccountException(String invalid_description) {
        super(invalid_description);
    }
}
