package model.Request;

public class TransferRequest {
    private int fromAccountId;
    private int toAccountId;
    private Double amount;
    private String userName;

    public TransferRequest(int fromAccountId, int toAccountId, Double amount, String userName) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.userName = userName;
    }

    public TransferRequest() {
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getUserName() {
        return userName;
    }
}
