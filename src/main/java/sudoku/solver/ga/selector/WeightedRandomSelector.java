package sudoku.solver.ga.selector;

import sudoku.solver.ga.WeightedEntity;

import java.util.List;
import java.util.function.Supplier;

public class WeightedRandomSelector<T extends WeightedEntity> implements Supplier<T> {

    final List<T> elements;

    public WeightedRandomSelector(List<T> list) {
        elements = list;
    }

    @Override
    public T get() {
        double weightSum = elements.stream()
                .mapToDouble(WeightedEntity::getWeight)
                .reduce(0.0, (x, y) -> x + y);
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
        throw new IllegalStateException("this should never happen.");
    }
}
