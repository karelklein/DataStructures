import java.util.NoSuchElementException;

/**
 * Your implementation of an array-backed stack.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class ArrayStack<T> implements StackInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayStack.
     */
    public ArrayStack() {
        size = 0;
        backingArray = (T[]) new Object[INITIAL_CAPACITY];
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Pop from the stack.
     *
     * Do not shrink the backing array.
     *
     * @see StackInterface#pop()
     */
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot pop from an empty stack.");
        }

        int top = size - 1;
        T temp = backingArray[top];
        backingArray[top] = null;
        size--;
        return temp;
    }

    /**
     * Push the given data onto the stack.
     *
     * If sufficient space is not available in the backing array, you should
     * regrow it to 1.5 times the current backing array length, rounding down
     * if necessary.
     *
     //* @see StackInterface#push()
     */
    @Override
    public void push(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot push null into stack.");
        }

        if (size == backingArray.length) {
            T[] tempArray = (T[]) new Object[(int) (size * 1.5)];
            for (int i = 0; i < size; i++) {
                tempArray[i] = backingArray[i];
            }
            tempArray[size] = data;
            backingArray = tempArray;
            size++;
        } else {
            backingArray[size] = data;
            size++;
        }
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the backing array of this stack.
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