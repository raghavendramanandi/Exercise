package utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void getRoundedAmount() {
        double val = Util.round(10.98765);
        assertEquals(10.99, val, 0.0);
    }
}