package main;

import model.Lender;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 8/19/2018.
 * This is for personal use. All Rights Reserved.
 */
public class CalculatorTest {

    private static Calculator calculator;
    private static List<String[]> parsedLines = new ArrayList<>();


    @BeforeClass
    public static void setUp() {

        String parsedLine1[] = new String[]{"John", "0.066", "420"};
        String parsedLine2[] = new String[]{"Bob", "0.084", "500"};
        String parsedLine3[] = new String[]{"Alice", "0.075", "1200"};

        parsedLines.add(parsedLine1);
        parsedLines.add(parsedLine2);
        parsedLines.add(parsedLine3);
    }

    @Test
    public void initSuccessTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        calculator.setCsvFile(absolutePath);
        calculator.init();
    }

    @Test
    public void initFailTest() {
        String resourcePath = "src/main/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        calculator.setCsvFile(absolutePath);
        calculator.init();
    }

    @Test
    public void setupLendersTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        calculator.init();
        calculator.setupLenders(parsedLines);
        Assert.assertEquals(3, calculator.getAllLenders().size());
        Assert.assertEquals("John", calculator.getAllLenders().get(0).getName());
    }

    @Test
    public void normalizeLendersTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        calculator.init();
        Assert.assertTrue(calculator.getAvailableByRateMap().get(new BigDecimal("7.0")).size() == 4);
        Assert.assertEquals(calculator.getAvailableByRateMap().get(new BigDecimal("7.0")).get(0).getName(), "Jane");
        Assert.assertEquals(calculator.getAvailableByRateMap().get(new BigDecimal("10.0")).get(0).getName(), "Mary");
    }

    @Test
    public void getNormalizedRateTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        BigDecimal bd = calculator.getNormalizedRate("0.453S");
        Assert.assertEquals(new BigDecimal("0.000"),bd);
        BigDecimal bd2 = calculator.getNormalizedRate("0.075");
        Assert.assertEquals(new BigDecimal("8.0"), bd2);
    }

    @Test
    public void calculateSuccessTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1200);
        calculator.init();
        calculator.calculate();
        Assert.assertTrue(calculator.isRequestedAmountCovered());
        Assert.assertTrue(calculator.getGivenRate().equals(new BigDecimal("7.0")));
        Assert.assertEquals(calculator.getFailResponse(), "");
    }

    @Test
    public void calculateFailTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 5000);
        calculator.init();
        calculator.calculate();
        Assert.assertFalse(calculator.isRequestedAmountCovered());
        Assert.assertEquals(calculator.getFailResponse(), "It is not possible to provide a quote at that time.");
    }

    @Test
    public void calculatePMTFormulaTest() {
        String resourcePath = "src/test/resources/MarketData.csv";
        File file = new File(resourcePath);
        String absolutePath = file.getAbsolutePath();
        calculator = new Calculator(absolutePath, 1000);
        calculator.init();
        calculator.calculate();
        Assert.assertEquals(calculator.getMonthlyRepayment(), new BigDecimal("30.86"));
        Assert.assertEquals(calculator.getTotalRepayment(), new BigDecimal("1110.96"));
    }
}
