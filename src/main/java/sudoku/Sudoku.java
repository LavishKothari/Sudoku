package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class Sudoku {

	private final static Logger logger = Logger.getLogger(Sudoku.class);

	private final List<List<Integer>> grid;
	private final int dimensionOfGrid;
	private final int dimensionOfInnerGrid;

	public int getDimensionOfGrid() {
		return dimensionOfGrid;
	}

	/**
	 * This getter is package private as it can only be accessed by solvers in the
	 * package and is not available to end-client (to avoid mutations in the actual
	 * grid)
	 * 
	 * @return
	 */
	List<List<Integer>> getGrid() {
		return grid;
	}

	public Sudoku(List<List<Integer>> grid) {
		super();
		this.grid = grid;
		dimensionOfGrid = grid.size();
		dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);
	}

	public int getCellValue(int row, int col) {
		return grid.get(row).get(col);
	}

	/**
	 * This method checks if a sudoku is completely and correctly filled
	 * 
	 * @return
	 */
	public boolean isSolved() {
		for (int i = 0; i < dimensionOfGrid; i++) {
			if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfRow(grid, i)))
				return false;
			if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfColumn(grid, i)))
				return false;
			if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfInnerGrid(grid, i)))
				return false;
		}
		return true;
	}

	/**
	 * This method ignores any empty cells in sudoku grid and just checks if there
	 * are no violations of standard sudoku game.
	 * 
	 * @return This method returns true if there are no breaking of the rules
	 *         defined by a standard sudoku game.
	 */
	public boolean isValid() {
		for (int i = 0; i < dimensionOfGrid; i++) {
			if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfRow(grid, i)))
				return false;
			if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfColumn(grid, i)))
				return false;
			if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfInnerGrid(grid, i)))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @return The index number of the cell in the whole sudoku grid that has
	 *         minimum number of possibilities that can be filled in that cell. The
	 *         cells are indexed from 0. This method returns -1 when all the cells
	 *         are filled and there is no cell that is empty, so no meaning of least
	 *         number of possibilities
	 * 
	 *         For example, in a 9x9 sudoku grid the cells are indexed from 0 to 80
	 */
	public int getCellWithLeastPossibility() {
		int counter = 0, minCounter = -1, minListSize = Integer.MAX_VALUE;
		for (int i = 0; i < dimensionOfGrid; i++) {
			for (int j = 0; j < dimensionOfGrid; j++) {
				int currentListSize = getPossibleValues(i, j).size();
				if (currentListSize != 0 && currentListSize < minListSize) {
					minListSize = currentListSize;
					minCounter = counter;
					if (minListSize == 1)
						return minCounter;
				}
				counter++;
			}
		}
		return minCounter;
	}

	/**
	 * 
	 * @return The index number of the cell in the whole sudoku grid that has
	 *         exactly one possibility that can be filled in. If there is no such
	 *         cell, -1 is returned. The cells are indexed from 0.
	 * 
	 *         For example, in a 9x9 sudoku grid the cells are indexed from 0 to 80
	 */
	public int getCellWithExactlyOnePossibility() {
		int counter = 0;
		for (int i = 0; i < dimensionOfGrid; i++) {
			for (int j = 0; j < dimensionOfGrid; j++) {
				int currentListSize = getPossibleValues(i, j).size();
				if (currentListSize == 1)
					return counter;

				counter++;
			}
		}
		return -1;
	}

	/**
	 * The following method takes in the coordinates of a cell in sudoku grid and
	 * returns the list of possible values that can be filled in that cell without
	 * violating the standard sudoku rule.
	 * 
	 * If the cell is already filled (not empty) then this method returns list with
	 * one single element that is already present in that cell.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public List<Integer> getPossibleValues(int row, int col) {
		if (grid.get(row).get(col) != GridUtils.EMPTY_CELL)
			return Arrays.asList();

		List<Integer> rowList = GridUtils.getListOfRow(grid, row);
		List<Integer> colList = GridUtils.getListOfColumn(grid, col);

		int innerGridNumber = GridUtils.getInnerGridNumber(dimensionOfGrid, row, col);
		List<Integer> innerGridList = GridUtils.getListOfInnerGrid(grid, innerGridNumber);

		Set<Integer> elements = new HashSet<>();
		elements.addAll(rowList);
		elements.addAll(colList);
		elements.addAll(innerGridList);

		List<Integer> resultList = new ArrayList<>();
		for (int i = 0; i < dimensionOfGrid; i++)
			resultList.add(i + 1);

		return resultList.stream().filter(e -> !elements.contains(e)).collect(Collectors.toList());
	}

	public boolean hasUniqueSolution() {
		List<List<Integer>> tempGrid = GridUtils.getClonedGrid(grid);
		Sudoku tempSudoku = new Sudoku(tempGrid);
		int solutions = uniqueSolutionDecider(tempSudoku, tempSudoku.getCellWithLeastPossibility());
		return solutions == 1;
	}

	/**
	 * 
	 * @param tempSudoku
	 * @param currentCellNumber
	 * @return This function return 1 if the sudoku has a unique solution and it
	 *         returns a value greater than 1 if it has multiple solution and a
	 *         value 0 if it has no solution.
	 */
	private static int uniqueSolutionDecider(Sudoku tempSudoku, int currentCellNumber) {
		if (currentCellNumber == -1) {
			logger.info("One possible solution = \n" + tempSudoku);
			return 1;
		}

		int row = currentCellNumber / tempSudoku.getDimensionOfGrid();
		int col = currentCellNumber % tempSudoku.getDimensionOfGrid();
		List<Integer> possibleValues = tempSudoku.getPossibleValues(row, col);
		Collections.shuffle(possibleValues);
		int solutions = 0;
		for (final Integer currentValue : possibleValues) {
			tempSudoku.setCellValue(row, col, currentValue);

			int nextCellNumber = tempSudoku.getCellWithLeastPossibility();
			solutions += uniqueSolutionDecider(tempSudoku, nextCellNumber);
			if (solutions > 1) {
				return 2;
			}

			tempSudoku.clearCell(row, col);

		}
		// here the value of soutions will either be 0 or 1
		// clearing the cell is an important thing to do before backtracking
		tempSudoku.clearCell(row, col);
		return solutions;

	}

	public void setCellValue(int row, int col, int value) {
		grid.get(row).set(col, value);
	}

	public void clearCell(int row, int col) {
		setCellValue(row, col, GridUtils.EMPTY_CELL);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getDimensionOfGrid(); i++) {
			for (int j = 0; j < getDimensionOfGrid(); j++)
				sb.append(getCellValue(i, j) + " ");
			sb.append('\n');
		}
		return sb.toString();
	}

}
