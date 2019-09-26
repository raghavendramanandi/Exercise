import exceptions.ApplicationException;
import org.junit.Test;
import service.BankServiceImpl;

import static org.junit.Assert.*;

public class BankSingletonTest {

    @Test
    public void shouldAlwaysReturnTheSameInstance() throws Exception, ApplicationException {
        BankServiceImpl fetch1 = BankSingleton.getInstance();
        BankServiceImpl fetch2 = BankSingleton.getInstance();

        assertTrue(fetch1 == fetch2);
        assertTrue(fetch1 instanceof BankServiceImpl ? true : false);
    }

}