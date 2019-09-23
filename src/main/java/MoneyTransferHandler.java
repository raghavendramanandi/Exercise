import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class MoneyTransferHandler implements HttpHandler {
    private final String value;
    public MoneyTransferHandler(String value) {
        this.value = value;
    }

    public void handleRequest(HttpServerExchange exchange) throws Exception {
//        exchange
        //call service
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(value + "\n");
    }
}