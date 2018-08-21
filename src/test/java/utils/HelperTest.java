package utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by JK on 8/19/2018.
 * This is for personal use. All Rights Reserved.
 */
public class HelperTest {


@Test
public void isIntegerTest() {
    Assert.assertFalse(Helper.isInteger(null));
    Assert.assertFalse(Helper.isInteger("45s"));
    Assert.assertTrue(Helper.isInteger("450"));
    Assert.assertFalse(Helper.isInteger("450.0"));
}

@Test
public void isValidNumberForRateTest(){
    Assert.assertFalse(Helper.isValidNumberForRate(new BigDecimal("0.00")));
    Assert.assertTrue(Helper.isValidNumberForRate(new BigDecimal("7.0")));
    Assert.assertTrue(Helper.isValidNumberForRate(new BigDecimal("7.5")));
    Assert.assertTrue(Helper.isValidNumberForRate(new BigDecimal("10")));
}

@Test
public void isAmountAHundredDividedTest() {
    Assert.assertTrue(Helper.isAmountAHundredDivided(1000));
    Assert.assertFalse(Helper.isAmountAHundredDivided(1050));
    Assert.assertTrue(Helper.isAmountAHundredDivided(14000));
    Assert.assertFalse(Helper.isAmountAHundredDivided(3310));
}

}
