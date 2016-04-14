package tests;

import calculator.InvalidRomanNumeralException;
import calculator.Numeral;
import calculator.NumeralUtils;
import junit.framework.TestCase;

import java.util.HashMap;


public class NumeralUtilsTest extends TestCase{

    public void testValidateNumeralString() {
        String[] invalid_numerals = {"IVI", "VIIIII", "XCXC", "CCCC", "IIX"};
        String[] valid_numerals = {"VIII", "CCC", "XC", "XVIII", "CIV"};

        for (String entry: invalid_numerals) {
            assertFalse("Failed to invalidate string " + entry, NumeralUtils.isValidRomanNumeral(entry));
        }

        for (String entry: valid_numerals) {
            assertTrue("Failed to validate string " + entry, NumeralUtils.isValidRomanNumeral(entry));
        }
    }

    public void testCompressStringToNumeral() {
        HashMap<String, String> comp_values = new HashMap<>();
        comp_values.put("IIII", "IV");
        comp_values.put("DCCCCXXXXVIIII", "CMXLIX");
        comp_values.put("CCCCLXXXXVIIII", "CDXCIX");
        for (String key : comp_values.keySet()) {
            try {
                assertEquals(NumeralUtils.compressToNumeral(key).getValue(), comp_values.get(key));
            } catch (InvalidRomanNumeralException e) {
                fail("Unable to compress String " + key);
            }
        }
    }

    public void testDecompressNumeral() {
        HashMap<String, String> decomp_values = new HashMap<>();
        decomp_values.put("IV", "IIII");
        decomp_values.put("CMXLIX", "DCCCCXXXXVIIII");
        decomp_values.put("CDXCIX", "CCCCLXXXXVIIII");
        for (String key : decomp_values.keySet()) {
            try {
                Numeral num = new Numeral(key);
                assertEquals(NumeralUtils.decompressToString(num), decomp_values.get(key));
            } catch (InvalidRomanNumeralException e) {
                fail("Unable to decompress String " + key);
            }
        }
    }

    public void testAddNumeral() {
        Numeral num1, num2, num3, num4, result;
        try {
            num1 = new Numeral("IV");
            num2 = new Numeral("VII");
            num3 = new Numeral("XLIX");
            num4 = new Numeral("CXCIX");
            result = new Numeral("CCLIX");
            assertEquals(NumeralUtils.add(num1, num2, num3, num4).getValue(), result.getValue());
        } catch (InvalidRomanNumeralException e) {
            fail("Failed to add numerals with error " + e.getMessage());
        }
    }
}
