package sudoku;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sudoku.core.CustomSudokuSolverTest;
import sudoku.core.SudokuTest;
import sudoku.utils.GridUtilsTest;
import sudoku.utils.NumberUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ GridUtilsTest.class, NumberUtilsTest.class, SudokuTest.class, CustomSudokuSolverTest.class, IntegrationTest.class})
public final class MasterTester {

}