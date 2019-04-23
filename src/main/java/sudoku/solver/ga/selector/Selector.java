package sudoku.solver.ga.selector;

import sudoku.solver.ga.WeightedEntity;

public interface Selector<T extends WeightedEntity> {
    T selectNext();
}
