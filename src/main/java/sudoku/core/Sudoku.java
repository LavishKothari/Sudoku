package sudoku.core;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import sudoku.utils.GridUtils;
import sudoku.utils.NumberUtils;

public class Sudoku {

	private final static Logger logger = Logger.getLogger(Sudoku.class);

	private final List<List<Integer>> grid;
	/**
	 * The functionality corresponding to the cachedSolvedGrid is to be implemented.
	 */
	// private final List<List<Integer>> cachedSolvedGrid;
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
	 *         number of possibilities. This method also returns -1 when there is no
	 *         possibility available to be filled in even if the cell is empty. This
	 *         situation can arise when the sudoku is not correctly filled and at
	 *         the current cell you have no choice of digit to be filled in.
	 * 
	 *         For example, in a 9x9 sudoku grid the cells are indexed from 0 to 80
	 */
	public int getCellWithLeastPossibility() {
		int counter = 0, minCounter = -1, minListSize = Integer.MAX_VALUE;
		for (int i = 0; i < dimensionOfGrid; i++) {
			for (int j = 0; j < dimensionOfGrid; j++) {
				int currentListSize = getPossibleValues(i, j).size();
				if (currentListSize > 0 && currentListSize < minListSize) {
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
	 * @return Returns a list of possibilities that can be filled in this cell. If
	 *         the cell is already filled returns an empty list.
	 */
	public List<Integer> getPossibleValues(int row, int col) {
		if (this.getCellValue(row, col) != GridUtils.EMPTY_CELL)
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

	/**
	 * this function don't alters the original sudoku
	 * 
	 * @return <code>true</code> if this sudoku has a unique solution
	 */
	public boolean hasUniqueSolution() {
		Sudoku tempSudoku = getClonedSudoku();
		int solutions = uniqueSolutionDecider(tempSudoku, tempSudoku.getCellWithLeastPossibility());
		return solutions == 1;
	}

	public Sudoku getClonedSudoku() {
		List<List<Integer>> tempGrid = GridUtils.getClonedGrid(grid);
		return new Sudoku(tempGrid);
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
		if (currentCellNumber == -1 && tempSudoku.isSolved()) {
			logger.info("One possible solution = \n" + tempSudoku);
			return 1;
		} else if (currentCellNumber == -1) {
			/*
			 * definitely currentCellNumber is -1 because of some inconsistent state in
			 * sudoku that occurred during backtracking
			 */
			return 0;
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
		return GridUtils.toString(grid);
	}

	/**
	 * The following method generates a random completely filled valid sudoku.
	 * 
	 * @param n,
	 *            the dimension of grid
	 * @return
	 */
	public static Sudoku generateRandomCompletelyFilledSudoku(int n) {
		if (!NumberUtils.isPerfectSquare(n)) {
			throw new IllegalArgumentException("The dimension of the sudoku grid should be a perfect square");
		}
		List<List<Integer>> grid = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			grid.add(new ArrayList<Integer>(n));
			for (int j = 0; j < n; j++) {
				grid.get(i).add(GridUtils.EMPTY_CELL);
			}
		}
		Sudoku sudoku = new Sudoku(grid);
		while (!sudoku.isSolved()) {
			new CustomSudokuSolver(sudoku).alwaysCalculatingCellWithLeastPossibility(true).randomize(true)
					.selectInitialOptimalCellOrderingList().backtrackingTimeOut(1000).solve();
		}
		return sudoku;
	}

	/**
	 * This method returns a partially filled sudoku
	 * 
	 * @param dimension
	 * @return
	 */
	public static Sudoku getPartiallyFilledSudokuPuzzle(int dimension) {
		Sudoku s = Sudoku.generateRandomCompletelyFilledSudoku(dimension);
		List<Integer> randomIndices = NumberUtils.getShuffledList(0, dimension * dimension - 1);

		for (final Integer currentRandomIndex : randomIndices) {
			int row = currentRandomIndex / dimension;
			int col = currentRandomIndex % dimension;
			Integer toBeReplaced = s.getCellValue(row, col);

			s.setCellValue(row, col, GridUtils.EMPTY_CELL);
			if (!s.hasUniqueSolution()) {
				s.setCellValue(row, col, toBeReplaced);
			}
		}

		return s;
	}

	public void writeSudokuToFile(String fileName, OpenOption... openOptions) throws IOException {
		GridUtils.writeGridToFile(fileName, getGrid(), openOptions);
	}

	public static List<Sudoku> getSudokusFromFile(String fileName) throws IOException {
		List<Sudoku> sudokuList = new ArrayList<>();
		for (List<List<Integer>> currentGrid : GridUtils.getGridsFromFile(fileName)) {
			sudokuList.add(new Sudoku(currentGrid));
		}
		return sudokuList;
	}

	/**
	 * 
	 * @param puzzleFileName
	 * @param solutionFileName
	 * @param timeOut
	 * @return The number of pairs of sudoku puzzles and their solutions
	 *         successfully appended in respective file
	 * @throws IOException
	 */
	public static int appendSudoku(int dimension, String puzzleFileName, String solutionFileName, long timeOut)
			throws IOException {
		long startTime = System.currentTimeMillis();
		int counter = 0;
		while (System.currentTimeMillis() - startTime < timeOut) {
			Sudoku s = Sudoku.getPartiallyFilledSudokuPuzzle(dimension);
			s.writeSudokuToFile(puzzleFileName, StandardOpenOption.APPEND);
			new CustomSudokuSolver(s).alwaysCalculatingCellWithLeastPossibility(true).randomize(true).solve();
			s.writeSudokuToFile(solutionFileName, StandardOpenOption.APPEND);
			counter++;
		}
		return counter;
	}

	/**
	 * 
	 * @param unsolvedSudoku
	 * @return true if this is solved version of unsolvedSudoku, flase otherwise
	 */
	public boolean isSolutionOf(Sudoku unsolvedSudoku) {
		if (this.dimensionOfGrid != unsolvedSudoku.dimensionOfGrid)
			return false;
		for (int i = 0; i < dimensionOfGrid; i++) {
			for (int j = 0; j < dimensionOfGrid; j++) {
				if (unsolvedSudoku.getCellValue(i, j) != GridUtils.EMPTY_CELL
						&& this.getCellValue(i, j) != unsolvedSudoku.getCellValue(i, j)) {
					return false;	
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dimensionOfGrid;
		result = prime * result + dimensionOfInnerGrid;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
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
		Sudoku other = (Sudoku) obj;
		if (dimensionOfGrid != other.dimensionOfGrid)
			return false;
		if (dimensionOfInnerGrid != other.dimensionOfInnerGrid)
			return false;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else {
			for (int i = 0; i < dimensionOfGrid; i++) {
				for (int j = 0; j < dimensionOfGrid; j++) {
					if (this.getCellValue(i, j) != other.getCellValue(i, j))
						return false;
				}
			}
		}
		return true;
	}

}
