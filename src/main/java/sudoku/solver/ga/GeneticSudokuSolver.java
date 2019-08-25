package sudoku.solver.ga;

import sudoku.core.Sudoku;
import sudoku.utils.GridUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GeneticSudokuSolver {

    private static Random rnd = new Random();
    private final int dimension;
    private final Sudoku sudoku;
    private int solutionsInEachGeneration = 2000;
    private int maximumNumberOfGenerations = 10000;
    private double selectionRate = 0.30;
    private double mutationRate = 0.08;
    private int numberOfCrossOverPoints = 15;

    public GeneticSudokuSolver(Sudoku sudoku) {
        super();
        this.sudoku = sudoku;
        dimension = sudoku.getDimensionOfGrid();
    }

    private static List<Sudoku> performBreeding(List<Sudoku> sudokus) {
        List<Sudoku> result = new ArrayList<>(sudokus.size());
        for (int i = 0; i < sudokus.size(); i++) {
            int a = rnd.nextInt(sudokus.size());
            int b = rnd.nextInt(sudokus.size());
            Sudoku sudokuA = sudokus.get(a);
            Sudoku sudokuB = sudokus.get(b);

            Sudoku newSudoku = new Sudoku(GridUtils.getEmptyGrid(sudokuA.getDimensionOfGrid()));
            for (int j = 0; j < sudokuA.getDimensionOfGrid(); j++) {
                if ((j & 1) == 0) {
                    List<Integer> list = sudokuA.getInnerGridValues(j);
                    GridUtils.setInnerGridUsingList(newSudoku, j, list);
                } else {
                    List<Integer> list = sudokuB.getInnerGridValues(j);
                    GridUtils.setInnerGridUsingList(newSudoku, j, list);
                }
            }
            result.add(newSudoku);
        }
        return result;
    }

    public GeneticSudokuSolver numberOfInitialSolution(int numberOfInitialSolution) {
        this.solutionsInEachGeneration = numberOfInitialSolution;
        return this;
    }

    public GeneticSudokuSolver maximumNumberOfGenerations(int maximumNumberOfGenerations) {
        this.maximumNumberOfGenerations = maximumNumberOfGenerations;
        return this;
    }

    public GeneticSudokuSolver selectionRate(int selectionRate) {
        this.selectionRate = selectionRate;
        return this;
    }

    public GeneticSudokuSolver mutationRate(int mutationRate) {
        this.mutationRate = mutationRate;
        return this;
    }

    public GeneticSudokuSolver numberOfCrossOverPoints(int numberOfCrossOverPoints) {
        this.numberOfCrossOverPoints = numberOfCrossOverPoints;
        return this;
    }

    public boolean solve() {
        sudoku.solveUsingDeterministicTechniques();
        if (sudoku.isSolved()) {
            return true;
        }
        List<Sudoku> sudokuList = SudokuGeneticAlgorithmUtils
                .getRandomMayBeInvalidConformingSudokuList(solutionsInEachGeneration, sudoku);

        for (int i = 0; i < maximumNumberOfGenerations; i++) {
        //for (int i = 0; ; i++) {
            // check if any sudoku in sudokuList is solved correctly,
            // if yes, then return true
            Optional<Sudoku> solved = sudokuList.stream()
                    .filter(Sudoku::isSolved)
                    .findAny();
            if (solved.isPresent()) {
                sudoku.setGrid(solved.get().getGrid());
                return true;
            }

            List<Sudoku> selectedSudokus = GeneticAlgorithmSelectionUtils
                    .getSelectedFitSudokus(sudokuList,
                            selectionRate);

            //  perform breeding of selected sudokus
            List<Sudoku> breeded = performBreeding(selectedSudokus);

            breeded.stream()
                    .forEach(s ->
                            System.out.println(DistanceCalculator.getDistanceFromSolution(s)));
            // TODO: perform mutation of sudokus resulted from breeding
            // TODO: Initialise sudokuList with the result of mutation
            sudokuList = breeded;
        }
        return false;
    }

}
