import java.util.LinkedList;
import java.util.Comparator;
import java.util.Random;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class Sorting {

    /**
     * Implement cocktail sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable).
     *
     * See the PDF for more info on this sort.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null array or comparator");
        }
        int i;
        int iStart = 0;
        int size = arr.length;
        boolean needsSwap = true;

        while ((iStart < size - 1) && needsSwap) {
            needsSwap = false;
            for (i = iStart; i < size - 1; i++) {
                if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                    swap(arr, i, i + 1);
                    needsSwap = true;
                }
            }
            size--;
            if (needsSwap) {
                for (i = size - 1; i > iStart; i--) {
                    if (comparator.compare(arr[i - 1], arr[i]) > 0) {
                        swap(arr, i, i - 1);
                        needsSwap = true;
                    }
                }
            }
            iStart++;
        }
    }

    /**
     * Implement insertion sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable).
     *
     * See the PDF for more info on this sort.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null array or comparator");
        }
        int size = arr.length;
        int i;
        int k;
        int l;
        for (int j = 1; j < size; j++) {
            i = j - 1;
            k = i;
            l = j;
            while (l > 0 && comparator.compare(arr[k], arr[l]) > 0) {
                swap(arr, k, l);
                k--;
                l--;
            }
        }
    }

    /**
     * Implement selection sort.
     *
     * It should be:
     *  in-place
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n^2)
     *
     * Note that there may be duplicates in the array, but they may not
     * necessarily stay in the same relative order.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null array or comparator");
        }
        T min;
        int index;
        int size = arr.length;
        for (int j = 0; j < size; j++) {
            index = j;
            min = arr[j];
            for (int i = j + 1; i < size; i++) {
                if (comparator.compare(min, arr[i]) > 0) {
                    min = arr[i];
                    index = i;
                }
            }
            T temp = arr[j];
            arr[j] = min;
            arr[index] = temp;
        }
    }

    /**
     * Swaps item at index1 with item at index2
     * @param arr the array where the items are
     * @param index1 the index of first item
     * @param index2 the index of second item
     * @param <T> the type of item
     */
    private static <T> void swap(T[] arr, int index1, int index2) {
        T temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots.
     * For example if you need a pivot between a (inclusive)
     * and b (exclusive) where b > a, use the following code:
     *
     * int pivotIndex = r.nextInt(b - a) + a;
     *
     * It should be:
     *  in-place
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * Note that there may be duplicates in the array.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not use the one we have taught you!
     *
     * @throws IllegalArgumentException if the array or comparator or rand is
     * null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand the Random object used to select pivots
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                     Random rand) {
        if (arr == null || comparator == null || rand == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null array or null comparator.");
        }
        quickSort(arr, comparator, rand, 0, arr.length);
    }

    /**
     * quickSort an array implementing recursion
     * @param arr the array to be sorted
     * @param comparator the Comparator used to compare the data
     * @param rand the Random object used to select pivots
     * @param left the left bound
     * @param right the right bound
     * @param <T> the type of data to sort
     */
    private static <T> void quickSort(T[] arr, Comparator<T> comparator,
                                      Random rand, int left, int right) {
        int pivotIndex = rand.nextInt(right - left) + left;
        T pivot = arr[pivotIndex];
        swap(arr, pivotIndex, left);
        int leftIndex = left + 1;
        int rightIndex = right - 1;
        while (leftIndex <= rightIndex) {
            while ((leftIndex <= rightIndex)
                    && (comparator.compare(pivot, arr[leftIndex]) >= 0)) {
                leftIndex++;
            }
            while ((leftIndex <= rightIndex)
                    && (comparator.compare(pivot, arr[rightIndex])) <= 0) {
                rightIndex--;
            }
            if (leftIndex < rightIndex) {
                swap(arr, leftIndex, rightIndex);
                leftIndex++;
                rightIndex--;
            }
        }
        swap(arr, left, rightIndex);
        if (rightIndex > left) {
            quickSort(arr, comparator, rand, left, rightIndex);
        }
        if (right > (rightIndex + 1)) {
            quickSort(arr, comparator, rand, rightIndex + 1, right);
        }
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     *  stable
     *
     * Have a worst case running time of:
     *  O(n log n)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * You can create more arrays to run mergesort, but at the end,
     * everything should be merged back into the original T[]
     * which was passed in.
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null || comparator == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null array or comparator.");
        }
        int length = arr.length;
        if (length == 1) {
            return;
        }
        int midIndex = length / 2;
        T[] leftArray = (T[]) new Object[midIndex];
        for (int i = 0; i < midIndex; i++) {
            leftArray[i] = arr[i];
        }
        T[] rightArray = (T[]) new Object[length - midIndex];
        for (int j = 0; j < (length - midIndex); j++) {
            rightArray[j] = arr[j + midIndex];
        }
        mergeSort(leftArray, comparator);
        mergeSort(rightArray, comparator);

        int leftIndex = 0;
        int rightIndex = 0;
        int currentIndex = 0;

        while ((leftIndex < midIndex) && (rightIndex < (length - midIndex))) {
            if (comparator.compare(leftArray[leftIndex],
                    rightArray[rightIndex]) <= 0) {
                arr[currentIndex] = leftArray[leftIndex];
                leftIndex++;
            } else {
                arr[currentIndex] = rightArray[rightIndex];
                rightIndex++;
            }
            currentIndex++;
        }
        while (leftIndex < midIndex) {
            arr[currentIndex] = leftArray[leftIndex];
            leftIndex++;
            currentIndex++;
        }
        while (rightIndex < (length - midIndex)) {
            arr[currentIndex] = rightArray[rightIndex];
            rightIndex++;
            currentIndex++;
        }
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code!
     *
     * It should be:
     *  stable
     *
     * Have a worst case running time of:
     *  O(kn)
     *
     * And a best case running time of:
     *  O(kn)
     *
     * Any duplicates in the array should be in the same relative position after
     * sorting as they were before sorting. (stable)
     *
     * Do NOT use {@code Math.pow()} in your sort. Instead, if you need to, use
     * the provided {@code pow()} method below.
     *
     * You may use {@code java.util.ArrayList} or {@code java.util.LinkedList}
     * if you wish, but it may only be used inside radix sort and any radix sort
     * helpers. Do NOT use these classes with other sorts.
     *
     * @throws IllegalArgumentException if the array is null
     * @param arr the array to be sorted
     * @return the sorted array
     */
    public static int[] lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot accept null array.");
        }
        int longest = 0;
        for (int i = 0; i < arr.length; i++) {
            if (Math.abs(arr[i]) > longest) {
                longest = Math.abs(arr[i]);
            }
        }
        int iterations = 1;
        while (longest / 10 >= 1) {
            iterations++;
            longest = longest / 10;
        }
        for (int iter = 1; iter <= iterations; iter++) {
            LinkedList<Integer>[] buckets = new LinkedList[19];
            for (int k : arr) {
                int lsd = (Math.abs(k) % pow(10, iter)) / pow(10, iter - 1);
                int index;
                if (k < 0) {
                    index = lsd * -1 + 9;
                } else {
                    index = lsd + 9;
                }
                if (buckets[index] == null) {
                    buckets[index] = new LinkedList<Integer>();
                }
                buckets[index].add(k);
            }
            LinkedList<Integer> transferList = new LinkedList<Integer>();
            for (LinkedList<Integer> bucket : buckets) {
                if (bucket != null) {
                    for (int num : bucket) {
                        transferList.add(num);
                    }
                }
            }
            for (int j = 0; j < transferList.size(); j++) {
                arr[j] = transferList.get(j);
            }
        }
        return arr;
    }

    /**
     * Calculate the result of a number raised to a power. Use this method in
     * your radix sorts instead of {@code Math.pow()}.
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
