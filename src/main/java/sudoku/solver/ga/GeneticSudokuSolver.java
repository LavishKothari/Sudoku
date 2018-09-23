package sudoku.solver.ga;

import java.util.Collections;
import java.util.List;

import sudoku.core.Sudoku;

public class GeneticSudokuSolver {

	private int numberOfInitialSolution = 2000;
	private int maximumNumberOfGenerations = 10000;
	private double selectionRate = 0.30;
	private double mutationRate = 0.08;
	private int numberOfCrossOverPoints = 15;

	private final int dimension;

	private final Sudoku sudoku;

	public GeneticSudokuSolver(Sudoku sudoku) {
		super();
		this.sudoku = sudoku;
		dimension = sudoku.getDimensionOfGrid();
	}

	public GeneticSudokuSolver numberOfInitialSolution(int numberOfInitialSolution) {
		this.numberOfInitialSolution = numberOfInitialSolution;
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

		List<Sudoku> sudokuList = SudokuUtilForGeneticAlgorithm
				.getRandomMayBeInvalidConformingSudokuList(numberOfInitialSolution, sudoku);

		SudokuChromosome.addAndRefreshProbabilityOfSelection(sudokuList);

		Collections.sort(SudokuChromosome.getSudokuChromosomeList(), SudokuChromosome.SELECTION_PROBABILITY_COMPARATOR);

		// SudokuChromosome.getSudokuChromosomeList().stream()
		// .forEach(e -> System.out.println(e.getProbabilityOfSelection()));
		//
		// int selected = 0;
		// for (int i = SudokuChromosome.getSudokuChromosomeList().size() - 1; i >= 0;
		// i--) {
		// double currentRandomNumber = new Random().nextDouble() /
		// numberOfInitialSolution;
		// if (currentRandomNumber <
		// SudokuChromosome.getSudokuChromosomeList().get(i).getProbabilityOfSelection())
		// {
		// selected++;
		// System.out.println("selected with probability = "
		// +
		// SudokuChromosome.getSudokuChromosomeList().get(i).getProbabilityOfSelection());
		// }
		// }
		// System.out.println("selected = " + selected);
		return false;
	}

}
