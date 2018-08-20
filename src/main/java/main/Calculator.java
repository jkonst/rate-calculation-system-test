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


    private BigDecimal monthlyRepayment;
    private BigDecimal totalRepayment;

    private final static MathContext mc1 = new MathContext(1, RoundingMode.HALF_EVEN);
    private final static MathContext mc2 = new MathContext(2, RoundingMode.HALF_EVEN);

    public Calculator (String file) {
        csvFile = file;
    }

    /**
     * 1) csv parsing
     * 2) setupLenders() - allLenders instantiation and sorting by rate
     * 3) normalizeLenders() - availableByRateMap construction
     */
    public void init() {
        CSVParser parser = new CSVParser(this.csvFile);
        parser.parse();
        if (parser.isParsingSuccessful() && parser.getParsedLines().size() > 1) {
               setupLenders(parser.getParsedLines());
               normalizeLenders();
        } else {
            //TODO
        }
    }

    /**
     * Instantiates allLenders given the parsedLines from CSVParser
     */
    public void setupLenders(List<String[]> parsedLines) {
        allLenders = new ArrayList<>();
        for (String[] columns : parsedLines) {
            Lender lender = new Lender();
            lender.setName(columns[0]);
            if (Helper.isInteger(columns[2]) && Helper.isValidNumberForRate(getNormalizedRate(columns[1]))) {
                lender.setAvailable(Integer.parseInt(columns[2]));
                lender.setRate(getNormalizedRate(columns[1]));
                lender.setOnTheList(true);
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

        for(Lender lender : allLenders) {
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
     * @param str
     * @return rate with 1 decimal point
     */
    public BigDecimal getNormalizedRate (String str) {
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
}
