package calculator;

import java.util.HashMap;
import java.util.regex.Pattern;

public class NumeralUtils {
    // Regex for validating numeral strings.
    private static final Pattern pattern = Pattern.compile("^(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");

    // Reference string for determining char comparisons.
    private static final String order = "IVXLCDM";

    // Decompression map for decompressing valid numeral strings.
    private static HashMap<String, String> decomp_map;

    // Compression map for compressing decompressed numeral strings.
    private static HashMap<String, String> comp_map;

    // Separated compression sets keys due to ordering requirements. Full carries must be resolved before abbreviations.
    private static final String[] comp_set1 = {"IIIII", "VV", "XXXXX", "LL", "CCCCC", "DD"};

    // Ordering is important for the second set as values such as 9 and 90 must be resolved before 4 and 40 respectively.
    private static final String[] comp_set2 = {"VIIII", "IIII", "LXXXX", "XXXX", "DCCCC", "CCCC"};

    static {
        decomp_map = new HashMap<>();
        decomp_map.put("IV", "IIII");
        decomp_map.put("IX", "VIIII");
        decomp_map.put("XL", "XXXX");
        decomp_map.put("XC", "LXXXX");
        decomp_map.put("CD", "CCCC");
        decomp_map.put("CM", "DCCCC");

        comp_map = new HashMap<>();
        comp_map.put("IIII", "IV");
        comp_map.put("IIIII", "V");
        comp_map.put("VIIII", "IX");
        comp_map.put("VV", "X");
        comp_map.put("XXXX", "XL");
        comp_map.put("XXXXX", "L");
        comp_map.put("LXXXX", "XC");
        comp_map.put("LL", "C");
        comp_map.put("CCCC", "CD");
        comp_map.put("CCCCC", "D");
        comp_map.put("DCCCC", "CM");
        comp_map.put("DD", "M");
    }

    /**
     * Adds all given numerals.
     */
    public static Numeral add(Numeral... numerals) throws InvalidRomanNumeralException {
        Numeral res = numerals[0];
        for (int i=1;i<numerals.length;i++) {
            res = add(res, numerals[i]);
        }
        return res;
    }

    /**
     * Adds two roman numerals by decompressing both, appending the strings, sorting the resulting appended string
     * and finally compressing the sorted string.
     * e.g. IV (4) + XLVII (47) -> IIII + XXXXVII -> IIIIXXXXVII -> XXXXVIIIIII -> XXXXVVI -> XXXXXII -> LI (51)
     */
    private static Numeral add(Numeral numeral1, Numeral numeral2) throws InvalidRomanNumeralException {
        String appended = decompressToString(numeral1) + decompressToString(numeral2);
        String sortedNumeralString = sortNumeralString(appended);
        return compressToNumeral(sortedNumeralString);
    }

    /**
     * Compresses the string by compacting numerals into abbreviated forms.
     * Begin by looping through the first set of compression keys and resolving full carries.
     * e.g. IIIII -> V (5), LL -> C (100)
     * Finish by looping through the second set of compression keys and compacting abbreviations.
     * e.g. IIII -> IV (4), CCCC -> CD (400)
     */
    public static Numeral compressToNumeral(String value) throws InvalidRomanNumeralException{
        for (String key: comp_set1) {
            while (value.contains(key)) {
                value = value.replaceFirst(key, comp_map.get(key));
            }
        }

        for (String key: comp_set2) {
            while (value.contains(key)) {
                value = value.replaceFirst(key, comp_map.get(key));
            }
        }

        return new Numeral(value);
    }

    /**
     * Decompresses the numeral string by replacing compacted numerals with full length numerals.
     * e.g. IV -> IIII (4), XC -> LXXXX (90)
     */
    public static String decompressToString(Numeral numeral) {
        String value = numeral.getValue();
        StringBuilder res = new StringBuilder();

        // Loop through entire numeral string until length-1 in case string ends on an abbreviation .e.g IV.
        for (int i=0;i<value.length()-1;i++) {

            // If current char is greater than following char, append current char and the following if at string end.
            if (isGreaterThan(value.charAt(i), value.charAt(i+1)) || value.charAt(i)==value.charAt(i+1)) {
                res.append(value.charAt(i));
                if (i==value.length()-2) {
                    res.append(value.charAt(i+1));
                }
            } else { // Otherwise use decompression map to find replacement string and append.
                String key = value.substring(i, i+2);
                res.append(decomp_map.get(key));
                i++;
            }
        }
        return res.toString();
    }


    /**
     * Sorts a numeral tring using insertion sortNumeralString.
     */
    public static String sortNumeralString(String input) {
        char[] chars = input.toCharArray();

        for (int i=1;i<chars.length;i++) {
            char currentChar = chars[i];
            int j = i - 1;

            while (j >= 0 && isSmallerThan(chars[j], currentChar)) {
                chars[j+1] = chars[j];
                j--;
                chars[j+1] = currentChar;
            }
        }

        return new String(chars);
    }

    /**
     * Compares 2 numerals characters, returns true if the first is larger than second, else false.
     */
    public static boolean isGreaterThan(char input1, char input2) {
        return order.indexOf(input1) > order.indexOf(input2);
    }

    /**
     * Compares 2 numerals characters, returns true if the first is smaller than second, else false.
     */
    public static boolean isSmallerThan(char input1, char input2) {
        return order.indexOf(input1) < order.indexOf(input2);
    }

    /**
     * Checks that string is a valid numeral using regex pattern.
     */
    public static boolean isValidRomanNumeral(String input){
        return pattern.matcher(input).matches();
    }
}
