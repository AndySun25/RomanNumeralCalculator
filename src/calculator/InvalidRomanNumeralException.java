package calculator;

public class InvalidRomanNumeralException extends Exception{
    public InvalidRomanNumeralException(String value) {
        super("Invalid numeral value \"" + value + "\".");
    }
}
