package utils;

import java.text.DecimalFormat;

public class Util {
    public static double round(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(value));
    }
}
