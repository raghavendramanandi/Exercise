import exceptions.ApplicationException;
import service.BankService;

public class BankSingleton {
    private BankSingleton() {
    }

    private static BankService bankService;

    public static BankService getInstance() throws Exception, ApplicationException {
        if(bankService == null){
            synchronized (BankSingleton.class) {
                if(bankService == null){
                    bankService = new BankServiceBuilder().build("application.properties");
                }
            }
        }
        return bankService;
    }
}
