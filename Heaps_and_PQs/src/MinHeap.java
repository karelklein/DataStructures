import java.util.NoSuchElementException;

/**
 * Your implementation of a min heap.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class MinHeap<T extends Comparable<? super T>>
    implements HeapInterface<T> {

    private T[] backingArray;
    private int size;
    // Do not add any more instance variables. Do not change the declaration
    // of the instance variables above.

    /**
     * Creates a Heap with an initial size of {@code STARTING_SIZE} for the
     * backing array.
     *
     * Use the constant field in the interface. Do not use magic numbers!
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[STARTING_SIZE];
        size = 0;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item");
        }
        if ((size + 1) == backingArray.length) {
            T[] temp = (T[]) new Comparable[(size + 1) * 3 / 2];
            for (int i = 0; i < size + 1; i++) {
                temp[i] = backingArray[i];
            }
            int index = size + 1;
            temp[index] = item;
            int parentIndex = index / 2;
            T parentItem = temp[parentIndex];
            while (parentIndex > 0
                    && (temp[parentIndex].compareTo(temp[index])) > 0) {
                temp[parentIndex] = item;
                temp[index] = parentItem;
                index = parentIndex;
                parentIndex = parentIndex / 2;
                parentItem = temp[parentIndex];
            }
            backingArray = temp;
            size++;
        } else {
            int index = size + 1;
            backingArray[index] = item;
            int parentIndex = index / 2;
            T parentItem = backingArray[parentIndex];
            while (parentIndex > 0
                    && backingArray[parentIndex].
                            compareTo(backingArray[index]) > 0) {
                backingArray[parentIndex] = item;
                backingArray[index] = parentItem;
                index = parentIndex;
                parentIndex = parentIndex / 2;
                parentItem = backingArray[parentIndex];
            }
            size++;
        }
    }

    @Override
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Cannot remove from empty heap");
        }
        T removed = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;
        int index = 1;
        while (((index < (backingArray.length / 2)
                && !((backingArray[2 * index] == null)
                && (backingArray[2 * index + 1] == null))
                && ((backingArray[index].
                compareTo(backingArray[2 * index]) > 0)
                || (!(2 * index + 1 > size) && backingArray[index].
                compareTo(backingArray[2 * index + 1]) > 0))))) {
            int childIndex;
            if (backingArray[2 * index] == null) {
                childIndex = 2 * index + 1;
            } else if (backingArray[2 * index + 1] == null) {
                childIndex = 2 * index;
            } else if (backingArray[2 * index].
                    compareTo(backingArray[2 * index + 1]) > 0) {
                childIndex = 2 * index + 1;
            } else {
                childIndex = 2 * index;
            }
            T temp = backingArray[index];
            backingArray[index] = backingArray[childIndex];
            backingArray[childIndex] = temp;
            index = childIndex;
        }
        return removed;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        backingArray = (T[]) new Comparable[STARTING_SIZE];
        size = 0;
    }

    @Override
    public Comparable[] getBackingArray() {
        // DO NOT CHANGE THIS METHOD!
        return backingArray;
    }

}
