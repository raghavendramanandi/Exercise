import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ApplicationException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import model.Request.CreateUserRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ConstantStringHandler implements HttpHandler {
    private final String value;
    private static final String SUCCESS = "Success";
    private String output;

    public ConstantStringHandler(String value) {
        this.value = value;
    }

    public void handleRequest(HttpServerExchange exchange) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            switch (value){
                case "allUsers":
                    output = Arrays.toString(BankSingleton.getInstance().getAllUsers().toArray());
                    break;
                case "createUser":
                    String body = getBodyFromExchange(exchange);
                    BankSingleton.getInstance().createUser(mapper.readValue(body, CreateUserRequest.class));
                    output = SUCCESS;
                    break;
                case "exception":
                    throw new Exception();
            }
        }catch (Exception | ApplicationException e){
            e.printStackTrace();
            output = "Error, Please contact admin for more details";
        }
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.setStatusCode(200);
        exchange.getResponseSender().send(output + "\n");
    }

    private String getBodyFromExchange(HttpServerExchange exchange) throws IOException {
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