package sudoku.solver.ga.selector;

import sudoku.solver.ga.WeightedEntity;

import java.util.List;

public class WeightedRandomSelector<T extends WeightedEntity> implements Selector<T> {

    final List<T> elements;

    public WeightedRandomSelector(List<T> list) {
        elements = list;
    }

    @Override
    public T selectNext() {
        double weightSum = elements.stream().mapToDouble(x -> x.getWeight()).reduce(0.0, (x, y) -> x + y);
        /*
         * for a good distribution we can shuffle. but running it shows that shuffle
         * don't have much improvements.
         */
        double randomSum = Math.random() * weightSum;

        for (T ele : elements) {
            randomSum = randomSum - ele.getWeight();
            if (randomSum < 0.0)
                return ele;
        }
        // should never happen
        return null;
    }
}
