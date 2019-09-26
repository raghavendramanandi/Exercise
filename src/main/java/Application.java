import io.undertow.Undertow;

public class Application {

    public static void main(final String[] args) {
//        try{
//            new SetupData().doMain(args);
//        }catch (Exception e){
//            e.printStackTrace();
//            exit(1);
//        }

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(Routes.ROUTES).build();
        server.start();
    }
}