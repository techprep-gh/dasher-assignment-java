package com.techprep.dasher;

import static com.techprep.dasher.Fixtures.dasher;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.techprep.dasher.errors.DasherAlreadyOnlineException;
import com.techprep.dasher.errors.UnknownDasherException;

public class AvailableDashersTest {
    private static AvailableDashers poolOf(String... ids) {
        AvailableDashers pool = new AvailableDashers();
        for (String id : ids) {
            pool.add(dasher(id));
        }
        return pool;
    }

    @Test
    public void addedDashersAreAvailable() {
        AvailableDashers pool = poolOf("d1", "d2");

        assertEquals(2, pool.size());
        assertTrue(pool.contains("d1") && pool.contains("d2"));
        assertEquals(List.of("d1", "d2"), pool.ids());
    }

    @Test
    public void addingTheSameDasherTwiceIsRejected() {
        AvailableDashers pool = poolOf("d1");

        assertThrows(DasherAlreadyOnlineException.class, () -> pool.add(dasher("d1")));
    }

    @Test
    public void removingADasherMovesTheLastOneIntoItsSlot() {
        AvailableDashers pool = poolOf("d1", "d2", "d3", "d4");

        Dasher removed = pool.remove("d2");

        assertEquals("d2", removed.id());
        assertEquals(List.of("d1", "d4", "d3"), pool.ids());
        assertFalse(pool.contains("d2"));
        assertEquals("d4", pool.at(1).id());
    }

    @Test
    public void removedDasherCanBeAddedAgain() {
        AvailableDashers pool = poolOf("d1", "d2");

        pool.remove("d1");
        pool.add(dasher("d1"));

        assertEquals(2, pool.size());
        assertTrue(pool.contains("d1"));
    }

    @Test
    public void removingAnUnknownDasherThrows() {
        AvailableDashers pool = poolOf("d1");

        assertThrows(UnknownDasherException.class, () -> pool.remove("d9"));
    }
}
