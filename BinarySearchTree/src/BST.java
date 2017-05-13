import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of a binary search tree.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> implements BSTInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty BST.
     * YOU DO NOT NEED TO IMPLEMENT THIS CONSTRUCTOR!
     */
    public BST() {
    }

    /**
     * Initializes the BST with the data in the Collection. The data in the BST
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot accept null data");
        }
        for (T element : data) {
            if (element == null) {
                throw new IllegalArgumentException("Cannot accept null data");
            } else {
                add(element);
            }
        }
    }

    /**
     * Adds data to a new node by recursively searching
     * the tree for appropriate location to add node
     * @param data the data to be added
     * @param current the current node being examined
     * @return the new node that has been added,
     * or the current node if nothing added
     */
    private BSTNode<T> addr(T data, BSTNode<T> current) {
        if (current == null) {
            BSTNode<T> newnode = new BSTNode<>(data);
            size++;
            return newnode;
        } else {
            if (current.getData().compareTo(data) < 0) {
                current.setRight(addr(data, current.getRight()));
            } else if (current.getData().compareTo(data) > 0) {
                current.setLeft(addr(data, current.getLeft()));
            }
            return current;
        }
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot add null data");
        }
        root = addr(data, root);
    }

    /**
     * Removes the input data from the tree by recursively
     * searching the BST for the data
     * @param data the data to be removed from the BST
     * @param current the current node being examined
     * @return the node containing the data
     */
    private BSTNode<T> remover(T data, BSTNode<T> current) {
        if (current == null) {
            throw new NoSuchElementException("Data not found");
        } else {
            BSTNode<T> dummy = new BSTNode<>(data);
            if (current.getData().compareTo(data) == 0) {
                dummy.setData(current.getData());
                if (current.getLeft() == null && current.getRight() == null) {
                    current = null;
                } else if (current.getLeft() != null
                        && current.getRight() == null) {
                    current = current.getLeft();
                } else if (current.getLeft() == null
                        && current.getRight() != null) {
                    current = current.getRight();
                } else if (current.getLeft() != null
                        && current.getRight() != null) {
                    current = current.getLeft();
                    while (current.getRight() != null) {
                        current = current.getRight();
                    }
                }
                size--;
                return current;
            } else if (current.getData().compareTo(data) < 0) {
                current.setRight(remover(data, current.getRight()));
            } else if (current.getData().compareTo(data) > 0) {
                current.setLeft(remover(data, current.getLeft()));
            }
            return dummy;
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data");
        }
        return remover(data, root).getData();
    }

    /**
     * searches for data in BST, returns data from tree if found
     * @param data the data to be retrieved from the BST
     * @param current the node currently examined
     * @return the data from the BST if found
     */
    private T getr(T data, BSTNode<T> current) {
        if (current == null) {
            throw new NoSuchElementException("Data not found");
        } else {
            if (current.getData().equals(data)) {
                return current.getData();
            } else if (current.getData().compareTo(data) < 0) {
                return getr(data, current.getRight());
            } else if (current.getData().compareTo(data) > 0) {
                return getr(data, current.getLeft());
            }
            return current.getData();
        }
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot get null data");
        }
        return getr(data, root);
    }

    /**
     * Recurses through BST to see if it contains the input data
     * @param data the data to be searched for in the BST
     * @param current the node currently being examined
     * @return true if BST contains data, false otherwise
     */
    private boolean containsr(T data, BSTNode<T> current) {
        boolean answer = false;
        if (current == null) {
            return false;
        } else {
            if (current.getData().equals(data)) {
                answer = true;
            } else if (current.getData().compareTo(data) < 0) {
                return containsr(data, current.getRight());
            } else if (current.getData().compareTo(data) > 0) {
                return containsr(data, current.getLeft());
            }
            return answer;
        }
    }

    @Override
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Null data not accepted");
        }
        return containsr(data, root);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a preorder traversal of the BST in an ArrayList
     * @param current the current node being analyzed
     * @param preorderList the ArrayList to be populated
     * @return a preorder traversal ArrayList
     */
    private List<T> preorderr(BSTNode<T> current, ArrayList<T> preorderList) {
        if (current != null) {
            preorderList.add(current.getData());
            preorderr(current.getLeft(), preorderList);
            preorderr(current.getRight(), preorderList);
        }
        return preorderList;
    }

    @Override
    public List<T> preorder() {
        ArrayList<T> preorderList = new ArrayList<>();
        return preorderr(root, preorderList);
    }

    /**
     * Returns a postorder traversal of the BST in an ArrayList
     * @param current the current node analyzed
     * @param postorderList the ArrayList to be populated
     * @return a postorder traversal ArrayList
     */
    private List<T> postorderr(BSTNode<T> current, ArrayList<T> postorderList) {
        if (current != null) {
            postorderr(current.getLeft(), postorderList);
            postorderr(current.getRight(), postorderList);
            postorderList.add(current.getData());
        }
        return postorderList;
    }

    @Override
    public List<T> postorder() {
        ArrayList<T> postorderList = new ArrayList<>();
        return postorderr(root, postorderList);
    }

    /**
     * Returns an inorder traversal of the BST in an ArrayList
     * @param current the current node being analyzed
     * @param inorderList the ArrayList to be populated
     * @return an inorder traversal ArrayList
     */
    private List<T> inorderr(BSTNode<T> current, ArrayList<T> inorderList) {
        if (current != null) {
            inorderr(current.getLeft(), inorderList);
            inorderList.add(current.getData());
            inorderr(current.getRight(), inorderList);
        }
        return inorderList;
    }

    @Override
    public List<T> inorder() {
        ArrayList<T> inorderList = new ArrayList<>();
        return inorderr(root, inorderList);
    }

    /**
     * Returns a level order traversal of the BST
     * @param current the current node being analyzed
     * @param levelorderList the Queue to be populated
     * @return a level order traversal ArrayList
     */
    private List<T> levelorderr(BSTNode<T> current,
                                ArrayList<T> levelorderList) {
        Queue<BSTNode<T>> levelQueue = new LinkedList<>();
        levelQueue.add(current);
        while (!levelQueue.isEmpty()) {
            levelorderList.add(current.getData());
            levelQueue.remove();
            if (current.getLeft() != null) {
                levelQueue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                levelQueue.add(current.getRight());
            }
            if (levelQueue.peek() != null) {
                current = levelQueue.peek();
            }
        }
        return levelorderList;
    }

    @Override
    public List<T> levelorder() {
        ArrayList<T> levelorderList = new ArrayList<>();
        if (size == 0) {
            return levelorderList;
        } else {
            return levelorderr(root, levelorderList);
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the BST by recursion
     * @param current the current node analyzed
     * @return the height of the root of the tree, -1 if tree is empty
     */
    private int heightr(BSTNode<T> current) {
        if (current == null) {
            return -1;
        }
        int leftheight = heightr(current.getLeft());
        int rightheight = heightr(current.getRight());
        if (leftheight > rightheight) {
            return leftheight + 1;
        } else {
            return rightheight + 1;
        }
    }

    @Override
    public int height() {
        return heightr(root);
    }

    @Override
    public BSTNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}
