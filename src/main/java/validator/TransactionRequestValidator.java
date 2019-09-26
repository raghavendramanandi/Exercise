package validator;

import exceptions.InvalidAmountException;
import exceptions.InvalidUserException;
import exceptions.SameFromAndToAccountException;
import exceptions.UserDoesNotOwnTheAccountToTransfer;
import model.Request.TransferRequest;
import model.User;
import model.UserAccount;
import repository.UserAccountRepository;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Stream;

public class TransactionRequestValidator{
    public void validate(TransferRequest transferRequest, UserRepository userRepository, UserAccountRepository userAccountRepository) throws InvalidAmountException, SameFromAndToAccountException, InvalidUserException, Exception, UserDoesNotOwnTheAccountToTransfer {
        if(transferRequest.getAmount() < 1){
            throw new InvalidAmountException(String.format("Amount cannot be %s", transferRequest.getAmount()));
        }

        if(transferRequest.getFromAccountId() == transferRequest.getToAccountId()){
            throw new SameFromAndToAccountException(
                    String.format("From account %s cannot be same as to account %s",
                            transferRequest.getFromAccountId(),
                            transferRequest.getToAccountId()));
        }

        if(isNotAValidUser(transferRequest.getUserName(), userRepository)){
            throw new InvalidUserException(String.format("User with user name %s does not exist", transferRequest.getUserName()));
        }

        List<UserAccount> usersForAccount = userAccountRepository.getUsersForAccount(transferRequest.getFromAccountId());
        List<User> users = userRepository.selectUsersForName(transferRequest.getUserName());
        Stream<Integer> integerStream1 = users.stream().map(user -> user.getId());
        Stream<Integer> integerStream = usersForAccount.stream().map(userAccount -> userAccount.getUser().getId());
        boolean validUser = integerStream.anyMatch(is -> integerStream1.anyMatch(is1 -> is == is1));
        if(!validUser){
            throw new UserDoesNotOwnTheAccountToTransfer("User does not own the account to transfer");
        }
    }

    private boolean isNotAValidUser(String userName, UserRepository userRepository) throws Exception {
        List<User> users = userRepository.selectUsersForName(userName);
        return users.size() != 1;
    }
}
