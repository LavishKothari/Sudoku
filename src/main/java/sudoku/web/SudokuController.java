package sudoku.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sudoku.core.Sudoku;

@RestController
public class SudokuController {
    @RequestMapping("sudoku")
    public Sudoku getUnfilledSudoku() {
        Sudoku unsolved = Sudoku.getPartiallyFilledSudokuPuzzle(9);

        return unsolved;
    }
}
