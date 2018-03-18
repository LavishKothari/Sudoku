package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final public class NaiveSolverWithRanomBacktracking {
	
	private static List<Integer> sequenceList;
	
	private NaiveSolverWithRanomBacktracking() {

	}

	public static boolean solve(Sudoku sudoku) {
		
		sequenceList = new ArrayList<>();
		for(int i=0;i<sudoku.getDimensionOfGrid()*sudoku.getDimensionOfGrid();i++) {
			sequenceList.add(i);
		}
		Collections.shuffle(sequenceList);
		return solveHelper(sudoku, 0);
	}

	private static boolean solveHelper(Sudoku sudoku, int currentIndex) {
		if (currentIndex == sudoku.getDimensionOfGrid() * sudoku.getDimensionOfGrid()) {
			return true;
		}
		int currentCell = sequenceList.get(currentIndex);
		
		int row = currentCell / sudoku.getDimensionOfGrid();
		int col = currentCell % sudoku.getDimensionOfGrid();

		if (sudoku.getCellValue(row, col) != GridUtils.EMPTY_CELL)
			return solveHelper(sudoku, currentCell + 1);

		List<Integer> possibleValues = sudoku.getPossibleValues(row, col);
		// shuffling the list of possible values and then iterating over them
		Collections.shuffle(possibleValues);
		for (final int currentValue : possibleValues) {
			sudoku.setCellValue(row, col, currentValue);
			if (solveHelper(sudoku, currentCell + 1))
				return true;
			// backtracking
			sudoku.setCellValue(row, col, GridUtils.EMPTY_CELL);
		}
		return false;
	}

}
