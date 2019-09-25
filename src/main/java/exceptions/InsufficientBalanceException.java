package exceptions;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String insufficient_funds) {
        super(insufficient_funds);
    }
}
