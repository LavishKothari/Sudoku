package sudoku.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import sudoku.core.CustomSudokuSolver;
import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;

public class GridUtilsTest {

	private static List<List<Integer>> sudokuGrid;
	private static String hardString;

	@BeforeClass
	public static void setup() {

		hardString = "....7..2.8.......6.1.2.5...9.54....8.........3....85.1...3.2.8.4.......9.7..6....";

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

	}

	@Test
	public void isStrictComplyingListTest() {
		List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

		for (int i = 0; i < 10; i++) {
			Collections.shuffle(testList);
			Assert.assertTrue(GridUtils.isStrictComplyingList(testList.size(), testList));
		}

		testList.set(0, 0);
		for (int i = 0; i < 10; i++) {
			Collections.shuffle(testList);
			Assert.assertFalse(GridUtils.isStrictComplyingList(testList.size(), testList));
		}
	}

	@Test
	public void isWeakComplyingListTest() {
		List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 0, 7, 8, 9);

		for (int i = 0; i < 10; i++) {
			Collections.shuffle(testList);
			Assert.assertTrue(GridUtils.isWeakComplyingList(testList.size(), testList));
		}
	}

	@Test
	public void getListOfRowsTest() {
		for (int i = 0; i < 9; i++) {
			Assert.assertEquals(sudokuGrid.get(i), GridUtils.getListOfRow(sudokuGrid, i));
		}
	}

	@Test
	public void getListOfColumnnTest() {
		Assert.assertEquals(Arrays.asList(4, 6, 1, 8, 3, 9, 5, 2, 7), GridUtils.getListOfColumn(sudokuGrid, 0));
	}

	@Test
	public void getListOfInnerGridTest() {
		Assert.assertEquals(Arrays.asList(8, 7, 4, 1, 3, 6, 2, 5, 9), GridUtils.getListOfInnerGrid(sudokuGrid, 8));
	}

	@Test
	public void getInnerGridNumberTest() {

		List<Integer> list1 = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2);
		List<Integer> list2 = Arrays.asList(3, 3, 3, 4, 4, 4, 5, 5, 5);
		List<Integer> list3 = Arrays.asList(6, 6, 6, 7, 7, 7, 8, 8, 8);
		List<List<Integer>> list = new ArrayList<>();
		list.add(list1);
		list.add(list1);
		list.add(list1);
		list.add(list2);
		list.add(list2);
		list.add(list2);
		list.add(list3);
		list.add(list3);
		list.add(list3);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Assert.assertEquals(list.get(i).get(j).intValue(),
						GridUtils.getInnerGridNumber(sudokuGrid.size(), i, j));
			}
		}
	}

	@Test
	public void getIthIntegerTest() {
		Assert.assertEquals(7, GridUtils.getIthInteger(hardString, 4));
		Assert.assertEquals(0, GridUtils.getIthInteger(hardString, 1));
		Assert.assertEquals(0, GridUtils.getIthInteger(hardString, 0));

	}

	@Test
	public void getGridFromStringTest() {
		List<List<Integer>> hardSudokuGrid = new ArrayList<>(9);

		hardSudokuGrid.add(Arrays.asList(0, 0, 0, 0, 7, 0, 0, 2, 0));
		hardSudokuGrid.add(Arrays.asList(8, 0, 0, 0, 0, 0, 0, 0, 6));
		hardSudokuGrid.add(Arrays.asList(0, 1, 0, 2, 0, 5, 0, 0, 0));
		hardSudokuGrid.add(Arrays.asList(9, 0, 5, 4, 0, 0, 0, 0, 8));
		hardSudokuGrid.add(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0));
		hardSudokuGrid.add(Arrays.asList(3, 0, 0, 0, 0, 8, 5, 0, 1));
		hardSudokuGrid.add(Arrays.asList(0, 0, 0, 3, 0, 2, 0, 8, 0));
		hardSudokuGrid.add(Arrays.asList(4, 0, 0, 0, 0, 0, 0, 0, 9));
		hardSudokuGrid.add(Arrays.asList(0, 7, 0, 0, 6, 0, 0, 0, 0));

		Assert.assertEquals(hardSudokuGrid, GridUtils.getGridFromStringFormat(hardString));
	}

	@Test
	public void getGridsFromFileTest() throws IOException {
		List<String> fileNames = Arrays.asList("easy_puzzles.txt", "hard_puzzles.txt", "hardest_puzzles.txt");
		for (final String currentFileName : fileNames) {
			for (final List<List<Integer>> currentGrid : GridUtils.getGridsFromFile(currentFileName)) {
				Sudoku currentSudoku = new Sudoku(currentGrid);
				Assert.assertTrue(currentSudoku.isValid());
			}
		}
	}

}
