package exceptions;

public class UserDoesNotOwnTheAccountToTransfer extends Throwable {
    public UserDoesNotOwnTheAccountToTransfer(String s) {
        super(s);
    }
}
