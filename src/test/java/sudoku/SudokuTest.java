package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SudokuTest {

	private Sudoku sudoku;
	private List<List<Integer>> sudokuGrid;

	private Sudoku uniqueTestSudoku;
	private List<List<Integer>> uniqueTestSudokuGrid;

	@Before
	public void setup() {

		sudokuGrid = new ArrayList<>(9);

		sudokuGrid.add(Arrays.asList(4, 3, 5, 2, 6, 9, 7, 8, 1));
		sudokuGrid.add(Arrays.asList(6, 8, 2, 5, 7, 1, 4, 9, 3));
		sudokuGrid.add(Arrays.asList(1, 9, 7, 8, 3, 4, 5, 6, 2));
		sudokuGrid.add(Arrays.asList(8, 2, 6, 1, 9, 5, 3, 4, 7));
		sudokuGrid.add(Arrays.asList(3, 7, 4, 6, 8, 2, 9, 1, 5));
		sudokuGrid.add(Arrays.asList(9, 5, 1, 7, 4, 3, 6, 2, 8));
		sudokuGrid.add(Arrays.asList(5, 1, 9, 3, 2, 6, 8, 7, 4));
		sudokuGrid.add(Arrays.asList(2, 4, 8, 9, 5, 7, 1, 3, 6));
		sudokuGrid.add(Arrays.asList(7, 6, 3, 4, 1, 8, 2, 5, 9));

		sudoku = new Sudoku(sudokuGrid);

		////////////////////////////////////

		uniqueTestSudokuGrid = new ArrayList<>(9);

		uniqueTestSudokuGrid.add(Arrays.asList(4, 3, 5, 2, 6, 9, 7, 8, 1));
		uniqueTestSudokuGrid.add(Arrays.asList(6, 8, 2, 5, 7, 1, 4, 9, 3));
		uniqueTestSudokuGrid.add(Arrays.asList(1, 9, 7, 8, 3, 4, 5, 6, 2));
		uniqueTestSudokuGrid.add(Arrays.asList(8, 2, 6, 1, 9, 5, 3, 0, 0));
		uniqueTestSudokuGrid.add(Arrays.asList(3, 7, 4, 6, 8, 2, 9, 1, 5));
		uniqueTestSudokuGrid.add(Arrays.asList(9, 5, 1, 7, 4, 3, 6, 2, 8));
		uniqueTestSudokuGrid.add(Arrays.asList(5, 1, 9, 3, 2, 6, 8, 0, 0));
		uniqueTestSudokuGrid.add(Arrays.asList(2, 4, 8, 9, 5, 7, 1, 3, 6));
		uniqueTestSudokuGrid.add(Arrays.asList(7, 6, 3, 4, 1, 8, 2, 5, 9));

		uniqueTestSudoku = new Sudoku(uniqueTestSudokuGrid);

	}

	@Test
	public void solvedAndValidTest() {
		Assert.assertTrue(sudoku.isSolved());
		Assert.assertTrue(sudoku.isValid());
	}

	@Test
	public void hasUniqueSolutionTest() {
		Assert.assertFalse(uniqueTestSudoku.hasUniqueSolution());
	}
}
