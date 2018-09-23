package sudoku.solver.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sudoku.core.Sudoku;

public class GeneticSudokuSolver {

	private int solutionsInEachGeneration = 2000;
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
		List<Sudoku> sudokuList = SudokuUtilForGeneticAlgorithm
				.getRandomMayBeInvalidConformingSudokuList(solutionsInEachGeneration, sudoku);

		for (int i = 0; i < maximumNumberOfGenerations; i++) {

			// TODO: check if any sudoku in sodokuList is solved correctly, if yes, then
			// return true

			List<Sudoku> selectedSudokus = GeneticAlgorithmSelectionUtils.getSelectedFitSudokus(sudokuList,
					selectionRate);

			// TODO: perform breeding of selected sudokus
			// TODO: perform mutation of sudokus resulted from breeding
			// TODO: Initialise sudokuList with the result of mutation
		}
		return false;
	}

}
