package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NaiveSolverTest {

	private Sudoku unsolvedSudoku;
	private List<List<Integer>> unsolvedSudokuGrid;

	private Sudoku solvedSudoku;
	private List<List<Integer>> solvedSudokuGrid;

	@Before
	public void setup() {
		unsolvedSudokuGrid = new ArrayList<>(9);

		unsolvedSudokuGrid.add(Arrays.asList(0, 0, 0, 2, 6, 0, 7, 0, 1));
		unsolvedSudokuGrid.add(Arrays.asList(6, 8, 0, 0, 7, 0, 0, 9, 0));
		unsolvedSudokuGrid.add(Arrays.asList(1, 9, 0, 0, 0, 4, 5, 0, 0));
		unsolvedSudokuGrid.add(Arrays.asList(8, 2, 0, 1, 0, 0, 0, 4, 0));
		unsolvedSudokuGrid.add(Arrays.asList(0, 0, 4, 6, 0, 2, 9, 0, 0));
		unsolvedSudokuGrid.add(Arrays.asList(0, 5, 0, 0, 0, 3, 0, 2, 8));
		unsolvedSudokuGrid.add(Arrays.asList(0, 0, 9, 3, 0, 0, 0, 7, 4));
		unsolvedSudokuGrid.add(Arrays.asList(0, 4, 0, 0, 5, 0, 0, 3, 6));
		unsolvedSudokuGrid.add(Arrays.asList(7, 0, 3, 0, 1, 8, 0, 0, 0));

		unsolvedSudoku = new Sudoku(unsolvedSudokuGrid);
		////////////////////////////////////////////////////////////
		solvedSudokuGrid = new ArrayList<>(9);

		solvedSudokuGrid.add(Arrays.asList(4, 3, 5, 2, 6, 9, 7, 8, 1));
		solvedSudokuGrid.add(Arrays.asList(6, 8, 2, 5, 7, 1, 4, 9, 3));
		solvedSudokuGrid.add(Arrays.asList(1, 9, 7, 8, 3, 4, 5, 6, 2));
		solvedSudokuGrid.add(Arrays.asList(8, 2, 6, 1, 9, 5, 3, 4, 7));
		solvedSudokuGrid.add(Arrays.asList(3, 7, 4, 6, 8, 2, 9, 1, 5));
		solvedSudokuGrid.add(Arrays.asList(9, 5, 1, 7, 4, 3, 6, 2, 8));
		solvedSudokuGrid.add(Arrays.asList(5, 1, 9, 3, 2, 6, 8, 7, 4));
		solvedSudokuGrid.add(Arrays.asList(2, 4, 8, 9, 5, 7, 1, 3, 6));
		solvedSudokuGrid.add(Arrays.asList(7, 6, 3, 4, 1, 8, 2, 5, 9));

		solvedSudoku = new Sudoku(solvedSudokuGrid);
	}

	@Test
	public void solveTest() {
		boolean isSolved = NaiveSolver.solve(unsolvedSudoku);
		Assert.assertTrue(isSolved);
		Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
	}

}
