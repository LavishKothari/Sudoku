package sudoku.utils;

import java.util.ArrayList;
import java.util.List;

public class CoOrdinate implements Comparable<CoOrdinate> {
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	private final int x, y;

	public static final CoOrdinate DUMMY = new CoOrdinate(-1, -1);

	public CoOrdinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public static List<CoOrdinate> getCoOrdinateListOfRow(int row, int dimensionOfGrid) {
		List<CoOrdinate> result = new ArrayList<>();
		for (int i = 0; i < dimensionOfGrid; i++) {
			result.add(new CoOrdinate(row, i));
		}
		return result;
	}

	public static List<CoOrdinate> getCoOrdinateListOfColumn(int col, int dimensionOfGrid) {
		List<CoOrdinate> result = new ArrayList<>();
		for (int i = 0; i < dimensionOfGrid; i++) {
			result.add(new CoOrdinate(i, col));
		}
		return result;
	}

	public static List<CoOrdinate> getCoOrdinateListOfInnerGrid(int innerGridNumber, int dimensionOfGrid) {
		if (NumberUtils.isPerfectSquare(dimensionOfGrid)) {
			List<CoOrdinate> result = new ArrayList<>();

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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoOrdinate other = (CoOrdinate) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(CoOrdinate o) {
		if (this.x < o.x) {
			return -1;
		} else if (this.x == o.x) {
			if (this.y < o.y) {
				return -1;
			} else if (this.y == o.y) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 1;
		}
	}

}
