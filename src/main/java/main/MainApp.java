package main;

import utils.Helper;

import java.util.Scanner;

/**
 * Created by JK on 8/18/2018.
 * This is for personal use. All Rights Reserved.
 */
public class MainApp {
    static Calculator calculator;
    static boolean exit = false;
    public static void main(String[] args) {
        System.out.println("--------Arguments Info---------");
        System.out.println("File: " + args[0]);
        System.out.println("Requested Amount: " + args[1]);
        System.out.println("-------------------------------");

        mainDialog(args[0], args[1]);
    }

    public static void mainDialog(String csvFile, String amount) {
        if (Helper.isInteger(amount) && Integer.parseInt(amount) <= 15000 && Integer.parseInt(amount) >= 1000
                && Helper.isAmountAHundredDivided(Integer.parseInt(amount))) {
            calculator = new Calculator(csvFile, Integer.parseInt(amount));
            calculator.init();
            if (calculator.getFailResponse().isEmpty()) { //successful parsing
                calculator.calculate();
                if (calculator.getFailResponse().isEmpty()) {
                    printBorrowerDetails(calculator);
                    Scanner scanner = new Scanner(System.in);
                    while (!exit) {
                        System.out.println("Give another amount to proceed or press exit to quit");
                        String givenAmount = scanner.next();
                        if (Helper.isInteger(givenAmount) && Integer.parseInt(givenAmount) <= 15000 && Integer.parseInt(givenAmount) >= 1000
                                && Helper.isAmountAHundredDivided(Integer.parseInt(givenAmount))) {
                            calculator.setRequestedAmount(Integer.parseInt(givenAmount));
                            calculator.calculate();
                            if (calculator.getFailResponse().isEmpty()) {
                                printBorrowerDetails(calculator);
                            } else {
                                System.out.println(calculator.getFailResponse());
                            }
                        } else {
                            if (givenAmount.equals("exit")) {
                                exit = true;
                            } else {
                                System.out.println("The amount you gave is not valid.");
                            }
                        }
                    }
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

    private static void printBorrowerDetails(Calculator calculator) {
        System.out.println("Requested amount: £" + calculator.getRequestedAmount());
        System.out.println("Rate: " + calculator.getGivenRate() + "%");
        System.out.println("Monthly Repayment: £" + calculator.getMonthlyRepayment());
        System.out.println("Total Repayment: £" + calculator.getTotalRepayment());
    }

}
