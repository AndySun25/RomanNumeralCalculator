import calculator.InvalidRomanNumeralException;
import calculator.Numeral;
import calculator.NumeralUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input;

        System.out.println("Calculator started. \n");
        System.out.println("Please enter the values you would like to have added separated by whitespaces.");
        System.out.println("Type \"exit\" to leave the calculator. \n");

        while (!(input = sc.nextLine()).equalsIgnoreCase("exit")) {
            parseString(input);
        }

        System.out.println("Exiting calculator.");
        System.exit(0);
    }

    public static void parseString(String input) {
        String[] split = input.split("\\s+");

        // Must have 2 or more numerals to add.
        if (split.length<2) {
            System.err.println("You must enter 2 or more numerals!");
            return;
        }

        List<Numeral> numerals = new ArrayList<>();
        List<String> invalid_numerals = new ArrayList<>();

        for (String entry: split) {
            try {
                numerals.add(new Numeral(entry));
            } catch (InvalidRomanNumeralException e){
                invalid_numerals.add(entry);
            }
        }

        if (invalid_numerals.size()>0) {
            System.err.print("Invalid numerals: ");
            for (String invalid_numeral: invalid_numerals) {
                System.err.print(invalid_numeral + " ");
            }
            System.err.print("\n");
            return;
        }

        try {
            Numeral result = NumeralUtils.add(numerals.toArray(new Numeral[numerals.size()]));
            System.out.println("Result: " + result.toString());
        } catch (InvalidRomanNumeralException e) {
            System.err.println("An error occurred in addition!");
        }
    }
}
