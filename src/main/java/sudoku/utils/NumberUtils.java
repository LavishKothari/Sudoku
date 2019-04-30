package sudoku.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NumberUtils {

    private static final int PERFECT_SQUARE_CACHE_SIZE = 10;
    private static final int[] perfectSquare;

    static {
        perfectSquare = new int[PERFECT_SQUARE_CACHE_SIZE * PERFECT_SQUARE_CACHE_SIZE + 1];
        for (int i = 0; i <= PERFECT_SQUARE_CACHE_SIZE * PERFECT_SQUARE_CACHE_SIZE; i++) {
            perfectSquare[i] = -1;
        }
        for (int i = 0; i <= PERFECT_SQUARE_CACHE_SIZE; i++) {
            perfectSquare[i * i] = i;
        }
    }

    /*
     * We don't want to instantiate this class
     */
    private NumberUtils() {
        /*
         * avoids accidental calls from within the class and also guards against
         * reflection
         */
        throw new UnsupportedOperationException("This class should not be instantiated");
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
        if (n <= PERFECT_SQUARE_CACHE_SIZE * PERFECT_SQUARE_CACHE_SIZE &&
                perfectSquare[(int) n] != -1)
            return perfectSquare[(int) n];

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

    /**
     * returns a shuffled list of integers from till to both inclusive
     *
     * @param from
     * @param to
     * @return
     */
    public static List<Integer> getShuffledList(int from, int to) {
        List<Integer> resultList = new ArrayList<>(to - from + 1);
        for (int i = from; i <= to; i++)
            resultList.add(i);
        Collections.shuffle(resultList);
        return resultList;
    }

    public static long factorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Arguments to factorial should be non-negative integer");
        long f = 1;
        for (int i = 1; i <= n; i++)
            f = f * i;
        return f;
    }

}
