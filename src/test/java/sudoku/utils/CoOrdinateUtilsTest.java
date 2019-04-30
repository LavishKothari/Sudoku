package sudoku.utils;

import org.junit.Assert;
import org.junit.Test;

public class CoOrdinateUtilsTest {
    @Test
    public void rowTest() {
        CoOrdinate[][] coOrdinates = new CoOrdinate[][]{
                {get(0, 0), get(0, 1), get(0, 2), get(0, 3), get(0, 4), get(0, 5), get(0, 6), get(0, 7), get(0, 8)},
                {get(1, 0), get(1, 1), get(1, 2), get(1, 3), get(1, 4), get(1, 5), get(1, 6), get(1, 7), get(1, 8)},
                {get(2, 0), get(2, 1), get(2, 2), get(2, 3), get(2, 4), get(2, 5), get(2, 6), get(2, 7), get(2, 8)},
                {get(3, 0), get(3, 1), get(3, 2), get(3, 3), get(3, 4), get(3, 5), get(3, 6), get(3, 7), get(3, 8)},
                {get(4, 0), get(4, 1), get(4, 2), get(4, 3), get(4, 4), get(4, 5), get(4, 6), get(4, 7), get(4, 8)},
                {get(5, 0), get(5, 1), get(5, 2), get(5, 3), get(5, 4), get(5, 5), get(5, 6), get(5, 7), get(5, 8)},
                {get(6, 0), get(6, 1), get(6, 2), get(6, 3), get(6, 4), get(6, 5), get(6, 6), get(6, 7), get(6, 8)},
                {get(7, 0), get(7, 1), get(7, 2), get(7, 3), get(7, 4), get(7, 5), get(7, 6), get(7, 7), get(7, 8)},
                {get(8, 0), get(8, 1), get(8, 2), get(8, 3), get(8, 4), get(8, 5), get(8, 6), get(8, 7), get(8, 8)}};
        for (int i = 0; i < 9; i++) {
            int j = 0;
            for (CoOrdinate c : CoOrdinate.getCoOrdinateListOfRow(i, 9)) {
                Assert.assertEquals(coOrdinates[i][j++], c);
            }
        }
    }

    @Test
    public void colTest() {
        CoOrdinate[][] coOrdinates = new CoOrdinate[][]{
                {get(0, 0), get(1, 0), get(2, 0), get(3, 0), get(4, 0), get(5, 0), get(6, 0), get(7, 0), get(8, 0)},
                {get(0, 1), get(1, 1), get(2, 1), get(3, 1), get(4, 1), get(5, 1), get(6, 1), get(7, 1), get(8, 1)},
                {get(0, 2), get(1, 2), get(2, 2), get(3, 2), get(4, 2), get(5, 2), get(6, 2), get(7, 2), get(8, 2)},
                {get(0, 3), get(1, 3), get(2, 3), get(3, 3), get(4, 3), get(5, 3), get(6, 3), get(7, 3), get(8, 3)},
                {get(0, 4), get(1, 4), get(2, 4), get(3, 4), get(4, 4), get(5, 4), get(6, 4), get(7, 4), get(8, 4)},
                {get(0, 5), get(1, 5), get(2, 5), get(3, 5), get(4, 5), get(5, 5), get(6, 5), get(7, 5), get(8, 5)},
                {get(0, 6), get(1, 6), get(2, 6), get(3, 6), get(4, 6), get(5, 6), get(6, 6), get(7, 6), get(8, 6)},
                {get(0, 7), get(1, 7), get(2, 7), get(3, 7), get(4, 7), get(5, 7), get(6, 7), get(7, 7), get(8, 7)},
                {get(0, 8), get(1, 8), get(2, 8), get(3, 8), get(4, 8), get(5, 8), get(6, 8), get(7, 8), get(8, 8)}};
        for (int i = 0; i < 9; i++) {
            int j = 0;
            for (CoOrdinate c : CoOrdinate.getCoOrdinateListOfColumn(i, 9)) {
                Assert.assertEquals(coOrdinates[i][j++], c);
            }
        }
    }


    @Test
    public void innerGridTest() {
        CoOrdinate[][] coOrdinates = new CoOrdinate[][]{
                {get(0, 0), get(0, 1), get(0, 2), get(1, 0), get(1, 1), get(1, 2), get(2, 0), get(2, 1), get(2, 2)},
                {get(0, 3), get(0, 4), get(0, 5), get(1, 3), get(1, 4), get(1, 5), get(2, 3), get(2, 4), get(2, 5)},
                {get(0, 6), get(0, 7), get(0, 8), get(1, 6), get(1, 7), get(1, 8), get(2, 6), get(2, 7), get(2, 8)},
                {get(3, 0), get(3, 1), get(3, 2), get(4, 0), get(4, 1), get(4, 2), get(5, 0), get(5, 1), get(5, 2)},
                {get(3, 3), get(3, 4), get(3, 5), get(4, 3), get(4, 4), get(4, 5), get(5, 3), get(5, 4), get(5, 5)},
                {get(3, 6), get(3, 7), get(3, 8), get(4, 6), get(4, 7), get(4, 8), get(5, 6), get(5, 7), get(5, 8)},
                {get(6, 0), get(6, 1), get(6, 2), get(7, 0), get(7, 1), get(7, 2), get(8, 0), get(8, 1), get(8, 2)},
                {get(6, 3), get(6, 4), get(6, 5), get(7, 3), get(7, 4), get(7, 5), get(8, 3), get(8, 4), get(8, 5)},
                {get(6, 6), get(6, 7), get(6, 8), get(7, 6), get(7, 7), get(7, 8), get(8, 6), get(8, 7), get(8, 8)}};
        for (int i = 0; i < 9; i++) {
            int j = 0;
            for (CoOrdinate c : CoOrdinate.getCoOrdinateListOfInnerGrid(i, 9)) {
                Assert.assertEquals(coOrdinates[i][j++], c);
            }
        }
    }

    private CoOrdinate get(int x, int y) {
        return CoOrdinate.getCoOrdinate(x, y);
    }
}
