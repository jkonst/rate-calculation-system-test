package main;

import model.Lender;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 8/19/2018.
 * This is for personal use. All Rights Reserved.
 */
public class CalculatorTest {
    private static String filePath;

    private static Calculator calculator;
    private static List<String[]> parsedLines = new ArrayList<>();
    private static List<Lender> allLenders = new ArrayList<>();

    @BeforeClass
    public static void setUp() {
        filePath = "/MarketData.csv";
        calculator = new Calculator(filePath, 1200);
        String parsedLine1[] = new String[]{"John", "0.066", "420"};
        String parsedLine2[] = new String[]{"Bob", "0.084", "500"};
        String parsedLine3[] = new String[]{"Alice", "0.075", "1200"};

        parsedLines.add(parsedLine1);
        parsedLines.add(parsedLine2);
        parsedLines.add(parsedLine3);

        Lender lender1 = new Lender();
        lender1.setId(1);
        lender1.setName("John");
        lender1.setRate(new BigDecimal("7.0"));
        lender1.setAvailable(1000);

        allLenders.add(lender1);

        Lender lender2 = new Lender();
        lender2.setId(2);
        lender2.setName("Alice");
        lender2.setRate(new BigDecimal("10.0"));
        lender2.setAvailable(500);

        allLenders.add(lender2);

        Lender lender3 = new Lender();
        lender3.setId(3);
        lender3.setName("Bob");
        lender3.setRate(new BigDecimal("7.0"));
        lender3.setAvailable(200);

        allLenders.add(lender3);

        Lender lender4 = new Lender();
        lender4.setId(4);
        lender4.setName("Mark");
        lender4.setRate(new BigDecimal("7.0"));
        lender4.setAvailable(800);
        allLenders.add(lender4);
    }

    @Test
    public void initSuccessTest() {
        calculator.init();
    }

    @Test
    public void initFailTest() {
        calculator.setCsvFile("/MarketDataCorrupted.csv");
        calculator.init();
    }

    @Test
    public void setupLendersTest() {
        calculator.setupLenders(parsedLines);
        Assert.assertEquals(3, calculator.getAllLenders().size());
        Assert.assertEquals("John", calculator.getAllLenders().get(0).getName());
    }

    @Test
    public void normalizeLendersTest() {
        calculator.setAllLenders(allLenders);
        calculator.normalizeLenders();
        Assert.assertTrue(calculator.getAvailableByRateMap().get(new BigDecimal("7.0")).size() == 3);
        Assert.assertNotEquals(calculator.getAvailableByRateMap().get(new BigDecimal("7.0")).get(0).getName(), "Alice");
        Assert.assertEquals(calculator.getAvailableByRateMap().get(new BigDecimal("10.0")).get(0).getName(), "Alice");
    }

    @Test
    public void getNormalizedRateTest() {
        BigDecimal bd = calculator.getNormalizedRate("0.453S");
        Assert.assertEquals(new BigDecimal("0.000"),bd);
        BigDecimal bd2 = calculator.getNormalizedRate("0.075");
        Assert.assertEquals(new BigDecimal("8.0"), bd2);
    }

    @Test
    public void calculateSuccessTest() {
        calculator.setAllLenders(allLenders);
        calculator.setRequestedAmount(1200);
        calculator.normalizeLenders();
        calculator.calculate();
        Assert.assertTrue(calculator.isRequestedAmountCovered());
        Assert.assertTrue(calculator.getGivenRate().equals(new BigDecimal("7.0")));
        Assert.assertEquals(calculator.getFailResponse(), "");
        List<Lender> lenders = calculator.getAvailableByRateMap().get(new BigDecimal("7.0"));
        int totalAvailable = 0;
        for (Lender lender : lenders) {
            totalAvailable += lender.getAvailable();
        }
        Assert.assertEquals(totalAvailable, 800);
    }

    @Test
    public void calculateFailTest() {
        calculator.setAllLenders(allLenders);
        calculator.setRequestedAmount(5000);
        calculator.normalizeLenders();
        calculator.calculate();
        Assert.assertFalse(calculator.isRequestedAmountCovered());
        Assert.assertEquals(calculator.getFailResponse(), "It is not possible to provide a quote at that time.");
    }

    @Test
    public void calculatePMTFormulaTest() {
        calculator.setAllLenders(allLenders);
        calculator.setRequestedAmount(1000);
        calculator.normalizeLenders();
        calculator.calculate();
    }
}
