import java.util.NoSuchElementException;

/**
 * Your implementation of an array-backed queue.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class ArrayQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int front;
    private int back;
    private int size;

    /**
     * Constructs a new ArrayQueue.
     */
    public ArrayQueue() {
        size = 0;
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
    }

    /**
     * Dequeue from the front of the queue.
     *
     * Do not shrink the backing array.
     * If the queue becomes empty as a result of this call, you <b>must not</b>
     * explicitly reset front or back to 0.
     *
     * @see QueueInterface#dequeue()
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from empty list.");
        }

        T element = backingArray[front];
        backingArray[front] = null;
        front = (front + 1) % backingArray.length;
        size--;
        return element;
    }

    /**
     * Add the given data to the queue.
     *
     * If sufficient space is not available in the backing array, you should
     * regrow it to 1.5 times the current backing array length, rounding down
     * if necessary. If a regrow is necessary, you should copy elements to the
     * front of the new array and reset front to 0.
     *
     * @see QueueInterface#enqueue(T)
     */
    @Override
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot enqueue null values.");
        }

        if (size == backingArray.length) {
            T[] tempArray = (T[]) new Object[(int) (size * 1.5)];
            for (int i = 0; i < size; i++) {
                tempArray[i] = backingArray[(front + i) % backingArray.length];
            }
            backingArray = tempArray;
            backingArray[size] = data;
            front = 0;
            back = size;
            size++;
        } else {
            back = (front + size) % backingArray.length;
            backingArray[back] = data;
            size++;
        }
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the backing array of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the backing array
     */
    public Object[] getBackingArray() {
        // DO NOT MODIFY!
        return backingArray;
    }
}