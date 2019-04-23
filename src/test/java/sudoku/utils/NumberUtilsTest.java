package sudoku.utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void isPerfectSquareTest() {
        Assert.assertTrue(NumberUtils.isPerfectSquare(25));
        Assert.assertTrue(NumberUtils.isPerfectSquare(1));
        Assert.assertTrue(NumberUtils.isPerfectSquare(4));
        Assert.assertTrue(NumberUtils.isPerfectSquare(100));
        Assert.assertTrue(NumberUtils.isPerfectSquare(1000000));

        Assert.assertFalse(NumberUtils.isPerfectSquare(3));
        Assert.assertFalse(NumberUtils.isPerfectSquare(2));
        Assert.assertFalse(NumberUtils.isPerfectSquare(5));
        Assert.assertFalse(NumberUtils.isPerfectSquare(24));
        Assert.assertFalse(NumberUtils.isPerfectSquare(99));
        Assert.assertFalse(NumberUtils.isPerfectSquare(999999));
    }

    @Test
    public void getSquareRootTest() {
        Assert.assertEquals(5, NumberUtils.getSqureRoot(25));
        Assert.assertEquals(1, NumberUtils.getSqureRoot(1));
        Assert.assertEquals(2, NumberUtils.getSqureRoot(4));
        Assert.assertEquals(10, NumberUtils.getSqureRoot(100));
        Assert.assertEquals(10000, NumberUtils.getSqureRoot(100000000));
    }

    @Test
    public void factorialTest() {
        Assert.assertEquals(24, NumberUtils.factorial(4));
        Assert.assertEquals(1, NumberUtils.factorial(0));
        Assert.assertEquals(1, NumberUtils.factorial(1));
        Assert.assertEquals(2, NumberUtils.factorial(2));
        Assert.assertEquals(3628800, NumberUtils.factorial(10));
    }

}
