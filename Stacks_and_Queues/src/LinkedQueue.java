import java.util.NoSuchElementException;

/**
 * Your implementation of a linked queue.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class LinkedQueue<T> implements QueueInterface<T> {

    // Do not add new instance variables.
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    @Override
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from empty list");
        }

        T element = head.getData();
        if (size == 1) {
            head = null;
            tail = null;
            size--;
        } else {
            head = head.getNext();
            size--;
        }
        return element;
    }

    @Override
    public void enqueue(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot enqueue null value.");
        }

        LinkedNode<T> node = new LinkedNode<T>(data);
        if (size == 0) {
            head = node;
            tail = node;
            size++;
        } else {
            tail.setNext(node);
            tail = node;
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
     * Returns the head of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the head node
     */
    public LinkedNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the tail of this queue.
     * Normally, you would not do this, but we need it for grading your work.
     *
     * DO NOT USE THIS METHOD IN YOUR CODE.
     *
     * @return the tail node
     */
    public LinkedNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}