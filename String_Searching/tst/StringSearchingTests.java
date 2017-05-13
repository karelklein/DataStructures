import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * JUnit test cases for the string searching assignment.
 *
 * @version 6
 * @author Timothy J. Aveni
 */

public class StringSearchingTests {

    @Rule
    public Timeout globalTimeout = new Timeout(20000);

    
    final PrintStream originalSystemOut = System.out;
    CleanOutputStream cleanOutputStream;
    
    private class CleanOutputStream extends OutputStream {

        private boolean clean = true;
        
        @Override
        public void write(int b) throws IOException {
            clean = false;
            originalSystemOut.write(b);
        }
        
        public boolean isClean() {
            return clean;
        }
    }
    
    private void assertException(String message, Class<? extends Exception> exceptionClass, Runnable code) {
        assertException(message, new Class[]{exceptionClass}, code);
    }
    
    private void assertException(String message, Class<? extends Exception>[] exceptionClasses, Runnable code) {
        try {
            code.run();
            Assert.fail(message);
        } catch (Exception e) {
            boolean foundException = false;
            for (Class<? extends Exception> exceptionClass: exceptionClasses) {
                if (exceptionClass.equals(e.getClass())) {
                    foundException = true;
                }
            }
            
            if (!foundException) {
                e.printStackTrace();
                Assert.fail(message);
            } else {
                  assertNotNull(
                          "Exception messages must not be empty",
                          e.getMessage());
                  assertNotEquals(
                          "Exception messages must not be empty",
                          "",
                          e.getMessage());
            }
        }
    }
    
    private class StrictString implements CharSequence {
        private String str;

        /**
         * Creates the SearchableString
         * @param s the string for the SearchableString to be created from
         */
        public StrictString(String s) {
            str = s;
        }

        @Override
        public char charAt(int i) {
            return str.charAt(i);
        }

        @Override
        public int length() {
            return str.length();
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            Assert.fail("You used the subSequence method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method "
                    + "subSequence.");
        }

        @Override
        public String toString() {
            Assert.fail("You used the toString method on a CharSequence. That's forbidden! "
                    + "Like, so forbidden, the javadoc says \"you WILL get a 0 on the entire assignment.\" "
                    + "If you can't figure out how to do it without toString(), you're probably better off just not including that method.");
            return "SearchableString containing: " + str + " (debug use only)";
        }

        @Override
        public boolean equals(Object o) {
            Assert.fail("You used the equals method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method equals.");
        }
    }
    
    private class CountingSequence implements CharSequence {
        private String str;
        private int[] counts;

        public CountingSequence(String s) {
            str = s;
            counts = new int[s.length()];
        }

        @Override
        public char charAt(int i) {
            counts[i]++;
            return str.charAt(i);
        }

        @Override
        public int length() {
            return str.length();
        }
        
        public int[] getCounts() {
            return counts;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            Assert.fail("You used the subSequence method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method "
                    + "subSequence.");
        }

        @Override
        public String toString() {
            Assert.fail("You used the toString method on a CharSequence. That's forbidden! "
                    + "Like, so forbidden, the javadoc says \"you WILL get a 0 on the entire assignment.\" "
                    + "If you can't figure out how to do it without toString(), you're probably better off just not including that method.");
            return "SearchableString containing: " + str + " (debug use only)";
        }

        @Override
        public boolean equals(Object o) {
            Assert.fail("You used the equals method on a CharSequence. That's forbidden!");
            throw new UnsupportedOperationException("Do not use method equals.");
        }
    }
    
    private List<Integer> naiveJavaSearch(CharSequence pattern, CharSequence text) {
        List<Integer> matches = new LinkedList<Integer>();
        int startIndex = 0;
        while (startIndex < text.length()) {
            int index = text.toString().substring(startIndex).indexOf(pattern.toString());
            if (index == -1) {
                return matches;
            }
            
            matches.add(index + startIndex);
            
            startIndex+= index + 1;
        }
        return matches;
    }
    
