import exceptions.ApplicationException;
import org.junit.Test;
import service.BankService;

import static org.junit.Assert.assertTrue;

public class BankServiceBuilderTest {
    @Test
    public void shouldBuildObjectOfTypeBankServiceIfValid() throws Exception, ApplicationException {
        assertTrue(new BankServiceBuilder().build("application.properties") instanceof BankService ? true  : false);
    }


    @Test(expected = ApplicationException.class)
    public void shouldThrowExceptionIfPropertyFileIsNotAvailable() throws Exception, ApplicationException {
        assertTrue(new BankServiceBuilder().build("application1.properties") instanceof BankService ? true  : false);
    }

}