package sudoku.solver.ga;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import sudoku.core.Sudoku;

public class SudokuChromosomeTest {

	@Test
	public void test() {
		Sudoku sudoku = Sudoku.getPartiallyFilledSudokuPuzzle(9);
		List<Sudoku> sudokuList = SudokuUtilForGeneticAlgorithm.getRandomMayBeInvalidConformingSudokuList(100, sudoku);

		SudokuChromosome.addAndRefreshProbabilityOfSelection(sudokuList);

		double totalProbability = SudokuChromosome.getSudokuChromosomeList().stream()
				.mapToDouble(x -> x.getProbabilityOfSelection()).reduce(0.0, (x, y) -> x + y);

		Assert.assertTrue(1.0 - totalProbability < 0.000001);
	}

}
