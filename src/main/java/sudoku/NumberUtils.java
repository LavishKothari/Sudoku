package sudoku;

import java.util.ArrayList;
import java.util.List;

public final class NumberUtils {

	private NumberUtils() {

	}

	public static boolean isPerfectSquare(long n) {
		long start = 1, end = n;
		while (start <= end) {
			long mid = (start + end) / 2;
			if (mid * mid == n || start * start == n || end * end == n)
				return true;
			else if (mid * mid < n)
				start = mid + 1;
			else
				end = mid - 1;

		}
		return false;
	}

	public static long getSqureRoot(long n) {
		long start = 1, end = n;
		long mid = (start + end) / 2;
		while (start <= end) {
			mid = (start + end) / 2;
			if (mid * mid == n)
				return mid;
			else if (start * start == n)
				return start;
			else if (end * end == n)
				return end;
			else if (mid * mid < n)
				start = mid + 1;
			else
				end = mid - 1;
		}
		return mid;
	}

	public static List<Integer> getShuffledList(int from, int to) {
		List<Integer> resultList = new ArrayList<>(to - from + 1);
		for (int i = from; i <= to; i++)
			resultList.add(i);
		return resultList;
	}

}
