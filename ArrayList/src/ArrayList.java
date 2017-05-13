/**
 * Your implementation of an ArrayList.
 *
 * @author Karel Klein-Cardena
 * @version 1
 */
public class ArrayList<T> implements ArrayListInterface<T> {

    // Do not add new instance variables.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new ArrayList.
     *
     * You may add statements to this method.
     */
    public ArrayList() {
        size = 0;
        backingArray = (T[]) new Object[INITIAL_CAPACITY];

    }

    @Override
    public void addAtIndex(int index, T data) {
        T[] newbackingArray;

        if (data == null) {
            throw new IllegalArgumentException("Null values not accepted");
        } else if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        } else if (size == backingArray.length) {
            newbackingArray = (T[]) new Object[size*2];
            for (int i = 0; i < index; i++) {
                newbackingArray[i] = backingArray[i];
            }
            newbackingArray[index] = data;
            for (int i = index + 1; i < size + 1; i++) {
                newbackingArray[i] = backingArray[i-1];
            }
        } else {
            newbackingArray = (T[]) new Object[backingArray.length];
            for (int i = 0; i < index; i++) {
                newbackingArray[i] = backingArray[i];
            }
            newbackingArray[index] = data;
            for (int i = index + 1; i < size + 1; i++) {
                newbackingArray[i] = backingArray[i-1];
            }

        }
        size += 1;
    }

    @Override
    public void addToFront(T data) {
        /*T[] newbackingArray;

        if (data == null) {
            throw new IllegalArgumentException("Null values not accepted");
        } else if(size == backingArray.length) {
            newbackingArray = (T[]) new Object[size * 2];
        } else {
            newbackingArray = (T[]) new Object[size];
        }
        for (int i = 0; i < size; i++) {
            newbackingArray[i+1] = backingArray[i];
        }
        newbackingArray[0] = data;
        backingArray = newbackingArray;
        size += 1; */

        addAtIndex(0, data);
    }

    @Override
    public void addToBack(T data) {
        T[] newbackingArray;

        if (data == null) {
            throw new IllegalArgumentException("Null values not accepted");
        } else if(size == backingArray.length) {
            newbackingArray = (T[]) new Object[size * 2];
        } else {
            newbackingArray = (T[]) new Object[backingArray.length];
        }
        newbackingArray = backingArray;
        newbackingArray[size] = data;
        backingArray = newbackingArray;
        size += 1;
    }

    @Override
    public T removeAtIndex(int index) {
        T[] newbackingArray;
        T removedObj;

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index is out of bounds");
        } else {
            removedObj = backingArray[index];
            newbackingArray = (T[]) new Object[size -1];
            for (int i = 0; i < index; i++) {
                newbackingArray[i] = backingArray[i];
            }
            for (int i = index; i < size - 1; i++) {
                newbackingArray[i] = backingArray[i + 1];
            }
        }
        backingArray = newbackingArray;
        size -= 1;
        return removedObj;

    }

    @Override
    public T removeFromFront() {
        if (backingArray.length == 0) {
            return null;
        } else {
             return removeAtIndex(0);
        }
    }

    @Override
    public T removeFromBack() {
        if (backingArray.length == 0) {
            return null;
        } else {
           return removeAtIndex(backingArray.length -1);
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        } else {
            return backingArray[index];
        }
    }

    @Override
    public boolean isEmpty() {
        if (backingArray.length == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        T[] emptyArray;
        emptyArray = (T[]) new Object[INITIAL_CAPACITY];
        backingArray = emptyArray;

    }

    @Override
    public Object[] getBackingArray() {
        // DO NOT MODIFY.
        return backingArray;
    }
}