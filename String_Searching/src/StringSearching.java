import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Your implementations of various string searching algorithms.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class StringSearching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm that relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the failure table before implementing this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for pattern
     * @return list containing the starting index for each match found
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Cannot accept null or zero-length pattern");
        }
        if (text == null) {
            throw new IllegalArgumentException("Cannot accept null text.");
        }
        List<Integer> matchList = new ArrayList<Integer>();
        if (pattern.length() > text.length()) {
            return matchList;
        }
        int p = 0;
        int t = 0;
        int pLength = pattern.length();
        int tLength = text.length();
        int[] failureTable = buildFailureTable(pattern);

        while (t < tLength && ((t + pLength - p - 1) < tLength)) {
            if (text.charAt(t) == pattern.charAt(p)) {
                if (p == pLength - 1) {
                    matchList.add(t - pLength + 1);
                    t++;
                    p = failureTable[pLength - 1];
                } else {
                    t++;
                    p++;
                }
            } else {
                if (p == 0) {
                    t++;
                } else {
                    p = failureTable[p - 1];
                }
            }
        }
        return matchList;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input text.
     *
     * Note that a given index i will be the largest prefix of the pattern
     * indices [0..i] that is also a suffix of the pattern indices [1..i].
     * This means that index 0 of the returned table will always be equal to 0
     *
     * Ex. ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @throws IllegalArgumentException if the pattern is null
     * @param pattern a {@code CharSequence} you're building a failure table for
     * @return integer array holding your failure table
     */
    public static int[] buildFailureTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        if (pattern.length() == 0) {
            int[] table = new int[0];
            return table;
        }
        int i = 0;
        int j = 1;
        int[] table = new int[pattern.length()];
        table[0] = 0;
        while (j < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                table[j] = i + 1;
                i++;
                j++;
            } else {
                if (i == 0) {
                    table[j] = i;
                    j++;
                } else {
                    i = table[i - 1];
                }
            }
        }
        return table;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the last occurrence table before implementing this
     * method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for the pattern
     * @return list containing the starting index for each match found
     */
    public static List<Integer> boyerMoore(CharSequence pattern,
            CharSequence text) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Cannot accept null or zero-length pattern.");
        }
        if (text == null) {
            throw new IllegalArgumentException("Cannot accept null text.");
        }
        List<Integer> matchList = new ArrayList<Integer>();
        if (pattern.length() > text.length()) {
            return matchList;
        }
        Map<Character, Integer> lastMap = buildLastTable(pattern);
        int pLength = pattern.length();
        int tLength = text.length();
        int p = pLength - 1;
        int t = pLength - 1;

        while (t < tLength) {
            char charAtx = text.charAt(t);
            if (charAtx == pattern.charAt(p)) {
                if (p == 0) {
                    matchList.add(t);
                    t++;
                    p = pLength - 1;
                } else {
                    t--;
                    p--;
                }
            } else {
                if (!lastMap.containsKey(charAtx)) {
                    t = t + pLength;
                    p = pLength - 1;
                } else {
                    int shiftIndex = lastMap.get(charAtx);
                    if (t + pLength - shiftIndex - 1 - p <= t) {
                        t = t + pLength - 1;
                        p = pLength - 1;
                    } else {
                        t = t + pLength - shiftIndex - 1;
                        p = pLength - 1;
                    }
                }
            }
        }
        return matchList;
    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @throws IllegalArgumentException if the pattern is null
     * @param pattern a {@code CharSequence} you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     *         to their last occurrence in the pattern
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Cannot accept null pattern.");
        }
        Map<Character, Integer> lastMap = new HashMap<Character, Integer>();
        if (pattern.length() == 0) {
            return lastMap;
        }
        for (int i = 0; i < pattern.length(); i++) {
            lastMap.put(pattern.charAt(i), i);
        }
        return lastMap;
    }

    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 433;

    /**
     * Runs Rabin-Karp algorithm. Generate the pattern hash, and compare it with
     * the hash from a substring of text that's the same length as the pattern.
     * If the two hashes match, compare their individual characters, else update
     * the text hash and continue.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text is null
     * @param pattern a string you're searching for in a body of text
     * @param text the body of text where you search for pattern
     * @return list containing the starting index for each match found
     */
    public static List<Integer> rabinKarp(CharSequence pattern,
            CharSequence text) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException(
                    "Cannot accept null or zero-length pattern.");
        }
        if (text == null) {
            throw new IllegalArgumentException("Cannot accept null text.");
        }
        List<Integer> matchList = new ArrayList<Integer>();
        if (pattern.length() > text.length()) {
            return matchList;
        }
        int patternHash = generateHash(pattern, pattern.length());
        int textHash = generateHash(text, pattern.length());
        int i = 0;
        while (i + pattern.length() <= text.length()) {
            if (patternHash == textHash) {
                int j = 0;
                while (j < pattern.length()
                        && pattern.charAt(j) == text.charAt(j + i)) {
                    if (j == pattern.length() - 1) {
                        matchList.add(i);
                        j++;
                    } else {
                        j++;
                    }
                }
            }
            if (pattern.length() + i < text.length()) {
                textHash = updateHash(textHash, pattern.length(),
                        text.charAt(i), text.charAt(i + pattern.length()));
            }
            i++;
        }
        return matchList;
    }

    /**
     * Hash function used for Rabin-Karp. The formula for hashing a string is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i), where c is the integer
     * value of the current character, and i is the index of the character
     *
     * For example: Hashing "bunn" as a substring of "bunny" with base 433 hash
     * = b * 433 ^ 3 + u * 433 ^ 2 + n * 433 ^ 1 + n * 433 ^ 0 = 98 * 433 ^ 3 +
     * 117 * 433 ^ 2 + 110 * 433 ^ 1 + 110 * 433 ^ 0 = 7977892179
     *
     * However, note that that will roll over to -612042413, because the largest
     * number that can be represented by an int is 2147483647.
     *
     * Do NOT use {@code Math.pow()} in this method.
     *
     * @throws IllegalArgumentException if current is null
     * @throws IllegalArgumentException if length is negative or greater
     *     than the length of current
     * @param current substring you are generating hash function for
     * @param length the length of the string you want to generate the hash for,
     * starting from index 0. For example, if length is 4 but current's length
     * is 6, then you include indices 0-3 in your hash (and pretend the actual
     * length is 4)
     * @return hash of the substring
     */
    public static int generateHash(CharSequence current, int length) {
        if (current == null) {
            throw new IllegalArgumentException("Cannot accept null substring.");
        }
        if (length < 0 || length > current.length()) {
            throw new IllegalArgumentException(
                    "Length cannot be negative or greater than substring.");
        }
        int hash = 0;
        for (int i = 0; i < length; i++) {
            hash += current.charAt(i) * pow(BASE, length - i - 1);
        }
        return hash;
    }

    /**
     * Updates a hash in constant time to avoid constantly recalculating
     * entire hash. To update the hash:
     *
     *  remove the oldChar times BASE raised to the length - 1, multiply by
     *  BASE, and add the newChar.
     *
     * For example: Shifting from "bunn" to "unny" in "bunny" with base 433
     * hash("unny") = (hash("bunn") - b * 433 ^ 3) * 433 + y * 433 ^ 0 =
     * (3764054547 - 98 * 433 ^ 3) * 433 + 121 * 433 ^ 0 = 9519051770
     *
     * However, the number will roll over to 929117178.
     *
     * The computation of BASE raised to length - 1 may require O(log n) time,
     * but the method should otherwise run in O(1).
     *
     * Do NOT use {@code Math.pow()} in this method. We have provided a pow()
     * method for you to use.
     *
     * @throws IllegalArgumentException if length is negative
     * @param oldHash hash generated by generateHash
     * @param length length of pattern/substring of text
     * @param oldChar character we want to remove from hashed substring
     * @param newChar character we want to add to hashed substring
     * @return updated hash of this substring
     */
    public static int updateHash(int oldHash, int length, char oldChar,
            char newChar) {
        if (length < 0) {
            throw new IllegalArgumentException(
                    "Cannot accept negative length.");
        }
        return BASE * (oldHash - oldChar * pow(BASE, length - 1)) + newChar;
    }

    /**
     * Calculate the result of a number raised to a power.
     *
     * DO NOT MODIFY THIS METHOD.
     *
     * @throws IllegalArgumentException if both {@code base} and {@code exp} are
     * 0
     * @throws IllegalArgumentException if {@code exp} is negative
     * @param base base of the number
     * @param exp power to raise the base to. Must be 0 or greater.
     * @return result of the base raised to that power
     */
    private static int pow(int base, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Exponent cannot be negative.");
        } else if (base == 0 && exp == 0) {
            throw new IllegalArgumentException(
                    "Both base and exponent cannot be 0.");
        } else if (exp == 0) {
            return 1;
        } else if (exp == 1) {
            return base;
        }
        int halfPow = pow(base, exp / 2);
        if (exp % 2 == 0) {
            return halfPow * halfPow;
        } else {
            return halfPow * pow(base, (exp / 2) + 1);
        }
    }
}
