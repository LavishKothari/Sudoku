package sudoku.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CoOrdinate implements Comparable<CoOrdinate> {
    public static final Comparator<CoOrdinate> XY_COMPARATOR =
            Comparator.comparing(CoOrdinate::getX)
                    .thenComparing(CoOrdinate::getY);
    public static final CoOrdinate DUMMY = new CoOrdinate(-1, -1);
    private final int x, y;

    public CoOrdinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static List<CoOrdinate> getCoOrdinateListOfRow(int row, int dimensionOfGrid) {
        List<CoOrdinate> result = new ArrayList<>(dimensionOfGrid);
        for (int i = 0; i < dimensionOfGrid; i++) {
            result.add(new CoOrdinate(row, i));
        }
        return result;
    }

    public static List<CoOrdinate> getCoOrdinateListOfColumn(int col, int dimensionOfGrid) {
        List<CoOrdinate> result = new ArrayList<>(dimensionOfGrid);
        for (int i = 0; i < dimensionOfGrid; i++) {
            result.add(new CoOrdinate(i, col));
        }
        return result;
    }

    public static List<CoOrdinate> getCoOrdinateListOfInnerGrid(int innerGridNumber, int dimensionOfGrid) {
        if (NumberUtils.isPerfectSquare(dimensionOfGrid)) {
            List<CoOrdinate> result = new ArrayList<>(dimensionOfGrid);

            int dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);

            int Y = innerGridNumber % dimensionOfInnerGrid;
            int X = innerGridNumber / dimensionOfInnerGrid;
            int startXIndex = X * dimensionOfInnerGrid;
            int startYIndex = Y * dimensionOfInnerGrid;

            for (int i = startXIndex; i < startXIndex + dimensionOfInnerGrid; i++) {
                for (int j = startYIndex; j < startYIndex + dimensionOfInnerGrid; j++) {
                    result.add(new CoOrdinate(i, j));
                }
            }
            return result;
        } else {
            throw new RuntimeException("The dimension of grid should be a perfect square!");
        }
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CoOrdinate))
            return false;
        CoOrdinate other = (CoOrdinate) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int compareTo(CoOrdinate o) {
        return XY_COMPARATOR.compare(this, o);
    }

}
