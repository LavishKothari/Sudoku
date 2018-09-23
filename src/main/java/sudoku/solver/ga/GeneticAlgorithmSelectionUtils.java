package sudoku.solver.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import sudoku.core.Sudoku;

public class GeneticAlgorithmSelectionUtils {

	private final static Logger logger = Logger.getLogger(GeneticAlgorithmSelectionUtils.class);

	/*
	 * We don't want to instantiate this class
	 */
	private GeneticAlgorithmSelectionUtils() {
		/*
		 * avoids accidental calls from within the class and also guards against
		 * reflection
		 */
		throw new UnsupportedOperationException("This class should not be instantiated");
	}

	public static List<Sudoku> getSelectedFitSudokus(List<Sudoku> inputSudokus, double selectionRate) {
		int solutionsInEachGeneration = inputSudokus.size();
		int toBeSelected = (int) (selectionRate * solutionsInEachGeneration);
		List<SudokuChromosome> sudokuChromosomeList = new ArrayList<>();
		SudokuChromosome.addAndRefreshProbabilityOfSelection(sudokuChromosomeList, inputSudokus);

		Collections.sort(sudokuChromosomeList, SudokuChromosome.SELECTION_PROBABILITY_COMPARATOR);

		sudokuChromosomeList.stream().forEach(e -> logger.debug(e.getProbabilityOfSelection()));
		return SudokuChromosome.getSudokuList(sudokuChromosomeList).subList(0, toBeSelected);
	}

}
