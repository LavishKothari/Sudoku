package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

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

	public CustomSudokuSolver sequenceList(List<Integer> sequenceList) {
		this.sequenceList = sequenceList;
		return this;
	}

	public boolean solve() {
		logger.info("solving the following sudoku: \n" + sudoku);
		boolean solved = solveHelper(0);
		logger.info("finished solving the following sudoku: \n" + sudoku);
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
			if (solveHelper(currentIndex + 1))
				return true;
			// backtracking
			sudoku.setCellValue(row, col, GridUtils.EMPTY_CELL);
		}
		return false;
	}

}
