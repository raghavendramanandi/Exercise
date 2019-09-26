import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;

public class Routes {
    public static final HttpHandler ROUTES = new BlockingHandler( new RoutingHandler()
            .get("/allUsers", RoutingHandlers.constantStringHandler("allUsers"))
            .get("/exception", RoutingHandlers.constantStringHandler("exception"))
            .get("/createAccount", RoutingHandlers.constantStringHandler("createAccount"))
            .post("/createUser", RoutingHandlers.constantStringHandler("createUser"))
            .get("/myOtherRoute", RoutingHandlers.constantStringHandler("GET - My Other Route"))
            // Wildcards and RoutingHandler had some bugs before version 1.4.8.Final
            .get("/myRoutePrefix*", RoutingHandlers.constantStringHandler("GET - My Prefixed Route"))
            // Pass a handler as a method reference.
            .setFallbackHandler(RoutingHandlers::notFoundHandler));
}
