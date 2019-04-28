package sudoku;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import sudoku.core.CustomSudokuSolverTest;
import sudoku.core.SudokuTest;
import sudoku.solver.ga.SudokuChromosomeTest;
import sudoku.solver.ga.SudokuUtilForGeneticAlgorithmTest;
import sudoku.utils.CoOrdinateUtilsTest;
import sudoku.utils.GridUtilsTest;
import sudoku.utils.NumberUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({CustomSudokuSolverTest.class,
        SudokuTest.class,
        NumberUtilsTest.class,
        GridUtilsTest.class,
        CoOrdinateUtilsTest.class,
        SudokuChromosomeTest.class,
        SudokuUtilForGeneticAlgorithmTest.class,
        IntegrationTest.class})
public final class MasterTester {

}