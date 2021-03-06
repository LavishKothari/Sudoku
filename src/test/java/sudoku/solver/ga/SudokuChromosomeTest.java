package sudoku.solver.ga;

import org.junit.Assert;
import org.junit.Test;
import sudoku.core.Sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuChromosomeTest {

    @Test
    public void test() {
        Sudoku sudoku = Sudoku.getPartiallyFilledSudokuPuzzle(9);
        List<Sudoku> sudokuList = SudokuGeneticAlgorithmUtils.getRandomMayBeInvalidConformingSudokuList(100, sudoku);

        List<SudokuChromosome> sudokuChromosomeList = new ArrayList<>();
        SudokuChromosome.addAndRefreshProbabilityOfSelection(sudokuChromosomeList, sudokuList);

        double totalProbability = sudokuChromosomeList.stream().mapToDouble(x -> x.getProbabilityOfSelection())
                .reduce(0.0, (x, y) -> x + y);

        Assert.assertTrue(1.0 - totalProbability < 0.000001);
    }

}
