package sudoku.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Stopwatch;

import sudoku.utils.GridUtils;

public class CustomSudokuSolver {

	private final static Logger logger = Logger.getLogger(CustomSudokuSolver.class);

	/**
	 * This field represents the sudoku to be solved.
	 */
	private Sudoku sudoku;

	/**
	 * This field represents the order/sequence in which the cells of sudoku grid
	 * are picked before applying any guess (and backtracking)
	 * 
	 * The default value of this list is number from 0 to (max number of cells -1)
	 * So for a 9x9 sudoku grid the default value of this list is 0, 1, 2, 3 ... 78,
	 * 79, 80
	 */
	private List<Integer> sequenceList;

	/**
	 * This field represents the decision (true/false) on whether to randomize the
	 * possibilities (that can be filled in a cell) or not.
	 * 
	 * The default value of this field is false
	 */
	private boolean randomize;

	/**
	 * This field, when true, signifies that just before the recursive call the
	 * solver should recalculate the cell number that has least number of
	 * possibilities that can be filled in this cell.
	 */
	private boolean alwaysCalculatingCellWithLeastPossibility;

	/**
	 * This field represents the timeout time when searching for a solution (or
	 * backtracking) is stopped. A value of 0 (default) indicates indefinite
	 * processing
	 */
	private long backtrackingTimeOut;

	/**
	 * This constructor represents the configuration related a very naive sudoku
	 * solver. This don't include any concept of randomization while solving
	 * 
	 * @param sudoku
	 */
	public CustomSudokuSolver(Sudoku sudoku) {
		this.sudoku = sudoku;
		sequenceList = new ArrayList<>();
		for (int i = 0; i < sudoku.getDimensionOfGrid() * sudoku.getDimensionOfGrid(); i++) {
			this.sequenceList.add(i);
		}
		this.randomize = false;
	}

	public CustomSudokuSolver randomize(boolean randomize) {
		this.randomize = randomize;
		return this;
	}

	public CustomSudokuSolver alwaysCalculatingCellWithLeastPossibility(
			boolean alwaysCalculatingCellWithLeastPossibility) {
		this.alwaysCalculatingCellWithLeastPossibility = alwaysCalculatingCellWithLeastPossibility;
		return this;
	}

	public CustomSudokuSolver sequenceList(List<Integer> sequenceList) {
		this.sequenceList = sequenceList;
		return this;
	}

	public CustomSudokuSolver backtrackingTimeOut(long backtrackingTimeOut) {
		this.backtrackingTimeOut = backtrackingTimeOut;
		return this;
	}

	/**
	 * this method assigns the sequenceList to a list which is optimal in terms of
	 * (greedily) minimum number of back tracking calls.
	 * 
	 * @return
	 */
	public CustomSudokuSolver selectInitialOptimalCellOrderingList() {
		List<List<Integer>> buckets = new ArrayList<>(10);

		for (int i = 0; i <= 9; i++) {
			buckets.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < sudoku.getDimensionOfGrid(); i++) {
			for (int j = 0; j < sudoku.getDimensionOfGrid(); j++) {
				List<Integer> currentList = sudoku.getPossibleValues(i, j);
				buckets.get(currentList.size()).add(i * sudoku.getDimensionOfGrid() + j);
			}
		}

		sequenceList = new ArrayList<>();
		for (int i = 0; i < buckets.size(); i++) {
			for (int j = 0; j < buckets.get(i).size(); j++) {
				sequenceList.add(buckets.get(i).get(j));
			}
		}

		return this;
	}

	/**
	 * 
	 * @return Returns true if the sudoku is solved It may be possible that because
	 *         of backtrackingTimeOut, the sudoku is not solved even if the solution
	 *         is possible. If the return value is false, then the original sudoku
	 *         is intact and the values already filled in at the starting of this
	 *         method are not altered at all.
	 */
	public boolean solve() {

//		if(sudoku.getCachedSolvedGrid() != null) {
//			sudoku.setGrid(GridUtils.getClonedGrid(sudoku.getCachedSolvedGrid()));
//			return true;
//		}
		
		logger.info("\n================================   Solver Report ================================ ");
		logger.info(this);
		long startTimer = System.currentTimeMillis();

		Sudoku tempSudoku = sudoku.getClonedSudoku();
		boolean solved = solveHelper(tempSudoku, 0, startTimer);

		if (solved) {
			long endTimer = System.currentTimeMillis();
			logger.info("finished solving. The solved sudoku is:\n" + tempSudoku);
			logger.info("Exceution Time is: " + (endTimer - startTimer) + " ms");
			logger.info("\n================================ Solver Report Ends ================================");
		}
		if (solved) {
			for (int i = 0; i < sudoku.getDimensionOfGrid(); i++) {
				for (int j = 0; j < sudoku.getDimensionOfGrid(); j++) {
					int value = tempSudoku.getCellValue(i, j);
					sudoku.setCellValue(i, j, value);
				}
			}
		}
		return solved;

	}

	private boolean solveHelper(Sudoku currentSudoku, int currentIndex, long startTimer) {
		if (backtrackingTimeOut != 0 &&  System.currentTimeMillis() - startTimer > backtrackingTimeOut) {
			return false;
		}
		if (currentIndex == currentSudoku.getDimensionOfGrid() * currentSudoku.getDimensionOfGrid()) {
			return true;
		}
		int currentCell = sequenceList.get(currentIndex);

		int row = currentCell / currentSudoku.getDimensionOfGrid();
		int col = currentCell % currentSudoku.getDimensionOfGrid();

		if (currentSudoku.getCellValue(row, col) != GridUtils.EMPTY_CELL)
			return solveHelper(currentSudoku, currentIndex + 1, startTimer);

		List<Integer> possibleValues = currentSudoku.getPossibleValues(row, col);
		if (randomize) {
			// shuffling the list of possible values and then iterating over them
			Collections.shuffle(possibleValues);
		}
		for (final int currentValue : possibleValues) {
			currentSudoku.setCellValue(row, col, currentValue);
			/*
			 * The following if basically for updating the sequence list so that it now
			 * reflects the optimal sequence at this moment.
			 */
			if (alwaysCalculatingCellWithLeastPossibility
					&& currentIndex + 1 < currentSudoku.getDimensionOfGrid() * currentSudoku.getDimensionOfGrid()) {
				int nextIndexShouldBe = currentSudoku.getCellWithLeastPossibility();
				int swapIndex1 = sequenceList.indexOf(nextIndexShouldBe);
				int swapIndex2 = currentIndex + 1;
				/*
				 * swapIndex1 can be less that 0 when the sudoku is completely filled and
				 * currentSudoku.getCellWithLeastPossibility() returns -1
				 */
				if (swapIndex1 > 0)
					Collections.swap(sequenceList, swapIndex1, swapIndex2);
			}
			if (solveHelper(currentSudoku, currentIndex + 1, startTimer))
				return true;
			// backtracking
			currentSudoku.setCellValue(row, col, GridUtils.EMPTY_CELL);
		}
		return false;
	}

	@Override
	public String toString() {
		return "CustomSudokuSolver [sudoku=\n" + sudoku + "\nsequenceList=\n" + sequenceList + "\nrandomize=\n"
				+ randomize + "\nalwaysCalculatingCellWithLeastPossibility=\n"
				+ alwaysCalculatingCellWithLeastPossibility + "]";
	}

}
