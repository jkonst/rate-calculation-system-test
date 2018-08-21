# Short Description
A rate calculation system allowing prospective borrowers to obtain a quote from our pool of lenders for 36 month loans. This system will take the form of a command-line application.

# Algorithm
This implementation in order to calculate monthly and total repayments uses only one rate instead of a mix of rates. So, in case the requested amount by the borrower can be covered by the available offers of lenders of one rate (the lowest possible), this rate is used to calculate the repayments. 

e.g. requested amount: 1000

7.0%: Lender1 -> 500, Lender2 -> 100 | 8.0%: Lender1: 1000 | 10.0%: Lender1: 1000

The implementation will select 8.0% as rate.

The implementation could be changed to use a mix of rates if this is required.

# Build
- mvn clean install (tests run) -> rate-calculation-system-test-1.0-SNAPSHOT.jar (target)

# Run
> cd target

> java -jar rate-calculation-system-test-1.0-SNAPSHOT.jar [path of csv file] [amount]
