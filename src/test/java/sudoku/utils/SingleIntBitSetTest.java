package sudoku.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleIntBitSetTest {
    @Test
    public void test() {
        SingleIntBitSet bitSet = new SingleIntBitSet(10);
        List<Integer> originalList = Arrays.asList(1, 2, 7, 9);
        for (int i : originalList) bitSet.set(i);

        bitSet.set(4);
        bitSet.set(5);
        bitSet.set(6);
        bitSet.clear(4);
        bitSet.clear(5);
        bitSet.clear(6);

        List<Integer> list = new ArrayList<>();
        bitSet.setBitsConsumer(i -> list.add(i));
        Assert.assertEquals(originalList, list);
    }
}
