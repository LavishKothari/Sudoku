package sudoku.solver.ga;

import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

final public class SudokuGeneticAlgorithmUtils {

    /*
     * We don't want to instantiate this class
     */
    private SudokuGeneticAlgorithmUtils() {
        /*
         * avoids accidental calls from within the class and also guards against
         * reflection
         */
        throw new UnsupportedOperationException("This class should not be instantiated");
    }


    private static List<Integer> getConformingListForInnerGrid(int innerGridNumber, Sudoku sudoku) {
        List<Integer> list = sudoku.getInnerGridValues(innerGridNumber, true);

        List<Integer> canBeFilled = IntStream.rangeClosed(1, sudoku.getDimensionOfGrid())
                .filter(i -> !list.contains(i))
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(canBeFilled);

        int counter = 0;
        for (int i = 0; i < sudoku.getDimensionOfGrid(); i++) {
            if (list.get(i) == GridUtils.EMPTY_CELL_VALUE) {
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
        String str = CharBuffer.allocate(dimension * dimension)
                .toString()
                .replace('\0', '0');
        Sudoku resultSudoku = new Sudoku(str);
        for (int i = 0; i < dimension; i++) {
            List<Integer> conformingList = getConformingListForInnerGrid(i, sudoku);
            GridUtils.setInnerGridUsingList(resultSudoku, i, conformingList);
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
