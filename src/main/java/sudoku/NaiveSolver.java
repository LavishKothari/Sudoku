package sudoku;

import java.util.List;

public class NaiveSolver {
	public static boolean solve(Sudoku sudoku) {
		return solveHelper(sudoku, 0);
	}

	private static boolean solveHelper(Sudoku sudoku, int currentCell) {
		if (currentCell == sudoku.getDimensionOfGrid() * sudoku.getDimensionOfGrid() || sudoku.isSolved()) {
			return true;
		}
		int row = currentCell / sudoku.getDimensionOfGrid();
		int col = currentCell % sudoku.getDimensionOfGrid();

		if (sudoku.getCellValue(row, col) != GridUtils.EMPTY_CELL)
			return solveHelper(sudoku, currentCell + 1);

		List<Integer> possibleValues = sudoku.getPossibleValues(row, col);
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
