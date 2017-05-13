/**
 * Your implementation of a DoublyLinkedList
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class DoublyLinkedList<T> implements LinkedListInterface<T> {
    // Do not add new instance variables.
    private LinkedListNode<T> head;
    private LinkedListNode<T> tail;
    private int size;

    @Override
    public void addAtIndex(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Invalid index, must be between 0 and size of list.");
        } else if (data == null) {
            throw new IllegalArgumentException("Cannot take in null as data.");
        }
        if (index == 0) {
            addToFront(data);
        } else if (index == size) {
            addToBack(data);
        } else {
            LinkedListNode<T> node = head;
            for (int i = 0; i < index - 1; i++) {
                node = node.getNext();
            }
            LinkedListNode<T> newnode = new LinkedListNode<T>(data, node, node.getNext());
            node.setNext(newnode);
            newnode.getNext().setPrevious(newnode);
            size++;
        }
    }

    @Override
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot take in null as data.");
        }
        if (size == 0) {
            LinkedListNode<T> node = new LinkedListNode<T>(data);
            head = node;
            tail = node;
            size++;
        } else {
            LinkedListNode<T> node = head;
            LinkedListNode<T> newnode = new LinkedListNode<T>(data, null, node);
            node.setPrevious(newnode);
            head = newnode;
            size++;
        }
    }

    @Override
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot take in null as data.");
        }
        if (size == 0) {
            LinkedListNode<T> node = new LinkedListNode<T>(data);
            head = node;
            tail = node;
            size++;
        } else {
            LinkedListNode<T> node = tail;
            LinkedListNode<T> newnode = new LinkedListNode<T>(data, node, null);
            node.setNext(newnode);
            tail = newnode;
            size++;
        }
    }

    @Override
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Error: index out of bounds. " +
                    " Please enter index between 0 and list size-1");
        }
        if (index == 0) {
            return removeFromFront();
        } else if (index == size - 1) {
            return removeFromBack();
        } else {
            LinkedListNode<T> node = head;
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
            T removedObj = node.getData();
            node.getNext().setPrevious(node.getPrevious());
            node.getPrevious().setNext(node.getNext());
            size--;
            return removedObj;
        }

    }

    @Override
    public T removeFromFront() {
        if (size == 0) {
            return null;
        } else {
            LinkedListNode<T> node = head;
            T removedObj = head.getData();
            head = node.getNext();
            head.setPrevious(null);
            size--;
            return removedObj;
        }
    }

    @Override
    public T removeFromBack() {
        if (size == 0) {
            return null;
        } else {
            LinkedListNode<T> node = tail;
            T removedObj = tail.getData();
            tail = node.getPrevious();
            tail.setNext(null);
            size--;
            return removedObj;
        }
    }

    @Override
    public boolean removeAllOccurrences(T data) {
        boolean exist = false;
        if (data == null) {
            throw new IllegalArgumentException("Error: cannot input null data.");
        }
        LinkedListNode<T> node = head;
        int listsize = size;
        for (int i = 0; i < listsize; i++) {
            if (node.getData() == data) {
                if (node == head) {
                    head = node.getNext();
                    head.setPrevious(null);
                } else if (node == tail) {
                    tail = node.getPrevious();
                    tail.setNext(null);
                } else {
                    node.getNext().setPrevious(node.getPrevious());
                    node.getPrevious().setNext(node.getNext());
                }
                size--;
                exist = true;
            }
            node = node.getNext();
        }
        return exist;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Cannot take in index outside of list bounds");
        }
        if (index == 0) {
            return head.getData();
        } else if (index == size - 1) {
            return tail.getData();
        } else {
            LinkedListNode<T> node = head;
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
            return node.getData();
        }
    }

    @Override
    public Object[] toArray() {
        T[] objArray = (T[]) new Object[size];
        LinkedListNode<T> node = head;
        for (int i = 0; i < size; i++) {
            objArray[i] = node.getData();
            node = node.getNext();
        }
        return objArray;
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
        head = null;
        tail = null;
        size == 0;
    }

    @Override
    public LinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    @Override
    public LinkedListNode<T> getTail() {
        // DO NOT MODIFY!
        return tail;
    }
}