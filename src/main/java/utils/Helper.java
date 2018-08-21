package utils;

import java.math.BigDecimal;

/**
 * Created by JK on 8/19/2018.
 * This is for personal use. All Rights Reserved.
 */
public class Helper {

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidNumberForRate(BigDecimal b) {
        return b.toPlainString().matches("\\d+(\\.\\d{1,1})?");
    }

    public static boolean isAmountAHundredDivided(int num) {
        return num % 100 == 0;
    }
}
