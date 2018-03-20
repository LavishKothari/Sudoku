package sudoku;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GridUtils {

	public static final int EMPTY_CELL = 0;

	public static List<Integer> getListOfRow(List<List<Integer>> grid, int rowNumber) {
		List<Integer> list = new ArrayList<>();
		grid.get(rowNumber).stream().filter(e -> e != GridUtils.EMPTY_CELL).forEach(e -> list.add(e));
		return list;
	}

	public static List<Integer> getListOfColumn(List<List<Integer>> grid, int columnNumber) {
		List<Integer> resultList = new ArrayList<>();
		grid.forEach(currentList -> resultList.add(currentList.get(columnNumber)));
		return resultList.stream().filter(e -> e != GridUtils.EMPTY_CELL).collect(Collectors.toList());
	}

	public static List<Integer> getListOfInnerGrid(List<List<Integer>> grid, int innerGridNumber) {
		int dimensionOfGrid = grid.size();
		int dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);

		int Y = innerGridNumber % dimensionOfInnerGrid;
		int X = innerGridNumber / dimensionOfInnerGrid;
		int startXIndex = X * dimensionOfInnerGrid;
		int startYIndex = Y * dimensionOfInnerGrid;

		List<Integer> resultList = new ArrayList<>();
		for (int i = startXIndex; i < startXIndex + dimensionOfInnerGrid; i++) {
			for (int j = startYIndex; j < startYIndex + dimensionOfInnerGrid; j++) {
				if (grid.get(i).get(j) != GridUtils.EMPTY_CELL)
					resultList.add(grid.get(i).get(j));
			}
		}
		return resultList;
	}

	/**
	 * This method checks if all the elements from 1 to list.size() are present in
	 * the list (possibly in shuffled order) and there are no repeated elements in
	 * the list
	 * 
	 * If any element is missing i.e.. is Sudoku.EMPTY_CELL then this method return
	 * false
	 * 
	 * @param maxValue
	 * @param list
	 * 
	 * @return
	 */
	public static boolean isStrictComplyingList(int maxValue, List<Integer> list) {
		if (list.size() != maxValue)
			return false;

		List<Integer> flag = new ArrayList<>(list.size());
		while (flag.size() < list.size())
			flag.add(0);
		/*
		 * applying filter in the next line to avoid any index out of bounds.
		 */
		list.stream().filter(e -> e > 0).forEach(e -> flag.set(e - 1, flag.get(e - 1) + 1));
		return flag.stream().allMatch(e -> e == 1);
	}

	/**
	 * This method checks if there are no repeated elements present in the list
	 * 
	 * If any element is missing i.e.. is Sudoku.EMPTY_CELL then that element is
	 * ignored and has no effect on the return value of this method
	 * 
	 * @param maxValue
	 *            TODO
	 * @param list
	 * 
	 * @return
	 */
	public static boolean isWeakComplyingList(int maxValue, List<Integer> list) {
		List<Integer> flag = new ArrayList<>(list.size());
		while (flag.size() < maxValue)
			flag.add(0);
		list.stream().filter(e -> e > 0).forEach(e -> flag.set(e - 1, flag.get(e - 1) + 1));
		return flag.stream().allMatch(e -> e == 0 || e == 1);
	}

	/**
	 * this function takes the grid along with row number and column number and
	 * returns the inner grid number in which this particular cell lies.
	 * 
	 * Note that the inner grids are numbered starting from 0.
	 * 
	 * @param grid
	 * @param row
	 * @param col
	 * @return
	 */
	public static int getInnerGridNumber(int dimensionOfGrid, int row, int col) {
		int dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);
		return (row / dimensionOfInnerGrid) * dimensionOfInnerGrid + col / dimensionOfInnerGrid;
	}

	/**
	 * This method returns a list of all the grids that are constructible from the
	 * given file
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static List<List<List<Integer>>> getGridsFromFile(String fileName) throws IOException {
		List<List<List<Integer>>> grids = new ArrayList<>();

		URL url = GridUtils.class.getClassLoader().getResource(fileName);
		for (String currentString : Files.readAllLines(Paths.get(url.getPath()))) {
			grids.add(getGridFromStringFormat(currentString));
		}
		return grids;
	}

	/**
	 * This method is used to get the grid represented by the string. <br>
	 * For example <br>
	 * ....7..2.8.......6.1.2.5...9.54....8.........3....85.1...3.2.8.4.......9.7..6....
	 * <br>
	 * is <br>
	 * 0 0 0 0 7 0 0 2 0<br>
	 * 8 0 0 0 0 0 0 0 6<br>
	 * 0 1 0 2 0 5 0 0 0<br>
	 * 9 0 5 4 0 0 0 0 8<br>
	 * 0 0 0 0 0 0 0 0 0<br>
	 * 3 0 0 0 0 8 5 0 1<br>
	 * 0 0 0 3 0 2 0 8 0<br>
	 * 4 0 0 0 0 0 0 0 9<br>
	 * 0 7 0 0 6 0 0 0 0<br>
	 */
	public static List<List<Integer>> getGridFromStringFormat(String str) {
		int totalCells = str.length();
		if (!NumberUtils.isPerfectSquare(totalCells))
			throw new IllegalArgumentException("The String is not convertible to a sudoku");

		List<List<Integer>> sudokuGrid = new ArrayList<>(9);
		int dimension = (int) NumberUtils.getSqureRoot(totalCells);

		for (int i = 0; i < dimension; i++) {
			sudokuGrid.add(new ArrayList<>(9));
			for (int j = 0; j < dimension; j++) {
				sudokuGrid.get(i).add(getIthInteger(str, i * dimension + j));
			}
		}

		Sudoku s = new Sudoku(sudokuGrid);
		if (!s.isValid())
			throw new IllegalArgumentException("The String is not convertible to a sudoku");

		return sudokuGrid;
	}

	public static int getIthInteger(String str, int i) {
		char c = str.charAt(i);
		if (c == '.')
			return 0;
		return (int) c - (int) '0';
	}

	public static List<List<Integer>> getClonedGrid(final List<List<Integer>> grid) {
		List<List<Integer>> result = new ArrayList<>(grid.size());
		for (int i = 0; i < grid.size(); i++) {
			result.add(new ArrayList<Integer>(grid.get(i).size()));
			for (int j = 0; j < grid.size(); j++) {
				result.get(i).add(grid.get(i).get(j));
			}
		}
		return result;
	}

}
