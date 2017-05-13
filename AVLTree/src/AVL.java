import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Karel Klein-Cardena
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> implements AVLInterface<T> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no argument constructor that should initialize an empty AVL tree.
     * DO NOT IMPLEMENT THIS CONSTRUCTOR!
     */
    public AVL() {
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
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
     * Calculates the Balancing factor for input node
     * @param curr the node whose BF is needed
     * @return the Balancing factor of input node
     */

    private int nodeBF(AVLNode<T> curr) {
        int nodeBF;
        if (curr.getLeft() == null && curr.getRight() == null) {
            nodeBF = 0;
        } else if (curr.getLeft() == null && curr.getRight() != null) {
            nodeBF = -1 - curr.getRight().getHeight();
        } else if (curr.getLeft() != null && curr.getRight() == null) {
            nodeBF = curr.getLeft().getHeight() + 1;
        } else {
            nodeBF = curr.getLeft().getHeight() - curr.getRight().getHeight();
        }
        return nodeBF;
    }

    /**
     * Calculates the node height for the input node
     * @param curr the node whose height is examined
     * @return the height of the node
     */
    private int nodeHeight(AVLNode<T> curr) {
        int nodeHeight;
        if (curr.getLeft() == null && curr.getRight() == null) {
            nodeHeight = 0;
        } else if (curr.getLeft() == null && curr.getRight() != null) {
            nodeHeight = curr.getRight().getHeight() + 1;
        } else if (curr.getLeft() != null && curr.getRight() == null) {
            nodeHeight = curr.getLeft().getHeight() + 1;
        } else {
            nodeHeight = Math.max(curr.getLeft().getHeight(),
                    curr.getRight().getHeight()) + 1;
        }
        return nodeHeight;
    }

    /**
     * perform a left rotation
     * @param curr the node under examination
     * @return the root of the subtree
     */
    private AVLNode<T> leftRotation(AVLNode<T> curr) {
        AVLNode<T> middleNode = curr.getRight();
        AVLNode<T> temp = middleNode;
        AVLNode<T> newLeft = new AVLNode<T>(curr.getData());

        newLeft.setLeft(curr.getLeft());
        newLeft.setRight(middleNode.getLeft());
        newLeft.setBalanceFactor(nodeBF(newLeft));
        newLeft.setHeight(nodeHeight(newLeft));

        temp.setLeft(newLeft);

        return temp;
    }

    /**
     * perform a right rotation
     * @param curr the node under examination
     * @return the root of the subtree
     */
    private AVLNode<T> rightRotation(AVLNode<T> curr) {
        AVLNode<T> middleNode = curr.getLeft();
        AVLNode<T> temp = middleNode;
        AVLNode<T> newRight = new AVLNode<T>(curr.getData());

        newRight.setRight(curr.getRight());
        newRight.setLeft(middleNode.getRight());
        newRight.setBalanceFactor(nodeBF(newRight));
        newRight.setHeight(nodeHeight(newRight));

        temp.setRight(newRight);

        return temp;
    }

    /**
     * balances subtree depending on case
     * @param current the root of subtree to balance
     * @return the new root of balanced subtree
     */
    private AVLNode<T> balanceTree(AVLNode<T> current) {
        if (current.getBalanceFactor() == -2
                && (current.getRight().getBalanceFactor() == 0
                || current.getRight().getBalanceFactor() == -1)) {
            current = leftRotation(current);
        } else if (current.getBalanceFactor() == 2
                && (current.getLeft().getBalanceFactor() == 0
                || current.getLeft().getBalanceFactor() == 1)) {
            current = rightRotation(current);
        } else if (current.getBalanceFactor() == -2
                && current.getRight().getBalanceFactor() == 1) {
            current.setRight(rightRotation(current.getRight()));
            current = leftRotation(current);
        } else if (current.getBalanceFactor() == 2
                && current.getLeft().getBalanceFactor() == -1) {
            current.setLeft(leftRotation(current.getLeft()));
            current = rightRotation(current);
        }
        return current;
    }

    /**
     * Adds data to a new node by recursively searching
     * the tree for appropriate location to add node
     * @param data the data to be added
     * @param current the current node being examined
     * @return the new node that has been added,
     * or the current node if nothing added
     */
    private AVLNode<T> addr(T data, AVLNode<T> current) {
        if (current == null) {
            AVLNode<T> newnode = new AVLNode<>(data);
            newnode.setHeight(0);
            newnode.setBalanceFactor(0);
            size++;
            return newnode;
        } else {
            if (current.getData().compareTo(data) < 0) {
                current.setRight(addr(data, current.getRight()));
            } else if (current.getData().compareTo(data) > 0) {
                current.setLeft(addr(data, current.getLeft()));
            }
            current.setHeight(nodeHeight(current));
            current.setBalanceFactor(nodeBF(current));

            current = balanceTree(current);

            current.setHeight(nodeHeight(current));
            current.setBalanceFactor(nodeBF(current));
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
     * @param dummy the node that will hold the matching data
     * @return the node containing the data
     */
    private AVLNode<T> remover(T data, AVLNode<T> current, AVLNode<T> dummy) {
        if (current == null) {
            throw new NoSuchElementException("Data not found");
        } else {
            if (current.getData().equals(data)) {
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
                current.setRight(remover(data, current.getRight(), dummy));
            } else if (current.getData().compareTo(data) > 0) {
                current.setLeft(remover(data, current.getLeft(), dummy));
            }
            current.setHeight(nodeHeight(current));
            current.setBalanceFactor(nodeBF(current));

            current = balanceTree(current);

            current.setHeight(nodeHeight(current));
            current.setBalanceFactor(nodeBF(current));
            return current;
        }
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data");
        }
        AVLNode<T> dummy = new AVLNode<T>(null);
        remover(data, root, dummy);
        if (dummy.getData() == null) {
            throw new NoSuchElementException("Element is not in the tree");
        } else {
            return dummy.getData();
        }
    }

    /**
     * searches for data in BST, returns data from tree if found
     * @param data the data to be retrieved from the BST
     * @param current the node currently examined
     * @return the data from the BST if found
     */
    private T getr(T data, AVLNode<T> current) {
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
    private boolean containsr(T data, AVLNode<T> current) {
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
    private List<T> preorderr(AVLNode<T> current, ArrayList<T> preorderList) {
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
    private List<T> postorderr(AVLNode<T> current, ArrayList<T> postorderList) {
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
    private List<T> inorderr(AVLNode<T> current, ArrayList<T> inorderList) {
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
    private List<T> levelorderr(AVLNode<T> current,
                                ArrayList<T> levelorderList) {
        Queue<AVLNode<T>> levelQueue = new LinkedList<>();
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

    @Override
    public int height() {
        if (size == 0) {
            return -1;
        } else {
            return root.getHeight();
        }
    }
    
    @Override
    public AVLNode<T> getRoot() {
        // DO NOT EDIT THIS METHOD!
        return root;
    }
}
