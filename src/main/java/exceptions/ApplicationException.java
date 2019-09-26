package exceptions;

public class ApplicationException extends Throwable {
    public ApplicationException(String issue_while_loading_the_properties) {
        super(issue_while_loading_the_properties);
    }
}
