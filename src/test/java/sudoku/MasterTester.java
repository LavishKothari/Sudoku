package sudoku;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GridUtilsTest.class, NumberUtilsTest.class, SudokuTest.class, CustomSudokuSolverTest.class, IntegrationTest.class})
public final class MasterTester {

}