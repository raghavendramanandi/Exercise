package validator;

import exceptions.InvalidAmountException;
import exceptions.InvalidUserException;
import exceptions.SameFromAndToAccountException;
import model.Request.TransferRequest;
import model.User;
import repository.UserDao;

import java.util.List;

public class TransactionRequestValidator{
    public void validate(TransferRequest transferRequest, UserDao userDao) throws InvalidAmountException, SameFromAndToAccountException, InvalidUserException, Exception {
        if(transferRequest.getAmount() < 0){
            throw new InvalidAmountException(String.format("Amount cannot be %s", transferRequest.getAmount()));
        }

        if(transferRequest.getFromAccountId() == transferRequest.getToAccountId()){
            throw new SameFromAndToAccountException(
                    String.format("From account %s cannot be same as to account %s",
                            transferRequest.getFromAccountId(),
                            transferRequest.getToAccountId()));
        }

        if(isNotAValidUser(transferRequest.getUserName(), userDao)){
            throw new InvalidUserException(String.format("User with user name %s does not exist", transferRequest.getUserName()));
        }

        //TODO Validate if the user who is transfering owns the account
    }

    private boolean isNotAValidUser(String userName, UserDao userDao) throws Exception {
        List<User> users = userDao.selectUsersForName(userName);
        return users.size() != 1;
    }
}
