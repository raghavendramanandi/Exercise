import exceptions.ApplicationException;
import service.BankServiceImpl;

public class BankSingleton {
    private BankSingleton() {
    }

    private static BankServiceImpl bankService;

    public static BankServiceImpl getInstance() throws Exception, ApplicationException {
        if(bankService == null){
            synchronized (BankSingleton.class) {
                if(bankService == null){
                    bankService = new BankServiceBuilder().build();
                }
            }
        }
        return bankService;
    }
}
