import io.undertow.Undertow;

public class Application {

    public static void main(final String[] args) {
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(Routes.ROUTES).build();
        server.start();
    }
}