package service;

import exceptions.*;
import model.Account;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;

import java.sql.SQLException;
import java.util.List;

public interface BankService {
    void createUser(CreateUserRequest createUserRequest) throws Exception;
    void createAccount(CreateAccountRequest createAccountRequest) throws Exception, InvalidUserException, InvalidTypeForAccountException, InvalidDescriptionForAccountException, InvalidUsernameForAccountException;
    List<User> getAllUsers() throws Exception;
    List<Account> getAllAccounts() throws SQLException;
    void transfer(TransferRequest transferRequest)
            throws Exception, SameFromAndToAccountException, InvalidUserException,
            InvalidAmountException, UserDoesNotOwnTheAccountToTransfer;
    List<UserAccount> getAllUserAccount() throws SQLException;
}
