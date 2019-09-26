import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;

public class Routes {
    public static final HttpHandler ROUTES = new BlockingHandler( new RoutingHandler()
            .get("/allUsers", RoutingHandlers.constantStringHandler("allUsers"))
            .get("/allAccounts", RoutingHandlers.constantStringHandler("allAccounts"))
            .get("/allUserAccount", RoutingHandlers.constantStringHandler("allUserAccount"))
            .post("/createAccount", RoutingHandlers.constantStringHandler("createAccount"))
            .post("/createUser", RoutingHandlers.constantStringHandler("createUser"))
            .post("/transfer", RoutingHandlers.constantStringHandler("transfer"))
            .get("/healthCheck", RoutingHandlers.constantStringHandler("healthCheck"))
            .setFallbackHandler(RoutingHandlers::notFoundHandler));
}
