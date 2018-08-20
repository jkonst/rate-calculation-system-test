package main;

import model.Lender;
import model.RateCompare;
import utils.CSVParser;
import utils.Helper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class Calculator {
    private List<Lender> allLenders;
    private Map<BigDecimal, List<Lender>> availableByRateMap;
    private String csvFile;
    private int requestedAmount;
    private boolean isRequestedAmountCovered;
    private String failResponse;

    private BigDecimal givenRate;
    private BigDecimal monthlyRepayment;
    private BigDecimal totalRepayment;

    private final static MathContext mc1 = new MathContext(1, RoundingMode.HALF_EVEN);
    private final static MathContext mc2 = new MathContext(2, RoundingMode.HALF_EVEN);
    private final static MathContext mc5 = new MathContext(5, RoundingMode.HALF_EVEN);
    private final static int MONTHLY_PERIODS = 36;

    public Calculator(String file, int amount) {
        setCsvFile(file);
        requestedAmount = amount;
        setRequestedAmountCovered(false);
        failResponse = "";
    }

    /**
     * 1) csv parsing
     * 2) setupLenders() - allLenders instantiation and sorting by rate
     * 3) normalizeLenders() - availableByRateMap construction
     */
    public void init() {
        CSVParser parser = new CSVParser(this.getCsvFile());
        parser.parse();
        if (parser.isParsingSuccessful() && parser.getParsedLines().size() > 1) {
            setupLenders(parser.getParsedLines());
            normalizeLenders();
            this.failResponse = "";
        } else {
            this.failResponse = "Something went wrong with parsing " + getCsvFile();
        }
    }

    /**
     * Instantiates allLenders given the parsedLines from CSVParser
     */
    public void setupLenders(List<String[]> parsedLines) {
        allLenders = new ArrayList<>();
        int count = 0;
        for (String[] columns : parsedLines) {
            Lender lender = new Lender();
            lender.setName(columns[0]);
            count++;
            if (Helper.isInteger(columns[2]) && Helper.isValidNumberForRate(getNormalizedRate(columns[1]))) {
                lender.setId(count);
                lender.setAvailable(Integer.parseInt(columns[2]));
                lender.setRate(getNormalizedRate(columns[1]));
                allLenders.add(lender);
            } else {
                System.out.println("Lender " + lender.getName() + " is not a valid lender.");
            }
        }
        RateCompare rateCompare = new RateCompare();
        Collections.sort(allLenders, rateCompare);
    }

    /**
     * Constructs a map with key, a rate from csvFile, and value, the lenders' available amounts (i.e. a list)
     */
    public void normalizeLenders() {
        this.availableByRateMap = new LinkedHashMap<>();

        for (Lender lender : allLenders) {
            List<Lender> availableList;
            if (availableByRateMap.containsKey(lender.getRate())) {
                availableList = availableByRateMap.get(lender.getRate());
            } else {
                availableList = new ArrayList<>();
            }
            availableList.add(lender);
            availableByRateMap.put(lender.getRate(), availableList);
        }

    }

    /**
     * Rounding is Half Even e.g. 1.5 -> 2.0, 1.4 -> 1.0
     *
     * @param str
     * @return rate with 1 decimal point
     */
    public BigDecimal getNormalizedRate(String str) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(str, mc1).multiply(new BigDecimal("100"));
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_EVEN);
        } catch (Exception e) {
            e.printStackTrace();
            bd = new BigDecimal("0.000");//invalid
        }
        return bd;
    }

    /**
     * 1) Check if the requested amount can be covered by lenders available
     * 2a) If so, update lenders with new available
     * 2b) Otherwise, inform borrower that it is not possible to provide a quote
     * 3) Calculate monthly and total payments
     */
    public void calculate() {
        checkAvailableForRequestedAmount();
        if (isRequestedAmountCovered) {
            calculatePMTFormula();
            failResponse = "";
        } else {
            failResponse = "It is not possible to provide a quote at that time.";
        }
    }

    /**
     * Check if the lenders total available are enough to cover the requested amount and
     * update lenders and set given rate
     */
    public void checkAvailableForRequestedAmount() {
        for (Map.Entry<BigDecimal, List<Lender>> entry : availableByRateMap.entrySet()) {
            List<Lender> lenders = entry.getValue();
            if (isLendersAvailableAdequate(lenders)) {
                isRequestedAmountCovered = true;
                updateLendersOnTheList(lenders);
                this.givenRate = entry.getKey();
                break;
            }
        }
    }

    /**
     * Update available of lenders
     *
     * @param lenders
     */
    public void updateLendersOnTheList(List<Lender> lenders) {
        int amount = requestedAmount;
        for (Lender lender : lenders) {
            if (amount > 0) {
                if (amount > lender.getAvailable()) {
                    amount -= lender.getAvailable();
                    lender.setAvailable(0);
                } else {
                    lender.setAvailable(lender.getAvailable() - amount);
                    amount = 0;
                }
            }
        }
    }

    /**
     * @param lenders
     * @return true if the sum of lenders available >= requestedAmount
     */
    public boolean isLendersAvailableAdequate(List<Lender> lenders) {
        int totalAvailable = 0;
        for (Lender lender : lenders) {
            totalAvailable += lender.getAvailable();
        }
        return totalAvailable >= requestedAmount;
    }

    /**
     * Using the equivalent of Excel PMT function
     * P = (Pv*R) / [1 - (1 + R)^(-n)
     * P: Monthly Payment
     * Pv: Requested amount
     * R: rate / 12 months
     * n: total number of interest periods, i.e. 36
     */
    public void calculatePMTFormula() {
        BigDecimal monthlyRate = givenRate.divide(new BigDecimal("1200"), 5, RoundingMode.HALF_EVEN);
        BigDecimal numerator = monthlyRate.multiply(new BigDecimal(String.valueOf(requestedAmount)));
        BigDecimal monthlyRateModified = new BigDecimal("1").add(monthlyRate);
        monthlyRateModified = new BigDecimal("1").divide(monthlyRateModified, 5, RoundingMode.HALF_EVEN);
        BigDecimal power = monthlyRateModified.pow(36, mc5);
        BigDecimal denominator = new BigDecimal("1").subtract(power);

        this.monthlyRepayment = numerator.divide(denominator, 2, RoundingMode.HALF_EVEN);
        System.out.println(monthlyRepayment);
        this.totalRepayment = new BigDecimal("36").multiply(monthlyRepayment);
        System.out.println(totalRepayment);
    }

    //Getters - Setters

    public List<Lender> getAllLenders() {
        return allLenders;
    }

    public void setAllLenders(List<Lender> allLenders) {
        this.allLenders = allLenders;
    }

    public Map<BigDecimal, List<Lender>> getAvailableByRateMap() {
        return availableByRateMap;
    }

    public void setAvailableByRateMap(Map<BigDecimal, List<Lender>> availableByRateMap) {
        this.availableByRateMap = availableByRateMap;
    }

    public BigDecimal getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(BigDecimal monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount = requestedAmount;
    }


    public boolean isRequestedAmountCovered() {
        return isRequestedAmountCovered;
    }

    public void setRequestedAmountCovered(boolean requestedAmountCovered) {
        isRequestedAmountCovered = requestedAmountCovered;
    }

    public BigDecimal getGivenRate() {
        return givenRate;
    }

    public void setGivenRate(BigDecimal givenRate) {
        this.givenRate = givenRate;
    }

    public String getFailResponse() {
        return failResponse;
    }

    public void setFailResponse(String failResponse) {
        this.failResponse = failResponse;
    }

    public String getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }
}
