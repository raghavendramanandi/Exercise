package validator;

import exceptions.InvalidAmountException;
import exceptions.InvalidUserException;
import exceptions.SameFromAndToAccountException;
import exceptions.UserDoesNotOwnTheAccountToTransfer;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;
import repository.UserAccountDao;
import repository.UserDao;

import java.util.List;
import java.util.stream.Stream;

public class TransactionRequestValidator{
    public void validate(TransferRequest transferRequest, UserDao userDao, UserAccountDao userAccountDao) throws InvalidAmountException, SameFromAndToAccountException, InvalidUserException, Exception, UserDoesNotOwnTheAccountToTransfer {
        if(transferRequest.getAmount() < 1){
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

        List<UserAccount> usersForAccount = userAccountDao.getUsersForAccount(transferRequest.getFromAccountId());
        List<User> users = userDao.selectUsersForName(transferRequest.getUserName());
        Stream<Integer> integerStream1 = users.stream().map(user -> user.getId());
        Stream<Integer> integerStream = usersForAccount.stream().map(userAccount -> userAccount.getUser().getId());
        boolean validUser = integerStream.anyMatch(is -> integerStream1.anyMatch(is1 -> is == is1));
        if(!validUser){
            throw new UserDoesNotOwnTheAccountToTransfer("User doesnot own the account to transfer");
        }
    }

    private boolean isNotAValidUser(String userName, UserDao userDao) throws Exception {
        List<User> users = userDao.selectUsersForName(userName);
        return users.size() != 1;
    }
}
