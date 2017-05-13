import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * JUnit test cases for the Sorting assignment.
 *
 * @version 2
 * @author Timothy J. Aveni
 */

public class SortingTests {

    @Rule
    public Timeout globalTimeout = new Timeout(2000);

    public final PrintStream originalSystemOut = System.out;
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

    private interface SortFunction<T> {
        void sort(T[] array, Comparator<T> comparator);
    }

    private void assertDoesSort(SortFunction<Integer> sort) {
        assertDoesSort(sort, Comparator.naturalOrder());
    }

    private void assertDoesSort(SortFunction<Integer> sort, Comparator<Integer> comparator) {
        for (int i = 0; i < 100; i++) {
            Integer[] array = new Integer[100];
            for (int j = 0; j < array.length; j++) {
                array[j] = (int) (Math.random() * 100) - 50;
            }

            sort.sort(array, comparator);

            assertIsSorted(array);
        }

        // already sorted:
        Integer[] array = new Integer[1000];
        for (int j = 0; j < array.length; j++) {
            array[j] = j - 500;
        }

        sort.sort(array, Comparator.naturalOrder());

        assertIsSorted(array);

        // reverse order:
        array = new Integer[1000];
        for (int j = 0; j < array.length; j++) {
            array[j] = 1000 - j - 500;
        }

        sort.sort(array, Comparator.naturalOrder());

        assertIsSorted(array);
    }

