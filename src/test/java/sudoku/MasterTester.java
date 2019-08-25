package sudoku;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import sudoku.core.CustomSudokuSolverTest;
import sudoku.core.SudokuTest;
import sudoku.solver.ga.SudokuChromosomeTest;
import sudoku.solver.ga.SudokuGeneticAlgorithmUtilsTest;
import sudoku.utils.CoOrdinateUtilsTest;
import sudoku.utils.GridUtilsTest;
import sudoku.utils.NumberUtilsTest;
import sudoku.utils.SingleIntBitSetTest;

@RunWith(Suite.class)
@SuiteClasses({CustomSudokuSolverTest.class,
        SudokuTest.class,
        NumberUtilsTest.class,
        GridUtilsTest.class,
        CoOrdinateUtilsTest.class,
        SingleIntBitSetTest.class,
        SudokuChromosomeTest.class,
        SudokuGeneticAlgorithmUtilsTest.class,
        IntegrationTest.class})
public final class MasterTester {

}
