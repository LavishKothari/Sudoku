package sudoku.solver.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import sudoku.core.Sudoku;

public class SudokuChromosome {
	private Sudoku sudoku;
	private double inverseDistance;
	private double probabilityOfSelection;

	public double getProbabilityOfSelection() {
		return probabilityOfSelection;
	}

	private static List<SudokuChromosome> sudokuChromosomeList = new ArrayList<>();;

	public static final Comparator<SudokuChromosome> SELECTION_PROBABILITY_COMPARATOR = new Comparator<SudokuChromosome>() {
		@Override
		public int compare(SudokuChromosome s1, SudokuChromosome s2) {
			Double d1 = s1.probabilityOfSelection;
			Double d2 = s2.probabilityOfSelection;
			return d1.compareTo(d2);
		}
	};

	public static final Comparator<SudokuChromosome> DISTANCE_COMPARATOR = new Comparator<SudokuChromosome>() {
		@Override
		public int compare(SudokuChromosome s1, SudokuChromosome s2) {
			Double d1 = s1.inverseDistance;
			Double d2 = s2.inverseDistance;
			return d1.compareTo(d2);
		}
	};

	private SudokuChromosome(Sudoku sudoku) {
		this.sudoku = sudoku;
		inverseDistance = 1.0 / DistanceCalculator.getDistanceFromSolution(this.sudoku);
	}

	public static List<SudokuChromosome> getSudokuChromosomeList() {
		return sudokuChromosomeList;
	}

	public static void addToSudokuChromosomeList(Sudoku sudoku) {
		SudokuChromosome chromosome = new SudokuChromosome(sudoku);
		sudokuChromosomeList.add(chromosome);
	}

	public static void addToSudokuChromosomeList(List<Sudoku> sudokuList) {
		for (int i = 0; i < sudokuList.size(); i++) {
			SudokuChromosome chromosome = new SudokuChromosome(sudokuList.get(i));
			sudokuChromosomeList.add(chromosome);
		}
	}

	public static void addAndRefreshProbabilityOfSelection(List<Sudoku> sudokuList) {
		addToSudokuChromosomeList(sudokuList);
		refreshProbabilityOfSelection();
	}

	public static void refreshProbabilityOfSelection() {
		double totalInverseDistance = sudokuChromosomeList.stream().mapToDouble(x -> x.inverseDistance).reduce(0.0,
				(x, y) -> x + y);
		for (int i = 0; i < sudokuChromosomeList.size(); i++) {
			sudokuChromosomeList.get(i).probabilityOfSelection = sudokuChromosomeList.get(i).inverseDistance
					/ totalInverseDistance;
		}
	}

}
