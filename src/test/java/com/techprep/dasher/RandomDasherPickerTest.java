package com.techprep.dasher;

import static com.techprep.dasher.Fixtures.dasher;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

public class RandomDasherPickerTest {
    private static AvailableDashers poolOf(String... ids) {
        AvailableDashers pool = new AvailableDashers();
        for (String id : ids) {
            pool.add(dasher(id));
        }
        return pool;
    }

    private static Set<String> pickMany(RandomDasherPicker picker, int count) {
        Set<String> picked = new HashSet<>();
        for (int i = 0; i < count; i++) {
            picked.add(picker.pick().orElseThrow().id());
        }
        return picked;
    }

    @Test
    public void pickReturnsAnAvailableDasher() {
        RandomDasherPicker picker = new RandomDasherPicker(poolOf("d1", "d2", "d3"), new Random(1));

        assertTrue(Set.of("d1", "d2", "d3").containsAll(pickMany(picker, 20)));
    }

    @Test
    public void pickEventuallyReachesEveryDasher() {
        RandomDasherPicker picker = new RandomDasherPicker(poolOf("d1", "d2", "d3", "d4"), new Random(3));

        assertEquals(Set.of("d1", "d2", "d3", "d4"), pickMany(picker, 200));
    }

    @Test
    public void pickNeverReturnsARemovedDasher() {
        AvailableDashers pool = poolOf("d1", "d2", "d3");
        pool.remove("d1");
        RandomDasherPicker picker = new RandomDasherPicker(pool, new Random(5));

        assertEquals(Set.of("d2", "d3"), pickMany(picker, 100));
    }
}
