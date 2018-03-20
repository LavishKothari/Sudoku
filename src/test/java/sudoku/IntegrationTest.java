package sudoku;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class IntegrationTest {

	@Test
	public void test() throws IOException {
		int obtained = 0;
		for (final List<List<Integer>> currentGrid : GridUtils.getGridsFromFile("easy_puzzles.txt")) {
			System.out.println(currentGrid);
			Sudoku s = new Sudoku(currentGrid);
			Assert.assertTrue(s.hasUniqueSolution());
			Assert.assertTrue(
					new CustomSudokuSolver(s).alwaysCalculatingCellWithLeastPossibility(true).randomize(true).solve());
			obtained += s.getCellValue(0, 0) * 100 + s.getCellValue(0, 1) * 10 + s.getCellValue(0, 2);
		}
		// from projecteuler.net
		Assert.assertEquals(24702, obtained);
	}

}
