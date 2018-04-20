package sudoku.solver.ga;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Assert;
import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;

public class SudokuUtilForGeneticAlgorithmTest {

	@Test
	public void getRandomMayBeInvalidConformingSudokuTest() {
		Sudoku s = Sudoku.getPartiallyFilledSudokuPuzzle(9);
		List<Sudoku> sudokuList = SudokuUtilForGeneticAlgorithm.getRandomMayBeInvalidConformingSudokuList(100, s);
		for (int i = 0; i < sudokuList.size(); i++) {
			Assert.assertTrue(checkIfConforming(s, sudokuList.get(i)));
		}
	}

	private boolean checkIfConforming(Sudoku original, Sudoku obtained) {
		if (original.getDimensionOfGrid() != obtained.getDimensionOfGrid()) {
			return false;
		}

		for (int i = 0; i < original.getDimensionOfGrid(); i++) {
			for (int j = 0; j < original.getDimensionOfGrid(); j++) {
				int originalValue = original.getCellValue(i, j);
				int obtainedValue = obtained.getCellValue(i, j);
				if (originalValue != GridUtils.EMPTY_CELL && originalValue != obtainedValue) {
					return false;
				}
				if (obtainedValue == GridUtils.EMPTY_CELL) {
					return false;
				}
			}
		}

		// checking if all inner grid contains every value
		for (int i = 0; i < obtained.getDimensionOfGrid(); i++) {
			Set<Integer> s = new HashSet<>();
			s.addAll(obtained.getInnerGridValues(i, true));
			if (s.size() != obtained.getDimensionOfGrid()) {
				return false;
			}
		}

		return true;
	}

}
