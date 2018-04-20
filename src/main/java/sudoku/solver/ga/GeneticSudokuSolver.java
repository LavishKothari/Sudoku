package sudoku.solver.ga;

import java.util.ArrayList;
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

		if(sudoku.isSolved()) {
			return true;
		}
		
		List<Sudoku> sudokuList = SudokuUtilForGeneticAlgorithm
				.getRandomMayBeInvalidConformingSudokuList(numberOfInitialSolution, sudoku);
		List<Double> distanceList = new ArrayList<>(sudokuList.size());
		for (int i = 0; i < sudokuList.size(); i++) {
			distanceList.add(DistanceCalculator.getDistanceFromSolution(sudokuList.get(i)));
		}

		System.out.println(distanceList);
		return false;
	}

}