    private void assertIsSorted(Integer[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i + 1]) > 0) {
                Assert.fail("Array was not sorted");
            }
        }
    }

    private void assertIsSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                Assert.fail("Array was not sorted");
            }
        }
    }

    private class StableInteger implements Comparable<StableInteger> {
        int data;
        int secondaryData;

        StableInteger(int data, int secondaryData) {
            this.data = data;
            this.secondaryData = secondaryData;
        }

        @Override
        public int compareTo(StableInteger other) {
            if (data < other.data) {
                return -1;
            } else if (data > other.data) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public String toString() {
            return data + "." + secondaryData;
        }
    }

    private void assertDoesSort(SortFunction<StableInteger> sort, boolean stable) {
        assertDoesSort(sort, Comparator.naturalOrder(), stable);
    }

    private void assertDoesSort(SortFunction<StableInteger> sort, Comparator<StableInteger> comparator, boolean stable) {
        for (int i = 0; i < 100; i++) {
            StableInteger[] array = new StableInteger[100];
            for (int j = 0; j < array.length; j++) {
                array[j] = new StableInteger((int) (Math.random() * 100) - 50, j);
            }

            sort.sort(array, comparator);

            assertIsSortedStably(array);
        }

        // already sorted:
        StableInteger[] array = new StableInteger[1000];
        for (int j = 0; j < array.length; j++) {
            array[j] = new StableInteger(j - 500, j);
        }

        sort.sort(array, Comparator.naturalOrder());

        assertIsSortedStably(array);

        // reverse order:
        array = new StableInteger[1000];
        for (int j = 0; j < array.length; j++) {
            array[j] = new StableInteger(1000 - j - 500, j);
        }

        sort.sort(array, Comparator.naturalOrder());

        assertIsSortedStably(array);
    }

    private void assertIsSortedStably(StableInteger[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int comparison = array[i].compareTo(array[i + 1]);
            if (comparison > 0) {
                Assert.fail("Array was not sorted");
            } else if (comparison == 0) {
                if (array[i].secondaryData > array[i + 1].secondaryData) {
                    Assert.fail("Sort was not stable");
                }
            }
        }
    }

    private void assertStandardExceptions(SortFunction<Integer> sort, String sortName) {
        assertException(
                "Passing a null array to " + sortName + " should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sort.sort(null, Comparator.naturalOrder()));

        assertException(
                "Passing a null comparator to " + sortName + " should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> sort.sort(new Integer[4], null));
    }

    private class TrickySignComparator implements Comparator<Integer> {
        // Comparators can return any negative, a zero, or any positive - not just -1/0/1

        @Override
        public int compare(Integer one, Integer two) {
            if (one < two) {
                return -2;
            } else if (one > two) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    @Test
    public void cocktailExceptions() {
        assertStandardExceptions((array, comparator) -> Sorting.cocktailSort(array, comparator), "cocktail sort");
    }

    @Test
    public void cocktailSort() {
        assertDoesSort((array, comparator) -> Sorting.cocktailSort(array, comparator));
    }

    @Test
    public void cocktailStability() {
        assertDoesSort((array, comparator) -> Sorting.cocktailSort(array, comparator), true);
    }

    @Test
    public void cocktailComparatorSign() {
        assertDoesSort((array, comparator) -> Sorting.cocktailSort(array, comparator), new TrickySignComparator());
    }

    @Test
    public void insertionExceptions() {
        assertStandardExceptions((array, comparator) -> Sorting.insertionSort(array, comparator), "insertion sort");
    }

    @Test
    public void insertionSort() {
        assertDoesSort((array, comparator) -> Sorting.insertionSort(array, comparator));
    }

    @Test
    public void insertionStability() {
        assertDoesSort((array, comparator) -> Sorting.insertionSort(array, comparator), true);
    }

    @Test
    public void insertionComparatorSign() {
        assertDoesSort((array, comparator) -> Sorting.insertionSort(array, comparator), new TrickySignComparator());
    }

    @Test
    public void selectionExceptions() {
        assertStandardExceptions((array, comparator) -> Sorting.selectionSort(array, comparator), "selection sort");
    }

    @Test
    public void selectionSort() {
        assertDoesSort((array, comparator) -> Sorting.selectionSort(array, comparator));
    }

    @Test
    public void selectionComparatorSign() {
        assertDoesSort((array, comparator) -> Sorting.selectionSort(array, comparator), new TrickySignComparator());
    }

    @Test
    public void quickExceptions() {
        assertStandardExceptions((array, comparator) -> Sorting.quickSort(array, comparator, new Random()), "quicksort");
        assertException(
                "Passing a null random object to quicksort should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> Sorting.quickSort(new Integer[4], Comparator.naturalOrder(), null));
    }

    @Test
    public void quickSort() {
        assertDoesSort((array, comparator) -> Sorting.quickSort(array, comparator, new Random()));
    }

    @Test
    public void quickComparatorSign() {
        assertDoesSort((array, comparator) -> Sorting.quickSort(array, comparator, new Random()), new TrickySignComparator());
    }

    @Test
    public void mergeExceptions() {
        assertStandardExceptions((array, comparator) -> Sorting.mergeSort(array, comparator), "mergesort");
    }

    @Test
    public void mergeSort() {
        assertDoesSort((array, comparator) -> Sorting.mergeSort(array, comparator));
    }

    @Test
    public void mergeStability() {
        assertDoesSort((array, comparator) -> Sorting.mergeSort(array, comparator), true);
    }

    @Test
    public void mergeComparatorSign() {
        assertDoesSort((array, comparator) -> Sorting.mergeSort(array, comparator), new TrickySignComparator());
    }

    @Test
    public void radixExceptions() {
        assertException(
                "Passing a null array to radix short should throw an IllegalArgumentException",
                IllegalArgumentException.class,
                () -> Sorting.lsdRadixSort(null));
    }

    @Test
    public void radixSort() {
        // cannot test radix sort stability with the provided method signature
        for (int i = 0; i < 100; i++) {
            int[] array = new int[1000];
            for (int j = 0; j < array.length; j++) {
                array[j] = (int) (Math.random() * 2016);
            }

            Sorting.lsdRadixSort(array);

            assertIsSorted(array);
        }
    }

    @Test
    public void radixSortWithNegatives() {
        // cannot test radix sort stability with the provided method signature
        for (int i = 0; i < 10; i++) {
            int[] array = new int[100];
            for (int j = 0; j < array.length; j++) {
                array[j] = (int) (Math.random() * 2016) - 1008;
            }

            Sorting.lsdRadixSort(array);

            assertIsSorted(array);
        }
    }
}