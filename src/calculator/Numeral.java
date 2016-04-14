package calculator;


public class Numeral {
    private String value;

    public Numeral(String value) throws InvalidRomanNumeralException{
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    private void setValue(String value) throws InvalidRomanNumeralException{
        if (NumeralUtils.isValidRomanNumeral(value)) {
            this.value = value;
        } else {
            throw new InvalidRomanNumeralException(value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
