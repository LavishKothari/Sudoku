package sudoku.solver.ga;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;
import sudoku.utils.NumberUtils;

final public class SudokuUtilForGeneticAlgorithm {

	/*
	 * We don't want to instantiate this class
	 */
	private SudokuUtilForGeneticAlgorithm() {
		/*
		 * avoids accidental calls from within the class and also guards against
		 * reflection
		 */
		throw new UnsupportedOperationException("This class should not be instantiated");
	}

	private static void setInnerGridUsingList(Sudoku sudoku, int innerGridNumber, List<Integer> list) {
		int dimensionOfGrid = sudoku.getDimensionOfGrid();
		int dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);

		int Y = innerGridNumber % dimensionOfInnerGrid;
		int X = innerGridNumber / dimensionOfInnerGrid;
		int startXIndex = X * dimensionOfInnerGrid;
		int startYIndex = Y * dimensionOfInnerGrid;

		int counter = 0;
		for (int i = startXIndex; i < startXIndex + dimensionOfInnerGrid; i++) {
			for (int j = startYIndex; j < startYIndex + dimensionOfInnerGrid; j++) {
				sudoku.setCellValue(i, j, list.get(counter++));
			}
		}
	}

	private static List<Integer> getConformingListForInnerGrid(int innerGridNumber, Sudoku sudoku) {
		List<Integer> list = sudoku.getInnerGridValues(innerGridNumber, true);

		List<Integer> canBeFilled = new ArrayList<>();
		for (int i = 1; i <= sudoku.getDimensionOfGrid(); i++) {
			if (!list.contains(i))
				canBeFilled.add(i);
		}
		Collections.shuffle(canBeFilled);

		int counter = 0;
		for (int i = 0; i < sudoku.getDimensionOfGrid(); i++) {
			if (list.get(i) == GridUtils.EMPTY_CELL) {
				list.set(i, canBeFilled.get(counter++));
			}
		}

		return list;
	}

	/*
	 * Conforming only in terms of inner-grid
	 */
	private static Sudoku getConformingSudoku(Sudoku sudoku) {
		int dimension = sudoku.getDimensionOfGrid();
		String str = CharBuffer.allocate(dimension * dimension).toString().replace('\0', '0');
		Sudoku resultSudoku = new Sudoku(str);
		for (int i = 0; i < dimension; i++) {
			List<Integer> conformingList = getConformingListForInnerGrid(i, sudoku);
			setInnerGridUsingList(resultSudoku, i, conformingList);
		}
		return resultSudoku;
	}

	public static List<Sudoku> getRandomMayBeInvalidConformingSudokuList(int numberOfInitialSolution, Sudoku sudoku) {
		List<Sudoku> sudokuList = new ArrayList<>(numberOfInitialSolution);
		for (int i = 0; i < numberOfInitialSolution; i++) {
			sudokuList.add(getConformingSudoku(sudoku));
		}
		return sudokuList;
	}
}
