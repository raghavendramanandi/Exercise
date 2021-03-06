package validator;

import exceptions.InvalidDescriptionForAccountException;
import exceptions.InvalidTypeForAccountException;
import exceptions.InvalidUserException;
import exceptions.InvalidUsernameForAccountException;
import model.Request.CreateAccountRequest;
import model.User;
import repository.UserRepository;

import java.util.List;

public class CreateAccountValidator {
    public void validate(CreateAccountRequest createAccountRequest, UserRepository userRepository) throws InvalidUserException, Exception, InvalidUsernameForAccountException, InvalidTypeForAccountException, InvalidDescriptionForAccountException {
        if(createAccountRequest.getDescription() == null || createAccountRequest.getDescription().length() == 0){
            throw new InvalidDescriptionForAccountException("Invalid description");
        }

        if(createAccountRequest.getType() == null || createAccountRequest.getType().length() == 0){
            throw new InvalidTypeForAccountException("Invalid type");
        }

        if(createAccountRequest.getUsername() == null || createAccountRequest.getUsername().length() == 0){
            throw new InvalidUsernameForAccountException("Invalid username");
        }

        if(isNotAValidUser(createAccountRequest.getUsername(), userRepository)){
            throw new InvalidUserException(String.format("User with user name %s does not exist", createAccountRequest.getUsername()));
        }
    }

    private boolean isNotAValidUser(String userName, UserRepository userRepository) throws Exception {
        List<User> users = userRepository.selectUsersForName(userName);
        return users.size() != 1;
    }
}