    @Before
    public void init() {
        cleanOutputStream = new CleanOutputStream();
        System.setOut(new PrintStream(cleanOutputStream));
    }
    
    @After
    public void checkOutput() {
        assertTrue(
                "You used print statements somewhere in your code. That's forbidden!", 
                cleanOutputStream.isClean());
    }
    
    @Test
    public void buildLastTableException() {
        assertException(
                "Building a last occurrence table from a null pattern should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.buildLastTable(null));
    }
    
    @Test
    public void buildEmptyLastTable() {
        Map<Character, Integer> emptyTable = StringSearching.buildLastTable(new StrictString(""));
        assertEquals(
                "A last occurrence table built from an empty pattern should be empty",
                0,
                emptyTable.size());
    }
    
    @Test
    public void buildSimpleLastTable() {
        Map<Character, Integer> actual = StringSearching.buildLastTable(new StrictString("abcd"));
        
        Map<Character, Integer> expected = new HashMap<Character, Integer>();
        expected.put('a', 0);
        expected.put('b', 1);
        expected.put('c', 2);
        expected.put('d', 3);
        
        assertEquals(
                "The last occurrence table method returned an incorrect map",
                expected,
                actual);
    }
    
    @Test
    public void buildDuplicateLastTable() {
        Map<Character, Integer> actual = StringSearching.buildLastTable(new StrictString("aaaa"));
        
        Map<Character, Integer> expected = new HashMap<Character, Integer>();
        expected.put('a', 3);
        
        assertEquals(
                "The last occurrence table method returned an incorrect map when the pattern contained duplicates",
                expected,
                actual);
    }
    
    @Test
    public void buildDuplicateLastTable2() {
        Map<Character, Integer> actual = StringSearching.buildLastTable(new StrictString("abab"));
        
        Map<Character, Integer> expected = new HashMap<Character, Integer>();
        expected.put('a', 2);
        expected.put('b', 3);
        
        assertEquals(
                "The last occurrence table method returned an incorrect map when the pattern contained duplicates",
                expected,
                actual);
    }
    
    @Test
    public void buildComplexLastTable() {
        Map<Character, Integer> actual = StringSearching.buildLastTable(new StrictString("abacdaebacb"));
        
        Map<Character, Integer> expected = new HashMap<Character, Integer>();
        expected.put('a', 8);
        expected.put('b', 10);
        expected.put('c', 9);
        expected.put('d', 4);
        expected.put('e', 6);
        
        assertEquals(
                "The last occurrence table method returned an incorrect map",
                expected,
                actual);
    }
    
    // TODO: shouldn't build last table if pattern is longer than text -> no charAt on pattern
    
    @Test
    public void boyerMooreExceptions() {
        assertException(
                "Boyer-Moore should throw an IllegalArgumentException if the pattern is null",
                IllegalArgumentException.class,
                () -> StringSearching.boyerMoore(null, "abcd"));
        
        assertException(
                "Boyer-Moore should throw an IllegalArgumentException if the pattern is empty",
                IllegalArgumentException.class,
                () -> StringSearching.boyerMoore("", "abcd"));
        
        assertException(
                "Boyer-Moore should throw an IllegalArgumentException if the text is null",
                IllegalArgumentException.class,
                () -> StringSearching.boyerMoore("abcd", null));
    }
    
    @Test
    public void boyerMooreWithLargerPattern() {
        CountingSequence pattern = new CountingSequence("abcd");
        CountingSequence text = new CountingSequence("ab");
        List<Integer> returned = StringSearching.boyerMoore(pattern, text);
        
        assertEquals(
                "Boyer-Moore should return an empty list of results when the pattern is longer than the text",
                0,
                returned.size());
        
        assertArrayEquals(
                "Boyer-Moore called charAt() on the text, even though the pattern was longer than the text",
                new int[]{0, 0},
                text.getCounts());
        
        assertArrayEquals(
                "Boyer-Moore called charAt() on the pattern, even though the pattern was longer than the text",
                new int[]{0, 0, 0, 0},
                pattern.getCounts());
    }
    
    @Test
    public void boyerMooreWithEmptyText() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        
        assertEquals(
                "Boyer-Moore should return an empty list of results when the text is empty",
                0,
                result.size());
        
        // shouldn't ever happen without an exception anyway
        assertArrayEquals(
                "Boyer-Moore called charAt() on the text, even though the text was empty",
                new int[]{},
                text.getCounts());
    }
    
