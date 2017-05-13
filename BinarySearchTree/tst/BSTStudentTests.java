import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Sample JUnit test cases for BST.
 *
 * @version 1.0
 * @author CS 1332 TAs
 */
public class BSTStudentTests {
    private BSTInterface<Integer> bst;
    public static final int TIMEOUT = 200;

    @Before
    public void setup() {
        bst = new BST<Integer>();
    }

    @Test(timeout = TIMEOUT)
    public void testAdd() {
        assertEquals(0, bst.size());

        bst.add(2);
        bst.add(1);
        bst.add(3);
        bst.add(4);
        bst.add(1);

        assertEquals(4, bst.size());
        assertEquals((Integer) 2, bst.getRoot().getData());
        assertEquals((Integer) 1, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 3, bst.getRoot().getRight().getData());
        assertEquals((Integer) 4, bst.getRoot().getRight().getRight().getData());
    }

    @Test(timeout = TIMEOUT)
    public void testRemove() {
        assertEquals(0, bst.size());

        bst.add(2);
        bst.add(1);
        bst.add(3);
        bst.add(4);
        bst.add(5);

        assertEquals((Integer) 3, bst.remove(3));
        assertEquals(4, bst.size());
        assertEquals((Integer) 2, bst.getRoot().getData());
        assertEquals((Integer) 1, bst.getRoot().getLeft().getData());
        assertEquals((Integer) 4, bst.getRoot().getRight().getData());
        assertEquals((Integer) 5, bst.getRoot().getRight()
                .getRight().getData());
    }

    @Test(timeout = TIMEOUT)
    public void testGet() {
        bst.add(24);
        bst.add(1);
        bst.add(7);
        bst.add(12);
        bst.add(94);
        bst.add(58);
        bst.add(73);

        assertTrue(bst.contains(58));
        assertEquals((Integer) 58, bst.get(58));
        assertTrue(bst.contains(12));
        assertEquals((Integer) 12, bst.get(12));
        assertTrue(bst.contains(94));
        assertEquals((Integer) 94, bst.get(94));
        assertTrue(bst.contains(24));
        assertEquals((Integer) 24, bst.get(24));
    }

    @Test(timeout = TIMEOUT)
    public void testGetDifferent() {
        Integer testingInteger = new Integer(12);

        bst.add(24);
        bst.add(1);
        bst.add(7);
        bst.add(testingInteger);
        bst.add(94);
        bst.add(58);
        bst.add(73);

        assertSame(testingInteger, bst.get(new Integer(12)));
    }

    @Test(timeout = TIMEOUT)
    public void testLevelorder() {
        bst.add(24);
        bst.add(1);
        bst.add(7);
        bst.add(12);
        bst.add(94);
        bst.add(58);
        bst.add(73);
        bst.add(77);
        bst.add(68);

        List<Integer> levelorder = new ArrayList<>();
        levelorder.add(24);
        levelorder.add(1);
        levelorder.add(94);
        levelorder.add(7);
        levelorder.add(58);
        levelorder.add(12);
        levelorder.add(73);
        levelorder.add(68);
        levelorder.add(77);

        assertEquals(levelorder, bst.levelorder());
    }

    @Test(timeout = TIMEOUT)
    public void testpreorder() {
        bst.add(56);
        bst.add(23);
        bst.add(10);
        bst.add(40);
        bst.add(60);
        bst.add(75);

        List<Integer> preorder = new ArrayList<>();
        preorder.add(56);
        preorder.add(23);
        preorder.add(10);
        preorder.add(40);
        preorder.add(60);
        preorder.add(75);

        assertEquals(preorder, bst.preorder());
    }

    @Test(timeout = TIMEOUT)
    public void testpostorder() {
        bst.add(56);
        bst.add(23);
        bst.add(10);
        bst.add(40);
        bst.add(60);
        bst.add(75);

        List<Integer> postorder = new ArrayList<>();
        postorder.add(10);
        postorder.add(40);
        postorder.add(23);
        postorder.add(75);
        postorder.add(60);
        postorder.add(56);

        assertEquals(postorder, bst.postorder());
    }

    @Test(timeout = TIMEOUT)
    public void testinorder() {
        bst.add(56);
        bst.add(23);
        bst.add(10);
        bst.add(40);
        bst.add(60);
        bst.add(75);

        List<Integer> inorder = new ArrayList<>();
        inorder.add(10);
        inorder.add(23);
        inorder.add(40);
        inorder.add(56);
        inorder.add(60);
        inorder.add(75);

        assertEquals(inorder, bst.inorder());
    }

    @Test(timeout = TIMEOUT)
    public void testheight() {
        bst.add(24);
        bst.add(1);
        bst.add(94);
        bst.add(7);
        bst.add(58);
        bst.add(12);
        bst.add(73);
        bst.add(68);
        bst.add(77);

        assertSame(4, bst.height());
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void addExpectException() {
        bst.add(null);
    }


    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void getEmpty() {
        bst.get(20);
    }
}
