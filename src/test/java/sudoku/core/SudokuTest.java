package sudoku.core;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sudoku.utils.GridUtils;

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

	@Test
	public void getCellWithLeastPossibilityTest() {
		// because the sudoku is completely filled
		Assert.assertEquals(-1, sudoku.getCellWithLeastPossibility());

		// because the sudoku is not completely filled
		Assert.assertTrue(uniqueTestSudoku.getCellWithLeastPossibility() != -1);
	}

	@Test
	public void generateRandomCompletelyFilledSudokuTest() {
		Sudoku s = Sudoku.generateRandomCompletelyFilledSudoku(9);
		Assert.assertTrue(s.isValid());
		Assert.assertTrue(s.isSolved());
	}

	@Test
	public void getPartiallyFilledSudokuPuzzleTest() {
		Sudoku s = Sudoku.getPartiallyFilledSudokuPuzzle(9);
		Assert.assertTrue(s.isValid());
		Assert.assertTrue(s.hasUniqueSolution());

		new CustomSudokuSolver(s).alwaysCalculatingCellWithLeastPossibility(true).randomize(true).solve();

		Assert.assertTrue(s.isSolved());
	}

	@Test
	public void writeSudokuToFileTest() throws IOException {
		String solutionFileName = "easy_puzzles_solution.txt";
		URL url = SudokuTest.class.getClassLoader().getResource(solutionFileName);
		Path path = Paths.get(url.getPath());
		Files.write(path, "".getBytes());

		for (final List<List<Integer>> currentGrid : GridUtils.getGridsFromFile("easy_puzzles.txt")) {
			Sudoku currentSudoku = new Sudoku(currentGrid);
			Assert.assertTrue(currentSudoku.isValid());
			new CustomSudokuSolver(currentSudoku).randomize(true).alwaysCalculatingCellWithLeastPossibility(true)
					.solve();
			currentSudoku.writeSudokuToFile(solutionFileName, StandardOpenOption.APPEND);
		}
		for (final Sudoku currentSudoku : Sudoku.getSudokusFromFile(solutionFileName)) {
			Assert.assertTrue(currentSudoku.isSolved());
		}
	}

	@Test
	public void isSolutionOfTest() {
		for (int i = 0; i < 10; i++) {
			Sudoku unsolved = Sudoku.generateRandomCompletelyFilledSudoku(9);
			Sudoku solved = new Sudoku(unsolved.getGrid());
			new CustomSudokuSolver(solved).alwaysCalculatingCellWithLeastPossibility(true).randomize(true).solve();
			Assert.assertTrue(solved.isSolutionOf(unsolved));
		}
	}

	@Test
	public void appendSudokuTest() throws IOException {
		String puzzleFileName = "sudoku_puzzles_generated.txt";
		String solutionFileName = "sudoku_solution_generated.txt";

		// generating puzzles and solutions for 20 seconds
		int appendedSudokus = Sudoku.appendRandomGeneratedSudoku(9, puzzleFileName, solutionFileName, 20000);
		List<Sudoku> unsolvedSudokuList = Sudoku.getSudokusFromFile(puzzleFileName);
		List<Sudoku> solvedSudokuList = Sudoku.getSudokusFromFile(solutionFileName);

		for (int i = 0; i < unsolvedSudokuList.size(); i++) {
			Assert.assertTrue(solvedSudokuList.get(i).isSolutionOf(unsolvedSudokuList.get(i)));
			Assert.assertTrue(solvedSudokuList.get(i).isSolved());
		}
	}

	@Test
	public void solveUsingNaiveTechniqueTest() throws IOException {
		for (final List<List<Integer>> currentGrid : GridUtils.getGridsFromFile("easy_puzzles.txt")) {
			Sudoku currentSudoku = new Sudoku(currentGrid);
			Assert.assertTrue(currentSudoku.isValid());
			currentSudoku.solveUsingNaiveTechnique();
			Assert.assertTrue(currentSudoku.isValid());
		}
	}
}
