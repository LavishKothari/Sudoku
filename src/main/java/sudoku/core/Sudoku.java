package sudoku.core;

import org.apache.log4j.Logger;
import sudoku.utils.CoOrdinate;
import sudoku.utils.GridUtils;
import sudoku.utils.NumberUtils;
import sudoku.utils.SingleIntBitSet;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

public class Sudoku {

    private final static Logger logger = Logger.getLogger(Sudoku.class);
    private final int dimensionOfGrid;
    private final int dimensionOfInnerGrid;
    private List<List<Integer>> grid;
    /**
     * The functionality corresponding to the cachedSolvedGrid is to be implemented.
     */
    private List<List<Integer>> cachedSolvedGrid;

    public Sudoku(List<List<Integer>> grid) {
        this.grid = grid;
        dimensionOfGrid = grid.size();
        dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);
    }

    public Sudoku(String s) {
        this.grid = GridUtils.getGridFromStringFormat(s);
        dimensionOfGrid = this.grid.size();
        dimensionOfInnerGrid = (int) NumberUtils.getSqureRoot(dimensionOfGrid);
    }

    /**
     * @param tempSudoku
     * @param currentCellNumber
     * @return This function return 1 if the sudoku has a unique solution and it
     * returns a value greater than 1 if it has multiple solution and a
     * value 0 if it has no solution.
     */
    private static int uniqueSolutionDecider(Sudoku tempSudoku, int currentCellNumber) {
        if (currentCellNumber == -1 && tempSudoku.isSolved()) {
            tempSudoku.setCachedSolvedGrid(tempSudoku.getGrid());
            return 1;
        } else if (currentCellNumber == -1) {
            /*
             * definitely currentCellNumber is -1 because of some inconsistent state in
             * sudoku that occurred during backtracking
             */
            return 0;
        }

        int row = currentCellNumber / tempSudoku.getDimensionOfGrid();
        int col = currentCellNumber % tempSudoku.getDimensionOfGrid();
        List<Integer> possibleValues = tempSudoku.getPossibleValues(row, col);
        Collections.shuffle(possibleValues);
        int solutions = 0;
        for (final Integer currentValue : possibleValues) {
            String previous = GridUtils.getStringFromGrid(tempSudoku.getGrid());
            tempSudoku.setCellValue(row, col, currentValue);

            tempSudoku.solveUsingDeterministicTechniques();

            int nextCellNumber = tempSudoku.getCellWithLeastPossibility();
            solutions += uniqueSolutionDecider(tempSudoku, nextCellNumber);
            if (solutions > 1) {
                return 2;
            }
            // tempSudoku.clearCell(row, col);
            tempSudoku.setGrid(GridUtils.getGridFromStringFormat(previous));
        }
        // here the value of soutions will either be 0 or 1
        // clearing the cell is an important thing to do before backtracking
        // tempSudoku.clearCell(row, col);
        return solutions;

    }

    /**
     * The following method generates a random completely filled valid sudoku.
     *
     * @param n, the dimension of grid
     * @return
     */
    public static Sudoku generateRandomCompletelyFilledSudoku(int n) {
        if (!NumberUtils.isPerfectSquare(n)) {
            throw new IllegalArgumentException("The dimension of the sudoku grid should be a perfect square");
        }
        List<List<Integer>> grid = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            grid.add(new ArrayList<>(n));
            for (int j = 0; j < n; j++) {
                grid.get(i).add(GridUtils.EMPTY_CELL_VALUE);
            }
        }
        Sudoku sudoku = new Sudoku(grid);
        while (!sudoku.isSolved()) {
            new CustomSudokuSolver(sudoku)
                    .alwaysCalculatingCellWithLeastPossibility(true)
                    .randomize(true)
                    //.selectInitialOptimalCellOrderingList()
                    .backtrackingTimeOut(1000)
                    .solve();
        }
        return sudoku;
    }

    /**
     * This method returns a partially filled sudoku
     *
     * @param dimension
     * @return
     */
    public static Sudoku getPartiallyFilledSudokuPuzzle(int dimension) {
        Sudoku s = Sudoku.generateRandomCompletelyFilledSudoku(dimension);
        // updating the cached grid
        s.setCachedSolvedGrid(s.getGrid());
        List<Integer> randomIndices = NumberUtils.getShuffledList(0, dimension * dimension - 1);

        for (final Integer currentRandomIndex : randomIndices) {
            int row = currentRandomIndex / dimension;
            int col = currentRandomIndex % dimension;
            Integer toBeReplaced = s.getCellValue(row, col);

            s.setCellValue(row, col, GridUtils.EMPTY_CELL_VALUE);
            if (!s.hasUniqueSolution()) {
                s.setCellValue(row, col, toBeReplaced);
            }
        }

        return s;
    }

    public static List<Sudoku> getSudokusFromFile(String fileName) throws IOException {
        List<Sudoku> sudokuList = new ArrayList<>();
        for (List<List<Integer>> currentGrid : GridUtils.getGridsFromFile(fileName)) {
            sudokuList.add(new Sudoku(currentGrid));
        }
        return sudokuList;
    }

    /**
     * @param puzzleFileName
     * @param solutionFileName
     * @param timeOut
     * @return The number of pairs of sudoku puzzles and their solutions
     * successfully appended in respective file
     * @throws IOException
     */
    public static int appendRandomGeneratedSudoku(int dimension, String puzzleFileName, String solutionFileName,
                                                  long timeOut) throws IOException {
        long startTime = System.currentTimeMillis();
        int counter = 0;
        while (System.currentTimeMillis() - startTime < timeOut) {
            Sudoku s = Sudoku.getPartiallyFilledSudokuPuzzle(dimension);
            s.writeSudokuToFile(puzzleFileName, StandardOpenOption.APPEND);
            new CustomSudokuSolver(s)
                    .alwaysCalculatingCellWithLeastPossibility(true)
                    .randomize(true)
                    .solve();
            s.writeSudokuToFile(solutionFileName, StandardOpenOption.APPEND);
            counter++;
        }
        return counter;
    }

    List<List<Integer>> getCachedSolvedGrid() {
        return cachedSolvedGrid;
    }

    /**
     * This is a special setter. This is the ONLY recommended way to set the cached
     * grid if the cached grid is already set, this method does nothing, otherwise
     * it creates a new deep copy of someGrid and then assigns it to cached grid.
     * The cached grid should never updated in any other way.
     *
     * @param someGrid
     */
    void setCachedSolvedGrid(List<List<Integer>> someGrid) {
        if (cachedSolvedGrid == null && new Sudoku(someGrid).isSolutionOf(this)) {
            cachedSolvedGrid = GridUtils.getClonedGrid(someGrid);
        }
    }

    public int getDimensionOfGrid() {
        return dimensionOfGrid;
    }

    /**
     * This getter is package private as it can only be accessed by solvers in the
     * package and is not available to end-client (to avoid mutations in the actual
     * grid)
     *
     * @return
     */
    List<List<Integer>> getGrid() {
        return grid;
    }

    /**
     * This is a special setter which clones someGrid and then sets this.grid
     *
     * @param someGrid
     */
    void setGrid(List<List<Integer>> someGrid) {
        this.grid = GridUtils.getClonedGrid(someGrid);
    }

    void setGrid(String gridString) {
        this.grid = GridUtils.getGridFromStringFormat(gridString);
    }

    public int getCellValue(int row, int col) {
        return grid.get(row).get(col);
    }

    /**
     * This method checks if a sudoku is completely and correctly filled
     *
     * @return
     */
    public boolean isSolved() {
        for (int i = 0; i < dimensionOfGrid; i++) {
            if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfRow(grid, i)))
                return false;
            if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfColumn(grid, i)))
                return false;
            if (!GridUtils.isStrictComplyingList(dimensionOfGrid, GridUtils.getListOfInnerGrid(grid, i)))
                return false;
        }
        return true;
    }

    /**
     * This method ignores any empty cells in sudoku grid and just checks if there
     * are no violations of standard sudoku game.
     *
     * @return This method returns true if there are no breaking of the rules
     * defined by a standard sudoku game.
     */
    public boolean isValid() {
        for (int i = 0; i < dimensionOfGrid; i++) {
            if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfRow(grid, i)))
                return false;
            if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfColumn(grid, i)))
                return false;
            if (!GridUtils.isWeakComplyingList(dimensionOfGrid, GridUtils.getListOfInnerGrid(grid, i)))
                return false;
        }
        return true;
    }

    /**
     * @return The index number of the cell in the whole sudoku grid that has
     * minimum number of possibilities that can be filled in that cell. The
     * cells are indexed from 0. This method returns -1 when all the cells
     * are filled and there is no cell that is empty, so no meaning of least
     * number of possibilities. This method also returns -1 when there is no
     * possibility available to be filled in even if the cell is empty. This
     * situation can arise when the sudoku is not correctly filled and at
     * the current cell you have no choice of digit to be filled in.
     * <p>
     * For example, in a 9x9 sudoku grid the cells are indexed from 0 to 80
     */
    public int getCellWithLeastPossibility() {
        int counter = 0, minCounter = -1, minListSize = Integer.MAX_VALUE;
        for (int i = 0; i < dimensionOfGrid; i++) {
            for (int j = 0; j < dimensionOfGrid; j++) {
                int currentListSize = getPossibleValues(i, j).size();
                if (currentListSize > 0 && currentListSize < minListSize) {
                    minListSize = currentListSize;
                    minCounter = counter;
                    if (minListSize == 1)
                        return minCounter;
                }
                counter++;
            }
        }
        return minCounter;
    }

    /**
     * @return The index number of the cell in the whole sudoku grid that has
     * exactly one possibility that can be filled in. If there is no such
     * cell, -1 is returned. The cells are indexed from 0.
     * <p>
     * For example, in a 9x9 sudoku grid the cells are indexed from 0 to 80
     */
    public int getCellWithExactlyOnePossibility() {
        int counter = 0;
        for (int i = 0; i < dimensionOfGrid; i++) {
            for (int j = 0; j < dimensionOfGrid; j++) {
                int currentListSize = getPossibleValues(i, j).size();
                if (currentListSize == 1)
                    return counter;

                counter++;
            }
        }
        return -1;
    }

    /**
     * The following method takes in the coordinates of a cell in sudoku grid and
     * returns the list of possible values that can be filled in that cell without
     * violating the standard sudoku rule.
     * <p>
     * If the cell is already filled (not empty) then this method returns list with
     * one single element that is already present in that cell.
     * <p>
     * This method is a hot method. So try to optimize it as much as possible.
     *
     * @param row
     * @param col
     * @return Returns a list of possibilities that can be filled in this cell. If
     * the cell is already filled returns an empty list.
     */
    public List<Integer> getPossibleValues(int row, int col) {
        if (this.getCellValue(row, col) != GridUtils.EMPTY_CELL_VALUE)
            return Collections.EMPTY_LIST;
        SingleIntBitSet rowList = GridUtils.getListOfRowEff(grid, row);
        SingleIntBitSet colList = GridUtils.getListOfColumnEff(grid, col);

        int innerGridNumber = GridUtils.getInnerGridNumber(dimensionOfGrid, row, col);
        SingleIntBitSet innerGridList = GridUtils.getListOfInnerGridEff(grid, innerGridNumber);

        SingleIntBitSet elements = new SingleIntBitSet(grid.size() + 1);
        IntConsumer intConsumer = e -> elements.set(e);
        SingleIntBitSet.setBitsConsumer(intConsumer, rowList, colList, innerGridList);

        List<Integer> resultList = new ArrayList<>(dimensionOfGrid);
        for (int i = 0; i < dimensionOfGrid; i++) {
            if (!elements.isSet(i + 1))
                resultList.add(i + 1);
        }
        return resultList;
    }

    /**
     * this function don't alters the original sudoku
     *
     * @return <code>true</code> if this sudoku has a unique solution
     */
    public boolean hasUniqueSolution() {
        Sudoku tempSudoku = getClonedSudoku();
        int solutions = uniqueSolutionDecider(tempSudoku, tempSudoku.getCellWithLeastPossibility());
        if (solutions >= 1) {
            this.setCachedSolvedGrid(tempSudoku.cachedSolvedGrid);
        }
        return solutions == 1;
    }

    public Sudoku getClonedSudoku() {
        List<List<Integer>> tempGrid = GridUtils.getClonedGrid(grid);
        return new Sudoku(tempGrid);
    }

    public void setCellValue(int row, int col, int value) {
        grid.get(row).set(col, value);
    }

    public void clearCell(int row, int col) {
        setCellValue(row, col, GridUtils.EMPTY_CELL_VALUE);
    }

    @Override
    public String toString() {
        return GridUtils.toPrettyString(grid);
    }

    public void writeSudokuToFile(String fileName, OpenOption... openOptions) throws IOException {
        GridUtils.writeGridToFile(fileName, getGrid(), openOptions);
    }

    /**
     * This method assumes that this sudoku should be solved.
     *
     * @param unsolvedSudoku
     * @return true if this is solved version of unsolvedSudoku, flase otherwise
     */
    public boolean isSolutionOf(Sudoku unsolvedSudoku) {
        if (this.dimensionOfGrid != unsolvedSudoku.dimensionOfGrid)
            return false;
        if (!this.isSolved())
            return false;
        for (int i = 0; i < dimensionOfGrid; i++) {
            for (int j = 0; j < dimensionOfGrid; j++) {
                if (unsolvedSudoku.getCellValue(i, j) != GridUtils.EMPTY_CELL_VALUE
                        && this.getCellValue(i, j) != unsolvedSudoku.getCellValue(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dimensionOfGrid;
        result = prime * result + dimensionOfInnerGrid;
        result = prime * result + ((grid == null) ? 0 : grid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sudoku other = (Sudoku) obj;
        if (dimensionOfGrid != other.dimensionOfGrid)
            return false;
        if (dimensionOfInnerGrid != other.dimensionOfInnerGrid)
            return false;
        if (grid == null) {
            return other.grid == null;
        } else {
            for (int i = 0; i < dimensionOfGrid; i++) {
                for (int j = 0; j < dimensionOfGrid; j++) {
                    if (this.getCellValue(i, j) != other.getCellValue(i, j))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * @param row
     * @return a list of list such that possibleIndices.get(0) will represents list
     * of co-ordinates where 1 can be safely filled, <br/>
     * possibleIndices.get(1) will represents list of indices where 2 can be
     * safely filled, <br/>
     * possibleIndices.get(2) will represents list of indices where 3 can be
     * safely filled, <br/>
     * possibleIndices.get(3) will represents list of indices where 4 can be
     * safely filled, <br/>
     * ... <br/>
     * possibleIndices.get(8) will represents list of indices where 9 can be
     * safely filled, <br/>
     * for a 9*9 sudoku in row number <code>row</code>
     */
    private List<List<CoOrdinate>> getIndicesWhereDigitsCanBePlacedInRow(int row) {
        List<List<CoOrdinate>> possibleIndices = new ArrayList<>(this.getDimensionOfGrid());
        for (int i = 0; i < this.getDimensionOfGrid(); i++)
            possibleIndices.add(new ArrayList<>());

        for (int j = 0; j < this.getDimensionOfGrid(); j++) {
            List<Integer> currentList = getPossibleValues(row, j);
            CoOrdinate coOrdinate = CoOrdinate.getCoOrdinate(row, j);
            for (Integer currentValue : currentList) {
                possibleIndices.get(currentValue - 1).add(coOrdinate);
            }
        }
        return possibleIndices;
    }

    private List<List<CoOrdinate>> getIndicesWhereDigitsCanBePlacedInColumn(int col) {
        List<List<CoOrdinate>> possibleIndices = new ArrayList<>(this.getDimensionOfGrid());
        for (int i = 0; i < this.getDimensionOfGrid(); i++)
            possibleIndices.add(new ArrayList<>(this.getDimensionOfGrid()));

        for (int i = 0; i < this.getDimensionOfGrid(); i++) {
            List<Integer> currentList = getPossibleValues(i, col);
            CoOrdinate coOrdinate = CoOrdinate.getCoOrdinate(i, col);
            for (int currentValue : currentList) {
                possibleIndices.get(currentValue - 1).add(coOrdinate);
            }
        }
        return possibleIndices;
    }

    private List<List<CoOrdinate>> getIndicesWhereDigitsCanBePlacedInInnerBlock(int innerGridNumber) {
        List<List<CoOrdinate>> possibleIndices = new ArrayList<>(this.getDimensionOfGrid());
        for (int i = 0; i < this.getDimensionOfGrid(); i++)
            possibleIndices.add(new ArrayList<>());

        int Y = innerGridNumber % dimensionOfInnerGrid;
        int X = innerGridNumber / dimensionOfInnerGrid;
        int startXIndex = X * dimensionOfInnerGrid;
        int startYIndex = Y * dimensionOfInnerGrid;

        for (int i = startXIndex; i < startXIndex + dimensionOfInnerGrid; i++) {
            for (int j = startYIndex; j < startYIndex + dimensionOfInnerGrid; j++) {
                for (final Integer currentValue : getPossibleValues(i, j)) {
                    possibleIndices.get(currentValue - 1).add(CoOrdinate.getCoOrdinate(i, j));
                }
            }
        }
        return possibleIndices;
    }

    /**
     * This method checks and fills the only possible position where a digit can be
     * filled in a row, column or in a grid.<br/>
     * Suppose there is only one place in a specific row where we can fill 7 and no
     * other place in that row where 7 can be filled, then this method identifies
     * this for that each row, column and Inner grid and fill it in a deterministic
     * way.
     */
    private void fillBySeeingPossibilitiesWhereDigitCanBeFilled() {

        List<List<CoOrdinate>> possibleIndices;

        for (int counter = 0; counter < this.getDimensionOfGrid(); counter++) {
            // for rows
            possibleIndices = getIndicesWhereDigitsCanBePlacedInRow(counter);
            fillAccordingToListOfIndices(possibleIndices);

            // for columns
            possibleIndices = getIndicesWhereDigitsCanBePlacedInColumn(counter);
            fillAccordingToListOfIndices(possibleIndices);

            // for inner grids
            possibleIndices = getIndicesWhereDigitsCanBePlacedInColumn(counter);
            fillAccordingToListOfIndices(possibleIndices);
        }
    }

    /**
     * This method deterministically fills in all the possible cells
     */
    public void solveUsingDeterministicTechniques() {
        String startStringRep, endStringRep;
        do {
            startStringRep = GridUtils.getStringFromGrid(this.grid);

            fillBySeeingPossibilitiesWhereDigitCanBeFilled();
            fillBySeeingPossibilitiesOfDigitsInCell();

            endStringRep = GridUtils.getStringFromGrid(this.grid);
        } while (!startStringRep.equals(endStringRep));
    }

    private void fillBySeeingPossibilitiesOfDigitsInCell() {

        List<List<List<Integer>>> possibilities = getPossibilitiesForEachCell();
        /*
         * It might be the case that pruning naked doubles may prove computationally
         * expensive. This pruning is not always good because the benefit that it
         * provides might be expensive (not worth it) when compared to the good it did.
         */
        prunePossibilitiesUsingNakedDoubles(possibilities);
        for (int i = 0; i < this.getDimensionOfGrid(); i++) {
            for (int j = 0; j < this.getDimensionOfGrid(); j++) {
                List<Integer> possibilityList = possibilities.get(i).get(j);

                /*
                 * Filling Naked singles
                 */
                if (possibilities.size() == 1) {
                    this.setCellValue(i, j, possibilityList.get(0));
                }
            }
        }

    }

    private List<List<List<Integer>>> getPossibilitiesForEachCell() {
        List<List<List<Integer>>> possibilities = new ArrayList<>(this.getDimensionOfGrid());
        for (int i = 0; i < this.getDimensionOfGrid(); i++) {
            possibilities.add(new ArrayList<>(this.getDimensionOfGrid()));
            for (int j = 0; j < this.getDimensionOfGrid(); j++) {
                possibilities.get(i).add(null);
            }
        }

        for (int i = 0; i < this.getDimensionOfGrid(); i++) {
            for (int j = 0; j < this.getDimensionOfGrid(); j++) {
                List<Integer> possibilityList = this.getPossibleValues(i, j);
                // TODO: check if sorting is necessary
                Collections.sort(possibilityList);

                possibilities.get(i).set(j, possibilityList);

            }
        }
        return possibilities;
    }

    private void prunePossibilitiesUsingNakedDoubles(List<List<List<Integer>>> possibilities) {
        // for rows
        for (int row = 0; row < this.getDimensionOfGrid(); row++) {
            List<CoOrdinate> rowCoOrdinates = CoOrdinate.getCoOrdinateListOfRow(row, this.getDimensionOfGrid());
            pruneByCoOrdinates(rowCoOrdinates, possibilities);
        }

        // for cols
        for (int col = 0; col < this.getDimensionOfGrid(); col++) {
            List<CoOrdinate> colCoOrdinates = CoOrdinate.getCoOrdinateListOfColumn(col, this.getDimensionOfGrid());
            pruneByCoOrdinates(colCoOrdinates, possibilities);
        }

        // for innerGrid
        for (int gridNumber = 0; gridNumber < this.getDimensionOfGrid(); gridNumber++) {
            List<CoOrdinate> gridCoOrdinates = CoOrdinate.getCoOrdinateListOfInnerGrid(gridNumber,
                    this.getDimensionOfGrid());
            pruneByCoOrdinates(gridCoOrdinates, possibilities);
        }
    }

    /*
     * Basically this method is pruning naked doubles
     */
    private void pruneByCoOrdinates(List<CoOrdinate> coOrdinateList, List<List<List<Integer>>> possibilities) {

        List<CoOrdinate> listOfCoOrdinatesWith2Possibilities = coOrdinateList.stream()
                .filter(e -> possibilities.get(e.getX()).get(e.getY()).size() == 2).collect(Collectors.toList());

        for (int i = 0; i < listOfCoOrdinatesWith2Possibilities.size(); i++) {
            for (int j = i + 1; j < listOfCoOrdinatesWith2Possibilities.size(); j++) {
                CoOrdinate first = listOfCoOrdinatesWith2Possibilities.get(i);
                CoOrdinate second = listOfCoOrdinatesWith2Possibilities.get(j);

                List<Integer> firstList = possibilities.get(first.getX()).get(first.getY());
                List<Integer> secondList = possibilities.get(second.getX()).get(second.getY());
                if (firstList.size() == 2 && secondList.size() == 2 && firstList.equals(secondList)) {
                    // there is a possibility for pruning in row.

                    Integer dual1 = firstList.get(0);
                    Integer dual2 = firstList.get(1);

                    for (final CoOrdinate currentCoOrdinate : coOrdinateList) {
                        if (currentCoOrdinate.equals(first) || currentCoOrdinate.equals(second))
                            continue;
                        List<Integer> currentPossibilityList = possibilities.get(currentCoOrdinate.getX())
                                .get(currentCoOrdinate.getY());
                        currentPossibilityList.remove(dual1);
                        currentPossibilityList.remove(dual2);

                    }
                }
            }
        }

    }

    private void fillAccordingToListOfIndices(List<List<CoOrdinate>> possibleIndices) {
        for (int number = 0; number < this.getDimensionOfGrid(); number++) {
            if (possibleIndices.get(number).size() == 1) {
                CoOrdinate currentCoOrdinate = possibleIndices.get(number).get(0);
                this.setCellValue(currentCoOrdinate.getX(), currentCoOrdinate.getY(), number + 1);
            }
        }
    }

    public List<Integer> getRowValues(int rowNumber) {
        return GridUtils.getListOfRow(grid, rowNumber);
    }

    public List<Integer> getInnerGridValues(int innerGridNumber) {
        return GridUtils.getListOfInnerGrid(grid, innerGridNumber);
    }

    public List<Integer> getInnerGridValues(int innerGridNumber, boolean includeEmptyCells) {
        return GridUtils.getGenericListOfInnerGrid(grid, innerGridNumber, true);
    }

    public List<Integer> getColumnValues(int columnNumber) {
        return GridUtils.getListOfColumn(grid, columnNumber);
    }

}
