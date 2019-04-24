package sudoku.core;

import org.junit.Assert;
import org.junit.Test;
import sudoku.utils.GridUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerfTests {
    /**
     * To generate and solve around 200 sudokus,
     * it takes about 16-20 seconds
     */
    @Test
    public void perfTest() {
        long start = System.nanoTime();
        for (int i = 0; i < 200; i++) {
            Sudoku s = Sudoku.getPartiallyFilledSudokuPuzzle(9);
            Assert.assertTrue(s.isValid());
            Assert.assertTrue(s.hasUniqueSolution());

            new CustomSudokuSolver(s)
                    .alwaysCalculatingCellWithLeastPossibility(true)
                    .randomize(true)
                    .solve();

            Assert.assertTrue(s.isSolved());
        }
        double total = (System.nanoTime() - start) / 1000000.0;
        System.out.println("total time = " + total / 1000.0 + " seconds");
    }

    @Test
    public void perfOnOneFixedSudoku() throws IOException {
        long start = System.nanoTime();
        List<List<Integer>> sudokuGrid = GridUtils
                .getGridsFromFile("hardest_puzzles.txt")
                .get(0);
        for (int i = 0; i < 10000; i++) {
            System.out.println("-----" + i);
            List<List<Integer>> newGrid = new ArrayList<>(sudokuGrid.size());
            for (int x = 0; x < sudokuGrid.size(); x++) {
                newGrid.add(new ArrayList<>(sudokuGrid.get(x).size()));
            }
            for (int x = 0; x < sudokuGrid.size(); x++) {
                for (int y = 0; y < sudokuGrid.get(x).size(); y++) {
                    newGrid.get(x).add(sudokuGrid.get(x).get(y));
                }
            }
            Sudoku currentSudoku = new Sudoku(newGrid);
            Assert.assertTrue(currentSudoku.isValid());
            new CustomSudokuSolver(currentSudoku)
                    .alwaysCalculatingCellWithLeastPossibility(true)
                    .randomize(true)
                    .solve();

            Assert.assertTrue(currentSudoku.isSolved());
        }
        double total = (System.nanoTime() - start) / 1000000.0;
        System.out.println("total time = " + total / 1000.0 + " seconds");
    }
}
