import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

import static java.lang.System.exit;

public class Application {
    public static void main(final String[] args) {
        try{
            new SetupData().doMain(args);
        }catch (Exception e){
            e.printStackTrace();
            exit(1);
        }

        HttpHandler ROUTES = new RoutingHandler()
                .get("/", RoutingHandlers.constantStringHandler("GET - My Homepage"))
//                .get("/myRoute", RoutingHandlers.constantStringHandler("GET - My Route"))
//                .post("/addAccount", RoutingHandlers.constantStringHandler("POST - My Route"))
                .post("/makeTransfer", RoutingHandlers.constantStringHandler("POST - My Route"))
//                .get("/myOtherRoute", RoutingHandlers.constantStringHandler("GET - My Other Route"))
//                // Wildcards and RoutingHandler had some bugs before version 1.4.8.Final
//                .get("/myRoutePrefix*", RoutingHandlers.constantStringHandler("GET - My Prefixed Route"))
                // Pass a handler as a method reference.
//                .setFallbackHandler(RoutingHandlers.notFoundHandler())
                ;

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(ROUTES).build();
        server.start();
    }
}