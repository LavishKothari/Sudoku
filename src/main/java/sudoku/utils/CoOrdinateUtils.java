package sudoku.utils;

import java.util.ArrayList;
import java.util.List;

public class CoOrdinateUtils {

    private static final List<List<CoOrdinate>> rowCoOrdinates9;
    private static final List<List<CoOrdinate>> colCoOrdinates9;
    private static final List<List<CoOrdinate>> innerGridCoOrdinates9;
    private static boolean initialized = false;

    static {
        rowCoOrdinates9 = new ArrayList<>(9);
        colCoOrdinates9 = new ArrayList<>(9);
        innerGridCoOrdinates9 = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            rowCoOrdinates9.add(getRowCoOrdinates(9, i));
            colCoOrdinates9.add(getColCoOrdinates(9, i));
            innerGridCoOrdinates9.add(getInnerGridCoOrdinates(9, i));
        }
        initialized = true;
    }

    private CoOrdinateUtils() {

    }

    public static List<CoOrdinate> getRowCoOrdinates(int dimension, int row) {
        if (dimension == 9 && initialized) {
            return rowCoOrdinates9.get(row);
        }
        List<CoOrdinate> result = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
            result.add(CoOrdinate.getCoOrdinate(row, i));
        return result;
    }

    public static List<CoOrdinate> getColCoOrdinates(int dimension, int col) {
        if (dimension == 9 && initialized) {
            return colCoOrdinates9.get(col);
        }
        List<CoOrdinate> result = new ArrayList<>();
        for (int i = 0; i < dimension; i++)
            result.add(CoOrdinate.getCoOrdinate(i, col));
        return result;
    }

    /**
     * @param dimension
     * @param innerGridNumber is the number of inner grid - starting from 0.
     * @return
     */
    public static List<CoOrdinate> getInnerGridCoOrdinates(int dimension, int innerGridNumber) {
        if (dimension == 9 && initialized) {
            return innerGridCoOrdinates9.get(innerGridNumber);
        }
        int dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimension);

        int Y = innerGridNumber % dimensionOfInnerGrid;
        int X = innerGridNumber / dimensionOfInnerGrid;
        int startXIndex = X * dimensionOfInnerGrid;
        int startYIndex = Y * dimensionOfInnerGrid;

        List<CoOrdinate> result = new ArrayList<>();
        for (int i = startXIndex; i < startXIndex + dimensionOfInnerGrid; i++) {
            for (int j = startYIndex; j < startYIndex + dimensionOfInnerGrid; j++) {
                result.add(CoOrdinate.getCoOrdinate(i, j));
            }
        }

        return result;
    }
}
