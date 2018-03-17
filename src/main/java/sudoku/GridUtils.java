package sudoku;

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
	 * @param maxValue 
	 * @param list
	 * 
	 * @return
	 */
	public static boolean isStrictComplyingList(int maxValue, List<Integer> list) {
		if(list.size()!=maxValue)
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
	 * @param maxValue TODO
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

}
