package com.techprep.dasher;

import static com.techprep.dasher.Fixtures.dasher;
import static com.techprep.dasher.Fixtures.order;
import static com.techprep.dasher.Fixtures.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import com.techprep.dasher.errors.UnknownDasherException;
import com.techprep.dasher.errors.UnknownOrderException;

public class AssignmentServiceTest {
    @Test
    public void assignOffersTheOrderToAnOnlineDasher() {
        AssignmentService service = service();
        service.goOnline(dasher("d1"));
        service.goOnline(dasher("d2"));

        Assignment assignment = service.assign(order("o1")).orElseThrow();

        assertEquals("o1", assignment.orderId());
        assertTrue(Set.of("d1", "d2").contains(assignment.dasherId()));
        assertEquals("dispatch-1", assignment.dispatchNode());
        assertEquals(Optional.of(assignment), service.activeAssignment("o1"));
    }

    @Test
    public void ordersAreSpreadAcrossDispatchNodes() {
        AssignmentService service = service();
        for (String id : List.of("d1", "d2", "d3")) {
            service.goOnline(dasher(id));
        }

        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            nodes.add(service.assign(order("o" + i)).orElseThrow().dispatchNode());
        }

        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-1"), nodes);
    }

    @Test
    public void eachOrderGoesToADifferentDasher() {
        AssignmentService service = service();
        for (String id : List.of("d1", "d2", "d3")) {
            service.goOnline(dasher(id));
        }

        Set<String> dashers = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            dashers.add(service.assign(order("o" + i)).orElseThrow().dasherId());
        }

        assertEquals(Set.of("d1", "d2", "d3"), dashers);
    }

    @Test
    public void completingAnUnknownOrderThrows() {
        AssignmentService service = service();

        assertThrows(UnknownOrderException.class, () -> service.complete("o404"));
    }

    @Test
    public void goingOfflineTwiceThrows() {
        AssignmentService service = service();
        service.goOnline(dasher("d1"));
        service.goOnline(dasher("d2"));
        service.goOffline("d1");

        assertThrows(UnknownDasherException.class, () -> service.goOffline("d1"));
    }
}
