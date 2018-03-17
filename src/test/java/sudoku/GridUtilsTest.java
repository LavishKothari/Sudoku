package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GridUtilsTest {

	private List<List<Integer>> sudokuGrid;

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

}
