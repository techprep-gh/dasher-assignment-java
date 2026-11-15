package com.techprep.dasher;

import java.util.Optional;
import java.util.Random;

/** Offers each order to a uniformly random available dasher. */
public class RandomDasherPicker {
    private final AvailableDashers pool;
    private final Random random;

    public RandomDasherPicker(AvailableDashers pool, Random random) {
        this.pool = pool;
        this.random = random;
    }

    public Optional<Dasher> pick() {
        if (pool.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(pool.at(random.nextInt(pool.size())));
    }
}
