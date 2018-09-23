package sudoku.solver.ga;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;
import sudoku.utils.NumberUtils;

final public class DistanceCalculator {

	private final static Logger logger = Logger.getLogger(DistanceCalculator.class);

	/*
	 * We don't want to instantiate this class
	 */
	private DistanceCalculator() {
		/*
		 * avoids accidental calls from within the class and also guards against
		 * reflection
		 */
		throw new UnsupportedOperationException("This class should not be instantiated");
	}

	public static double getDistanceFromSolution(Sudoku sudoku) {
		int n = sudoku.getDimensionOfGrid();
		double distance = 0.0;

		for (int i = 0; i < n; i++) {

			List<Integer> rowList = sudoku.getRowValues(i);
			List<Integer> colList = sudoku.getColumnValues(i);
			List<Integer> gridList = sudoku.getInnerGridValues(i);

			distance += getDistanceOfHouseBySum(sudoku, rowList);
			distance += getDistanceOfHouseBySum(sudoku, colList);
			distance += getDistanceOfHouseBySum(sudoku, gridList);

			distance += getDistanceOfHouseByProduct(sudoku, rowList);
			distance += getDistanceOfHouseByProduct(sudoku, colList);
			distance += getDistanceOfHouseByProduct(sudoku, gridList);

			distance += getDistanceOfHouseByDistinctCount(sudoku, rowList);
			distance += getDistanceOfHouseByDistinctCount(sudoku, colList);
			distance += getDistanceOfHouseByDistinctCount(sudoku, gridList);
		}
		distance = Math.sqrt(distance);

		List<Integer> wholeGridList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			wholeGridList.addAll(sudoku.getRowValues(i));
		}

		// cost by seeing sum of whole grid
		distance += getDistanceOfWholeGridBySum(sudoku, wholeGridList);

		// cost by seeing the occurrence of each value in whole grid (will account for
		// empty cells and push up the cost)
		distance += getDistanceOfWholeGridByCount(sudoku, wholeGridList);
		logger.info("distance = " + distance);
		return distance;
	}

	private static double getDistanceOfHouseBySum(Sudoku sudoku, List<Integer> list) {
		int n = sudoku.getDimensionOfGrid();
		int requiredSumOfEachHouse = (n * (n + 1)) / 2;
		int obtainedSum = list.stream().mapToInt(x -> x.intValue()).sum();
		double sumCost = Math.sqrt((requiredSumOfEachHouse - obtainedSum) * (requiredSumOfEachHouse - obtainedSum));
		return Math.sqrt(sumCost);
	}

	private static double getDistanceOfHouseByProduct(Sudoku sudoku, List<Integer> list) {
		int n = sudoku.getDimensionOfGrid();
		long requiredProductOfEachHouse = NumberUtils.factorial(n);
		long obtainedProduct = list.stream().mapToLong(x -> x.longValue()).reduce(1, (x, y) -> x * y);
		double sumCost = Math
				.sqrt((requiredProductOfEachHouse - obtainedProduct) * (requiredProductOfEachHouse - obtainedProduct));
		return Math.pow(sumCost, 1.0 / n);
	}

	private static double getDistanceOfHouseByDistinctCount(Sudoku sudoku, List<Integer> list) {
		int n = sudoku.getDimensionOfGrid();
		int count = (int) list.stream().filter(x -> x != GridUtils.EMPTY_CELL).mapToInt(x -> x.intValue()).distinct()
				.count();
		return Math.sqrt((n - count) * (n - count));
	}

	private static double getDistanceOfWholeGridBySum(Sudoku sudoku, List<Integer> wholeGridList) {
		int n = sudoku.getDimensionOfGrid();
		int requiredWholeGridSum = ((n * (n + 1)) / 2) * n;
		int obtainedSum = wholeGridList.stream().mapToInt(x -> x.intValue()).sum();
		double sumCost = Math.sqrt((requiredWholeGridSum - obtainedSum) * (requiredWholeGridSum - obtainedSum));
		return Math.pow(sumCost, 1.0 / 3.0);
	}

	private static double getDistanceOfWholeGridByCount(Sudoku sudoku, List<Integer> wholeGridList) {
		int n = sudoku.getDimensionOfGrid();
		double cost = 0.0;
		// the following loop assumes that GridUtils.EMPTY_CELL = 0
		for (int i = 0; i <= n; i++) {
			final int j = i;
			int occurrence = (int) wholeGridList.stream().mapToInt(x -> x.intValue()).filter(x -> x == j).count();
			// there should be no occurrences of 0
			if (i == 0) {
				cost += occurrence;
			} else {
				cost += Math.sqrt((n - occurrence) * (n - occurrence));
			}
		}
		return Math.sqrt(cost);
	}

	public static double getMaximumDistance(int dimension) {
		String str = CharBuffer.allocate(dimension * dimension).toString().replace('\0', '0');
		Sudoku s = new Sudoku(str);
		return getDistanceFromSolution(s);
	}

}
