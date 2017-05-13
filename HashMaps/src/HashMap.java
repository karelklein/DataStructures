import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Your implementation of HashMap.
 * 
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class HashMap<K, V> implements HashMapInterface<K, V> {

    // Do not make any new instance variables.
    private MapEntry<K, V>[] table;
    private int size;

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code INITIAL_CAPACITY}.
     *
     * Do not use magic numbers!
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Create a hash map with no entries. The backing array has an initial
     * capacity of {@code initialCapacity}.
     *
     * You may assume {@code initialCapacity} will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        table = (MapEntry<K, V>[]) new MapEntry[initialCapacity];
        size = 0;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException(
                    "Cannot accept null key or value");
        }
        size++;
        MapEntry<K, V> newEntry = new MapEntry(key, value);
        double loadFactor = ((double) size) / table.length;
        if (loadFactor > MAX_LOAD_FACTOR) {
            resizeBackingTable(2 * table.length + 1);
        }

        int index = Math.abs(newEntry.getKey().hashCode()) % table.length;
        MapEntry<K, V> currentEntry = table[index];
        V returnValue = null;

        if (currentEntry == null) {
            table[index] = newEntry;
        } else {
            while (currentEntry != null) {
                if (currentEntry.getKey().equals(newEntry.getKey())) {
                    returnValue = currentEntry.getValue();
                    size--;
                    currentEntry.setValue(newEntry.getValue());
                } else if (currentEntry.getNext() == null) {
                    newEntry.setNext(table[index]);
                    table[index] = newEntry;
                    currentEntry = currentEntry.getNext();
                } else {
                    currentEntry = currentEntry.getNext();
                }
            }
        }
        return returnValue;
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot remove null key");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        MapEntry<K, V> current = table[index];
        V returnValue = null;
        int count = 0;

        if (current == null) {
            throw new NoSuchElementException("Key is not in the map");
        } else {
            while ((current != null) && (returnValue == null)) {
                if (current.getKey().equals(key)) {
                    size--;
                    returnValue = current.getValue();
                    if (count == 0) {
                        table[index] = current.getNext();
                    }
                } else if (current.getNext().getKey().equals(key)) {
                    size--;
                    returnValue = current.getNext().getValue();
                    current.setNext(current.getNext().getNext());
                } else if (current.getNext() == null) {
                    throw new NoSuchElementException("Key is not in the map");
                }
                current = current.getNext();
            }
        }
        return returnValue;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot get null key");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        MapEntry<K, V> current = table[index];
        V returnValue = null;
        if (current == null) {
            throw new NoSuchElementException("Key is not in the map");
        } else {
            while (current != null) {
                if (current.getKey().equals(key)) {
                    returnValue = current.getValue();
                } else if (current.getNext() == null) {
                    throw new NoSuchElementException("Key is not in the map");
                }
                current = current.getNext();
            }
        }
        return returnValue;
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot search for a null key");
        }
        int index = Math.abs(key.hashCode()) % table.length;
        MapEntry<K, V> current = table[index];
        boolean contains = false;
        if (current == null) {
            contains = false;
        } else {
            while (current != null) {
                if (current.getKey().equals(key)) {
                    return true;
                } else if (current.getNext() == null) {
                    contains = false;
                }
                current = current.getNext();
            }
        }
        return contains;
    }

    @Override
    public void clear() {
        table = (MapEntry<K, V>[]) new MapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int index = 0; index < table.length; index++) {
            MapEntry<K, V> current = table[index];
            while (current != null) {
                keySet.add(current.getKey());
                current = current.getNext();
            }
        }
        return keySet;
    }

    @Override
    public List<V> values() {
        List<V> valueList = new ArrayList<>();
        for (int index = 0; index < table.length; index++) {
            MapEntry<K, V> current = table[index];
            while (current != null) {
                valueList.add(current.getValue());
                current = current.getNext();
            }
        }
        return valueList;
    }

    @Override
    public void resizeBackingTable(int length) {
        if (length < 1 || length < size) {
            throw new IllegalArgumentException(
                    "Cannot resize when length < size or length < 1");
        }
        MapEntry<K, V>[] newTable = (MapEntry<K, V>[]) new MapEntry[length];
        for (MapEntry<K, V> element : table) {
            if (element != null) {
                while (element != null) {
                    int newIndex = Math.abs(element.getKey().hashCode()) % length;
                    System.out.println("newIndex: " + newIndex);

                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = element;
                        element.setNext(null);
                    } else {
                        MapEntry<K, V> nextElement = newTable[newIndex];
                        element.setNext(nextElement);
                        newTable[newIndex] = element;
                    }
                    element = element.getNext();
                }
            }
        }
        table = newTable;
    }
    
    @Override
    public MapEntry<K, V>[] getTable() {
        // DO NOT EDIT THIS METHOD!
        return table;
    }

}