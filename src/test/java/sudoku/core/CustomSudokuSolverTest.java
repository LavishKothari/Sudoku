package sudoku.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomSudokuSolverTest {

    private static Sudoku unsolvedSudoku;
    private static List<List<Integer>> unsolvedSudokuGrid;

    private static Sudoku unsolvedHardSudoku;
    private static List<List<Integer>> hardSudokuGrid;

    private static Sudoku solvedSudoku;
    private static List<List<Integer>> solvedSudokuGrid;

    @BeforeClass
    public static void setupForClass() {

        /*
         * The solved version of sudoku will always remain same.
         */
        solvedSudokuGrid = new ArrayList<>(9);

        solvedSudokuGrid.add(Arrays.asList(4, 3, 5, 2, 6, 9, 7, 8, 1));
        solvedSudokuGrid.add(Arrays.asList(6, 8, 2, 5, 7, 1, 4, 9, 3));
        solvedSudokuGrid.add(Arrays.asList(1, 9, 7, 8, 3, 4, 5, 6, 2));
        solvedSudokuGrid.add(Arrays.asList(8, 2, 6, 1, 9, 5, 3, 4, 7));
        solvedSudokuGrid.add(Arrays.asList(3, 7, 4, 6, 8, 2, 9, 1, 5));
        solvedSudokuGrid.add(Arrays.asList(9, 5, 1, 7, 4, 3, 6, 2, 8));
        solvedSudokuGrid.add(Arrays.asList(5, 1, 9, 3, 2, 6, 8, 7, 4));
        solvedSudokuGrid.add(Arrays.asList(2, 4, 8, 9, 5, 7, 1, 3, 6));
        solvedSudokuGrid.add(Arrays.asList(7, 6, 3, 4, 1, 8, 2, 5, 9));

        solvedSudoku = new Sudoku(solvedSudokuGrid);
    }

    @Before
    public void setupBeforeEachTest() {
        refreshedUnsolvedSudoku();
    }

    @Test
    public void solveNaiveTest() {
        boolean isSolved = new CustomSudokuSolver(unsolvedSudoku).solve();
        Assert.assertEquals(unsolvedSudoku.isSolved(), isSolved);
        Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
    }

    @Test
    public void solveRandomizedTest() {
        boolean isSolved = new CustomSudokuSolver(unsolvedSudoku).randomize(true).solve();
        Assert.assertEquals(unsolvedSudoku.isSolved(), isSolved);
        Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
    }

    @Test
    public void pureRandomizedTestWithRandomSequenceList() {
        List<Integer> sequenceList = new ArrayList<>();
        for (int i = 0; i < unsolvedSudoku.getDimensionOfGrid() * unsolvedSudoku.getDimensionOfGrid(); i++)
            sequenceList.add(i);
        Collections.shuffle(sequenceList);

        boolean isSolved = new CustomSudokuSolver(unsolvedSudoku).randomize(true).sequenceList(sequenceList).solve();
        Assert.assertEquals(unsolvedSudoku.isSolved(), isSolved);
        Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
    }

    @Test
    public void optimalSolveTest() {
        boolean isSolved = new CustomSudokuSolver(unsolvedSudoku).randomize(true).selectInitialOptimalCellOrderingList()
                .solve();
        Assert.assertEquals(unsolvedSudoku.isSolved(), isSolved);
        Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
    }

    @Test
    public void alwaysCalculateCellWithLeastPossibilityTest() {
        boolean isSolved = new CustomSudokuSolver(unsolvedSudoku).randomize(true).selectInitialOptimalCellOrderingList()
                .alwaysCalculatingCellWithLeastPossibility(true).solve();
        Assert.assertEquals(unsolvedSudoku.isSolved(), isSolved);
        Assert.assertEquals(solvedSudoku.getGrid(), unsolvedSudoku.getGrid());
    }

    @Test
    public void hardSudokuTest() {
        boolean isSolved = new CustomSudokuSolver(unsolvedHardSudoku).randomize(true)
                .selectInitialOptimalCellOrderingList().solve();
        Assert.assertEquals(unsolvedHardSudoku.isSolved(), isSolved);
    }

    public void refreshedUnsolvedSudoku() {
        unsolvedSudokuGrid = getUnsolvedSudokuGrid();
        unsolvedSudoku = new Sudoku(unsolvedSudokuGrid);

        hardSudokuGrid = getHardSudokuGrid();
        unsolvedHardSudoku = new Sudoku(hardSudokuGrid);
    }

    public List<List<Integer>> getHardSudokuGrid() {

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
        return hardSudokuGrid;
    }

    public List<List<Integer>> getUnsolvedSudokuGrid() {
        List<List<Integer>> sampleUnsolvedSudokuGrid = new ArrayList<>(9);

        sampleUnsolvedSudokuGrid.add(Arrays.asList(0, 0, 0, 2, 6, 0, 7, 0, 1));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(6, 8, 0, 0, 7, 0, 0, 9, 0));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(1, 9, 0, 0, 0, 4, 5, 0, 0));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(8, 2, 0, 1, 0, 0, 0, 4, 0));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(0, 0, 4, 6, 0, 2, 9, 0, 0));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(0, 5, 0, 0, 0, 3, 0, 2, 8));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(0, 0, 9, 3, 0, 0, 0, 7, 4));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(0, 4, 0, 0, 5, 0, 0, 3, 6));
        sampleUnsolvedSudokuGrid.add(Arrays.asList(7, 0, 3, 0, 1, 8, 0, 0, 0));

        return sampleUnsolvedSudokuGrid;

    }
}
