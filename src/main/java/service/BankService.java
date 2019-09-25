package service;

import model.Account;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;
import model.Response.CreateAccountResponse;
import model.Response.CreateUserResponse;
import model.Response.TransactionResponse;
import model.User;

import java.util.List;

public interface BankService {
    // Add user
    public CreateUserResponse createUser(CreateUserRequest createUserRequest);
    // Add Account
    public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);
    // User summery
    public List<User> getAllUsers();
    // Account summery
    public List<Account> getAllAccounts();
    // transfer
    public TransactionResponse transfer(TransferRequest transferRequest);
}
