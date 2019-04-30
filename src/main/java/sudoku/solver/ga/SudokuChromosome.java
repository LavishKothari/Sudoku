package sudoku.solver.ga;

import sudoku.core.Sudoku;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SudokuChromosome implements WeightedEntity {
    public static final Comparator<SudokuChromosome> SELECTION_PROBABILITY_COMPARATOR =
            Comparator.comparingDouble(SudokuChromosome::getProbabilityOfSelection);
    public static final Comparator<SudokuChromosome> DISTANCE_COMPARATOR =
            Comparator.comparingDouble(SudokuChromosome::getInverseDistance);
    private final Sudoku sudoku;
    private final double inverseDistance;
    private double probabilityOfSelection;

    private SudokuChromosome(Sudoku sudoku) {
        this.sudoku = sudoku;
        inverseDistance = 1.0 / DistanceCalculator.getDistanceFromSolution(this.sudoku);
    }

    public static void addToSudokuChromosomeList(List<SudokuChromosome> sudokuChromosomeList, Sudoku sudoku) {
        SudokuChromosome chromosome = new SudokuChromosome(sudoku);
        sudokuChromosomeList.add(chromosome);
    }

    public static void addToSudokuChromosomeList(List<SudokuChromosome> sudokuChromosomeList, List<Sudoku> sudokuList) {
        for (int i = 0; i < sudokuList.size(); i++) {
            SudokuChromosome chromosome = new SudokuChromosome(sudokuList.get(i));
            sudokuChromosomeList.add(chromosome);
        }
    }

    public static void addAndRefreshProbabilityOfSelection(List<SudokuChromosome> sudokuChromosomeList,
                                                           List<Sudoku> sudokuList) {
        addToSudokuChromosomeList(sudokuChromosomeList, sudokuList);
        refreshProbabilityOfSelection(sudokuChromosomeList);
    }

    public static void refreshProbabilityOfSelection(List<SudokuChromosome> sudokuChromosomeList) {
        double totalInverseDistance = sudokuChromosomeList
                .stream()
                .mapToDouble(x -> x.inverseDistance)
                .reduce(0.0, (x, y) -> x + y);
        for (int i = 0; i < sudokuChromosomeList.size(); i++) {
            sudokuChromosomeList.get(i).probabilityOfSelection = sudokuChromosomeList.get(i).inverseDistance
                    / totalInverseDistance;
        }
    }

    public static List<Sudoku> getSudokuList(List<SudokuChromosome> sudokuChromosomeList) {
        List<Sudoku> sudokuList = new ArrayList<>(sudokuChromosomeList.size());
        sudokuChromosomeList
                .stream()
                .forEach(e -> sudokuList.add(e.sudoku));
        return sudokuList;
    }

    public double getProbabilityOfSelection() {
        return probabilityOfSelection;
    }

    private double getInverseDistance() {
        return inverseDistance;
    }

    @Override
    public double getWeight() {
        return probabilityOfSelection;
    }

}
