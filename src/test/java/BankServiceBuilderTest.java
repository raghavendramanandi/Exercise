import exceptions.ApplicationException;
import org.junit.Test;
import service.BankServiceImpl;

import static org.junit.Assert.*;

public class BankServiceBuilderTest {
    @Test
    public void shouldBuildObjectOfTypeBankServiceIfValid() throws Exception, ApplicationException {
        assertTrue(new BankServiceBuilder().build("application.properties") instanceof BankServiceImpl ? true  : false);
    }


    @Test(expected = ApplicationException.class)
    public void shouldThrowExceptionIfPropertyFileIsNotAvailable() throws Exception, ApplicationException {
        assertTrue(new BankServiceBuilder().build("application1.properties") instanceof BankServiceImpl ? true  : false);
    }

}