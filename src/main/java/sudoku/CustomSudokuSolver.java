package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Stopwatch;

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

	/**
	 * this method assigns the sequenceList to a list which is optimal in terms of
	 * (greedily) minimum number of back tracking calls.
	 * 
	 * @return
	 */
	public CustomSudokuSolver selectInitialOptimalCellOrderingList() {
		List<List<Integer>> buckets = new ArrayList<>(9);

		for (int i = 0; i < 9; i++) {
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

	public boolean solve() {

		logger.info("\n================================   Solver Report ================================ ");
		logger.info(this);
		Stopwatch startTimer = Stopwatch.createStarted();

		boolean solved = solveHelper(0);

		Stopwatch endTimer = startTimer.stop();
		logger.info("finished solving. The solved sudoku is:\n" + sudoku);
		logger.info("Exceution Time is: " + endTimer);
		logger.info("\n================================ Solver Report Ends ================================");

		return solved;

	}

	private boolean solveHelper(int currentIndex) {
		if (currentIndex == sudoku.getDimensionOfGrid() * sudoku.getDimensionOfGrid()) {
			return true;
		}
		int currentCell = sequenceList.get(currentIndex);

		int row = currentCell / sudoku.getDimensionOfGrid();
		int col = currentCell % sudoku.getDimensionOfGrid();

		if (sudoku.getCellValue(row, col) != GridUtils.EMPTY_CELL)
			return solveHelper(currentIndex + 1);

		List<Integer> possibleValues = sudoku.getPossibleValues(row, col);
		if (randomize) {
			// shuffling the list of possible values and then iterating over them
			Collections.shuffle(possibleValues);
		}
		for (final int currentValue : possibleValues) {
			sudoku.setCellValue(row, col, currentValue);
			/*
			 * The following if basically for updating the sequence list so that it now
			 * reflects the optimal sequence at this moment.
			 */
			if (alwaysCalculatingCellWithLeastPossibility
					&& currentIndex + 1 < sudoku.getDimensionOfGrid() * sudoku.getDimensionOfGrid()) {
				int nextIndexShouldBe = sudoku.getCellWithLeastPossibility();
				int swapIndex1 = sequenceList.indexOf(nextIndexShouldBe);
				int swapIndex2 = currentIndex + 1;
				/*
				 * swapIndex1 can be less that 0 when the sudoku is completely filled and
				 * sudoku.getCellWithLeastPossibility() returns -1
				 */
				if (swapIndex1 > 0)
					Collections.swap(sequenceList, swapIndex1, swapIndex2);
			}
			if (solveHelper(currentIndex + 1))
				return true;
			// backtracking
			sudoku.setCellValue(row, col, GridUtils.EMPTY_CELL);
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
