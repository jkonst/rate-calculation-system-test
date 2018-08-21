package main;

import utils.Helper;

/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class MainApp {
    public static void main(String[] args) {
        System.out.println("--------Arguments Info---------");
        System.out.println("File: " + args[0]);
        System.out.println("Requested Amount: " + args[1]);
        System.out.println("-------------------------------");
        if (Helper.isInteger(args[1])) {
            Calculator calculator = new Calculator(args[0], Integer.parseInt(args[1]));
            calculator.init();
            if (calculator.getFailResponse().isEmpty()) { //successful parsing
                calculator.calculate();
                if (calculator.getFailResponse().isEmpty()) {
                    System.out.println("Requested amount: " + calculator.getRequestedAmount());
                    System.out.println("Rate: " + calculator.getGivenRate());
                    System.out.println("Monthly Repayment: " + calculator.getMonthlyRepayment());
                    System.out.println("Total Repayment: " + calculator.getTotalRepayment());
                }  else {
                    System.out.println(calculator.getFailResponse());
                }
            } else {
                System.out.println(calculator.getFailResponse());
            }
        } else {
            System.out.println("The amount you gave is not valid.");
        }
    }

}
