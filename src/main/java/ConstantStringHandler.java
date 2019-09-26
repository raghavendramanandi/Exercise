import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import model.Request.CreateAccountRequest;
import model.Request.CreateUserRequest;
import model.Request.TransferRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConstantStringHandler implements HttpHandler {
    private final String value;
    private static final String SUCCESS = "Success";
    private String output;

    public ConstantStringHandler(String value) {
        this.value = value;
    }

    public void handleRequest(HttpServerExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        String body;
        try{
            switch (value){
                case "allUsers":
                    output = prettyPrint(BankSingleton.getInstance().getAllUsers().toArray());
                    break;
                case "allAccounts":
                    output = prettyPrint(BankSingleton.getInstance().getAllAccounts().toArray());
                    break;
                case "allUserAccount":
                    output = prettyPrint(BankSingleton.getInstance().getAllUserAccount().toArray());
                    break;
                case "createAccount":
                    body = emptyCheck(getBodyFromExchangeForPostRequest(exchange));
                    BankSingleton.getInstance().createAccount(mapper.readValue(body, CreateAccountRequest.class));
                    output = SUCCESS;
                    break;
                case "createUser":
                    body = emptyCheck(getBodyFromExchangeForPostRequest(exchange));
                    BankSingleton.getInstance().createUser(mapper.readValue(body, CreateUserRequest.class));
                    output = SUCCESS;
                    break;
                case "transfer":
                    body = emptyCheck(getBodyFromExchangeForPostRequest(exchange));
                    BankSingleton.getInstance().transfer(mapper.readValue(body, TransferRequest.class));
                    output = SUCCESS;
                    break;
                case "healthCheck":
                    output = "Ready to server";
                    break;
            }
        }catch (Exception | ApplicationException | InvalidUserException |
                InvalidTypeForAccountException | InvalidDescriptionForAccountException |
                InvalidUsernameForAccountException | SameFromAndToAccountException |
                InvalidAmountException | PostRequestBodyCannotBeEmptyException |
                UserDoesNotOwnTheAccountToTransfer e){
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Error, Please contact admin for more details");
            errorMessage.append("\n");
            errorMessage.append("==more details==");
            errorMessage.append("\n");
            errorMessage.append(e.getMessage());
            errorMessage.append(e.getCause());
            output = errorMessage.toString();
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(output + "\n");
    }

    private String emptyCheck(String str) throws PostRequestBodyCannotBeEmptyException {
        if(str == null || str.length() == 0){
            throw new PostRequestBodyCannotBeEmptyException("Post request boddy cannot be empty");
        }
        return str;
    }

    private String prettyPrint(Object[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o: array) {
            stringBuilder.append(o.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private String getBodyFromExchangeForPostRequest(HttpServerExchange exchange) throws IOException {
        BufferedReader reader;
        StringBuilder builder = new StringBuilder( );
        String line;

        reader = new BufferedReader( new InputStreamReader( exchange.getInputStream( ) ) );
        while( ( line = reader.readLine( ) ) != null ) {
            builder.append( line );
        }
        return builder.toString();
    }
}