    @Test
    public void simpleBoyerMoore() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void simpleBoyerMoore2() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcde");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void simpleBoyerMoore3() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcde");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(1);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        // TODO: consider cases where, after a shift, optimized code chooses not to compare
        //          against the character that causes it not to shift. In this case, the
        //          array would be [0, 1, 1, 1, 1].
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 1, 2, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreNoMatch() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreNoMatch2() {
        StrictString pattern = new StrictString("bcde");
        CountingSequence text = new CountingSequence("abcda");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 1},
                text.getCounts());
    }
        
    @Test
    public void boyerMooreMultiSkipWithMatch() {
        StrictString pattern = new StrictString("cdef");
        CountingSequence text = new CountingSequence("abcdef");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(2);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 1, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMultiSkipNoMatch() {
        StrictString pattern = new StrictString("cdef");
        CountingSequence text = new CountingSequence("abcdeg");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 0, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreFullSkipWithMatch() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abceabcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(4);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreFullSkipNoMatch() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abceabcf");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 0, 0, 0, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMultipleMatches() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcdabcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        expected.add(4);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1, 2, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMultipleMatches2() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcdeabcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        expected.add(5);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMultipleMatches3() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("fabcdeabcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(1);
        expected.add(6);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 1, 2, 1, 1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMultipleMatchesOverlap() {
        StrictString pattern = new StrictString("abca");
        CountingSequence text = new CountingSequence("abcabca");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        expected.add(3);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 2, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAvoidBackwardsSkip() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("adcdabcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(4);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 1, 1, 2, 1, 1, 1},
                text.getCounts());
    }
    
    
    @Test
    public void boyerMooreSkipForwardTwo() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("aaadcd");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 1, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest1() {
        StrictString pattern = new StrictString("bb");
        CountingSequence text = new CountingSequence("acabbcc");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(3);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 1, 2, 1, 1, 0},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest2() {
        StrictString pattern = new StrictString("daabd");
        CountingSequence text = new CountingSequence("ddbadbacdacbabdbdddaccddb");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 2, 1, 2, 2, 2, 2, 1, 1, 0, 1, 0, 0, 0},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest3() {
        StrictString pattern = new StrictString("bcbb");
        CountingSequence text = new CountingSequence("aaccdbcbb");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(5);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 0, 0, 1, 1, 2, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest4() {
        StrictString pattern = new StrictString("bb");
        CountingSequence text = new CountingSequence("acabbbc");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(3);
        expected.add(4);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 1, 2, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest5() {
        StrictString pattern = new StrictString("aba");
        CountingSequence text = new CountingSequence("acababc");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(2);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 2, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreAdditionalSkipTest6() {
        StrictString pattern = new StrictString("abab");
        CountingSequence text = new CountingSequence("acababc");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(2);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 2, 2, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMoveOneForwardOnMatch() {
        StrictString pattern = new StrictString("aaa");
        CountingSequence text = new CountingSequence("acaaaab");
        
        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(2);
        expected.add(3);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{0, 1, 2, 2, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void boyerMooreMoveOneForwardOnBackwardsCharacter() {
        StrictString pattern = new StrictString("aaa");
        CountingSequence text = new CountingSequence("baaacccc");

        List<Integer> result = StringSearching.boyerMoore(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(1);
        
        assertEquals(
                "A Boyer-Moore search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Boyer-Moore search had incorrect calls to charAt()",
                new int[]{1, 2, 2, 1, 1, 0, 0, 1},
                text.getCounts());
    }
    
    @Test
    public void kmpFailureTableException() {
        assertException(
                "Building a failure table from a null pattern should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.buildFailureTable(null));
    }
    
    @Test
    public void kmpFailureTableTrivialCases() {
        assertArrayEquals(
                "Building a zero-length faliure table failed (1)",
                new int[]{},
                StringSearching.buildFailureTable(""));
        
        assertArrayEquals(
                "Building a length-one faliure table failed (2)",
                new int[]{0},
                StringSearching.buildFailureTable("a"));
    }
    
    @Test
    public void kmpFailureTable() {        
        assertArrayEquals(
                "Building a simple length-two faliure table failed (3)",
                new int[]{0, 0},
                StringSearching.buildFailureTable("ab"));
        
        assertArrayEquals(
                "Building a simple length-two faliure table failed (4)",
                new int[]{0, 1},
                StringSearching.buildFailureTable("aa"));
        
        assertArrayEquals(
                "Building a faliure table failed (5)",
                new int[]{0, 0, 1, 2, 0},
                StringSearching.buildFailureTable("ababc"));
        
        assertArrayEquals(
                "Building a faliure table with overlap failed (6)",
                new int[]{0, 0, 1, 2, 3},
                StringSearching.buildFailureTable("ababa"));
        
        assertArrayEquals(
                "Building a faliure table failed (7)",
                new int[]{0, 0, 0, 1, 2, 1, 2},
                StringSearching.buildFailureTable("abcabab"));
        
        assertArrayEquals(
                "Building a faliure table failed (8)",
                new int[]{0, 0, 1, 0, 1, 2, 3, 1},
                StringSearching.buildFailureTable("bcbabcbb"));
        
        // stop judging me
        
        assertArrayEquals(
                "Building a faliure table failed (9)",
                new int[]{0, 1, 0, 1, 2, 0, 1, 2, 3},
                StringSearching.buildFailureTable("bbabbcbba"));
        
        assertArrayEquals(
                "Building a faliure table failed (10)",
                new int[]{0, 1, 0, 1, 2, 2},
                StringSearching.buildFailureTable("bbabbb"));
        
        assertArrayEquals(
                "Building a faliure table failed (11)",
                new int[]{0, 1, 2, 0, 1, 2, 3, 3},
                StringSearching.buildFailureTable("bbbabbbb"));
        
        // presently stuck in my head: https://www.youtube.com/watch?v=lUorBvADBOs
        
        assertArrayEquals(
                "Building a faliure table failed (12)",
                new int[]{0, 0, 1, 2, 3, 0, 1, 2, 3, 4, 0},
                StringSearching.buildFailureTable("bababcbabad"));
    }
    
    @Test
    public void kmpException() {
        assertException(
                "KMP should throw an IllegalArgumentException if the pattern is null",
                IllegalArgumentException.class,
                () -> StringSearching.kmp(null, "abcd"));
        
        assertException(
                "KMP should throw an IllegalArgumentException if the pattern is empty",
                IllegalArgumentException.class,
                () -> StringSearching.kmp("", "abcd"));
        
        assertException(
                "KMP should throw an IllegalArgumentException if the text is null",
                IllegalArgumentException.class,
                () -> StringSearching.kmp("abcd", null));
    }
    
    @Test
    public void kmpWithLargerPattern() {
        CountingSequence pattern = new CountingSequence("abcd");
        CountingSequence text = new CountingSequence("ab");
        List<Integer> returned = StringSearching.kmp(pattern, text);
        
        assertEquals(
                "KMP should return an empty list of results when the pattern is longer than the text",
                0,
                returned.size());
        
        assertArrayEquals(
                "KMP called charAt() on the text, even though the pattern was longer than the text",
                new int[]{0, 0},
                text.getCounts());
        
        assertArrayEquals(
                "KMP called charAt() on the pattern, even though the pattern was longer than the text",
                new int[]{0, 0, 0, 0},
                pattern.getCounts());
    }
    
    @Test
    public void kmpWithEmptyText() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        
        assertEquals(
                "You should return an empty list of results when the text is empty",
                0,
                result.size());
        
        // shouldn't ever happen without an exception anyway
        assertArrayEquals(
                "You called charAt() on the text, even though the text was empty",
                new int[]{},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchNoMatchesEndEarly() {
        StrictString pattern = new StrictString("abcde");
        CountingSequence text = new CountingSequence("aaaaa");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt(), possibly because it terminated too late",
                new int[]{1, 1, 0, 0, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchNoMatchesEndEarly2() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("aaaaa");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt(), possibly because it terminated too late",
                new int[]{1, 1, 0, 0, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchNoMatchesEndEarly3() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("abaaa");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt(), possibly because it terminated too late",
                new int[]{1, 1, 1, 1, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchMatchOffByOneTermination() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("abaaababa");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(4);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt(), possibly because it terminated too late",
                new int[]{1, 1, 1, 3, 2, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchOneMatch() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("abcabacababcababac");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(12);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt()",
                new int[]{1, 1, 2, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchTwoMatches() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("abcababaabcababaabcabc");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(3);
        expected.add(11);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt()",
                new int[]{1, 1, 2, 1, 1, 1, 1, 1, 3, 1, 2, 1, 1, 1, 1, 1, 3, 1, 1, 0, 0, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchTwoMatchesOverlapping() {
        StrictString pattern = new StrictString("ababa");
        CountingSequence text = new CountingSequence("abcababababcabcabcabcabcabc");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(3);
        expected.add(5);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt()",
                new int[]{1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 2, 1, 1, 2, 1, 1, 2, 1, 1, 1, 0, 0, 0},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchMatchWithBigSkipAtBeginning() {
        StrictString pattern = new StrictString("ababc");
        CountingSequence text = new CountingSequence("ababcababa");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void kmpSearchMatchWithBigAndSmallSkips() {
        StrictString pattern = new StrictString("ababc");
        CountingSequence text = new CountingSequence("cababdababcababaab");
        
        List<Integer> result = StringSearching.kmp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(6);
        
        assertEquals(
                "A KMP search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A KMP search had incorrect calls to charAt()",
                new int[]{1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 0},
                text.getCounts());
    }
    
    @Test
    public void generateHashExceptions() {
        assertException(
                "Generating a hash with a negative length should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("abcd", -1));
        
        assertException(
                "Generating a hash with a negative length should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("abcd", -2));
        
        assertException(
                "Generating a hash with a length greater than the length of the sequence should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("abcd", 6));
        
        assertException(
                "Generating a hash with a length greater than the length of the sequence should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash("", 1));
        
        assertException(
                "Generating a hash with a null sequence should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.generateHash(null, 5));
    }
    
    @Test
    public void updateHashExceptions() {
        assertException(
                "Updating a hash with a negative length should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.updateHash(0, -1, 'a', 'b'));
        
        assertException(
                "Updating a hash with a negative length should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> StringSearching.updateHash(0, -2, 'a', 'b'));
    }
    
    @Test
    public void generateHash() {
        assertEquals(
                "Generating an empty hash failed to return 0",
                0,
                StringSearching.generateHash("", 0));
        
        assertEquals(
                "Generating an empty hash failed to return 0",
                0,
                StringSearching.generateHash("abcd", 0));
        
        assertEquals(
                "Generating a length-1 hash failed",
                97,
                StringSearching.generateHash("a", 1));
        
        assertEquals(
                "Generating a hash failed",
                -696792214,
                StringSearching.generateHash("abcd", 4));
        
        assertEquals(
                "Generating a hash failed",
                18228867,
                StringSearching.generateHash("ab" + ((char) 0), 3));
    }
    
    @Test
    public void generateSubHash() {
        assertEquals(
                "Generating a substring hash failed",
                42099,
                StringSearching.generateHash("abcd", 2));
    }
    
    @Test
    public void hashCharAtCounts() {
        CountingSequence four = new CountingSequence("abcd");
        StringSearching.generateHash(four, 4);
        assertArrayEquals(
                "Generating a hash should access each used character exactly once",
                new int[]{1, 1, 1, 1},
                four.getCounts());
        
        CountingSequence two = new CountingSequence("abcd");
        StringSearching.generateHash(two, 2);
        assertArrayEquals(
                "Generating a hash should access each used character exactly once",
                new int[]{1, 1, 0, 0},
                two.getCounts());
    }
    
    @Test
    public void updateHash() {
        assertEquals(
                "Updating a simple hash failed",
                98,
                StringSearching.updateHash(97, 1, 'a', 'b'));
        
        // hash (a, b) ==> 42099
        // hash (b, c) ==> 42533
        
        assertEquals(
                "Updating a hash failed",
                42533,
                StringSearching.updateHash(42099, 2, 'a', 'c'));
        
        assertEquals(
                "Updating a longer hash failed",
                -1213413920,
                StringSearching.updateHash(-1816805665, 5, 'p', '!'));
    }
    
    @Test
    public void rabinKarpExceptions() {
        assertException(
                "Rabin-Karp should throw an IllegalArgumentException if the pattern is null",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp(null, "abcd"));
        
        assertException(
                "Rabin-Karp should throw an IllegalArgumentException if the pattern is empty",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp("", "abcd"));
        
        assertException(
                "Rabin-Karp should throw an IllegalArgumentException if the text is null",
                IllegalArgumentException.class,
                () -> StringSearching.rabinKarp("abcd", null));
    }
    
    @Test
    public void rabinKarpWithLargerPattern() {
        CountingSequence pattern = new CountingSequence("abcd");
        CountingSequence text = new CountingSequence("ab");
        List<Integer> returned = null;
        try {
             returned = StringSearching.rabinKarp(pattern, text);
        } catch (IllegalArgumentException ex) {
            Assert.fail("Rabin-Karp threw an IllegalArgumentException when the pattern was longer than the text, "
                    + "which is not specified in the javadoc.");
        }
        
        assertEquals(
                "Rabin-Karp should return an empty list of results when the pattern is longer than the text",
                0,
                returned.size());
        
        assertArrayEquals(
                "Rabin-Karp called charAt() on the text, even though the pattern was longer than the text",
                new int[]{0, 0},
                text.getCounts());
        
        assertArrayEquals(
                "Rabin-Karp called charAt() on the pattern, even though the pattern was longer than the text",
                new int[]{0, 0, 0, 0},
                pattern.getCounts());
    }
    
    @Test
    public void rabinKarpTestSimple() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("aabcdd");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(1);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 3, 2, 2, 2, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpTestAtBeginning() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("abcdab");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{3, 3, 2, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpTestAtEnd() {
        StrictString pattern = new StrictString("abcd");
        CountingSequence text = new CountingSequence("ababcd");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(2);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 2, 2, 2, 2, 2},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpWithOverlap() {
        StrictString pattern = new StrictString("abab");
        CountingSequence text = new CountingSequence("ababab");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        expected.add(2);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{3, 3, 3, 3, 2, 2},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpLengthOne() {
        StrictString pattern = new StrictString("a");
        CountingSequence text = new CountingSequence("abacad");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(0);
        expected.add(2);
        expected.add(4);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{3, 2, 3, 2, 3, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpHashCollisionWithoutMatch() {
        StrictString pattern = new StrictString("0Zy1");
        CountingSequence text = new CountingSequence("abe12Lab");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 2, 3, 2, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpHashCollisionWithMatch() {
        StrictString pattern = new StrictString("7lyM");
        CountingSequence text = new CountingSequence("a7lyMlC2h7lyMa");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(1);
        expected.add(9);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 3, 3, 3, 3, 3, 2, 2, 2, 3, 2, 2, 2, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpHashCollisionMoreThanOneLetter() {
        StrictString pattern = new StrictString("b0Zy1");
        CountingSequence text = new CountingSequence("abe12Lab");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 3, 3, 1, 1, 1, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void rabinKarpHashCollisionOverlap() {
        StrictString pattern = new StrictString("b0Zy1b");
        CountingSequence text = new CountingSequence("abe12Lb0Zy1bab");
        
        List<Integer> result = StringSearching.rabinKarp(pattern, text);
        List<Integer> expected = new LinkedList<Integer>();
        expected.add(6);
        
        assertEquals(
                "A Rabin-Karp search failed to return the correct result",
                expected,
                result);
        
        assertArrayEquals(
                "A Rabin-Karp search had incorrect calls to charAt()",
                new int[]{2, 3, 3, 2, 2, 2, 3, 3, 2, 2, 2, 2, 1, 1},
                text.getCounts());
    }
    
    @Test
    public void largeScaleUpdateHash() {
        Random r = new Random();
        
        for (int i = 0; i < 100000; i++) {
            StringBuilder text = new StringBuilder();
            for (int j = 0; j < 21; j++) {
                text.append((char) r.nextInt(128));
            }
            
            int hash = StringSearching.generateHash(text, 20);
            
            assertEquals(
                    "The large-scale hash updating test caught a problem with substrings",
                    StringSearching.generateHash(text.substring(0, 20), 20),
                    hash);
            
            assertEquals(
                    "The large-scale hash updating test caught a problem with updating hashes",
                    StringSearching.generateHash(text.substring(1), 20),
                    StringSearching.updateHash(hash, 20, text.charAt(0), text.charAt(20)));
        }
    }
    
    @Test
    public void largeScaleBoyerMooreSearch() {
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            int textLength = r.nextInt(20) + 10;
            int patternLength = r.nextInt(5) + 1;
            
            StringBuilder text = new StringBuilder();
            for (int j = 0; j < textLength; j++) {
                text.append((char) (r.nextInt(4) + 97));
            }

            StringBuilder pattern = new StringBuilder();
            for (int j = 0; j < patternLength; j++) {
                pattern.append((char) (r.nextInt(4) + 97));
            }

            List<Integer> naiveResult = naiveJavaSearch(pattern, text);
            
            assertEquals(
                    "A large-scale Boyer-Moore test failed.",
                    naiveResult,
                    StringSearching.boyerMoore(pattern, text));
        }
    }
    
    @Test
    public void largeScaleKMPSearch() {
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            int textLength = r.nextInt(20) + 10;
            int patternLength = r.nextInt(5) + 1;
            
            StringBuilder text = new StringBuilder();
            for (int j = 0; j < textLength; j++) {
                text.append((char) (r.nextInt(4) + 97));
            }

            StringBuilder pattern = new StringBuilder();
            for (int j = 0; j < patternLength; j++) {
                pattern.append((char) (r.nextInt(4) + 97));
            }

            List<Integer> naiveResult = naiveJavaSearch(pattern, text);
            
            assertEquals(
                    "A large-scale KMP test failed.",
                    naiveResult,
                    StringSearching.kmp(pattern, text));
        }
    }

    @Test
    public void largeScaleRabinKarpSearch() {
        Random r = new Random();
        for (int i = 0; i < 100000; i++) {
            int textLength = r.nextInt(20) + 10;
            int patternLength = r.nextInt(5) + 1;
            
            StringBuilder text = new StringBuilder();
            for (int j = 0; j < textLength; j++) {
                text.append((char) (r.nextInt(4) + 97));
            }

            StringBuilder pattern = new StringBuilder();
            for (int j = 0; j < patternLength; j++) {
                pattern.append((char) (r.nextInt(4) + 97));
            }

            List<Integer> naiveResult = naiveJavaSearch(pattern, text);
            
            assertEquals(
                    "A large-scale Rabin-Karp test failed.",
                    naiveResult,
                    StringSearching.rabinKarp(pattern, text));
        }
    }
}