package com.techprep.dasher;

import static com.techprep.dasher.Fixtures.routeMany;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.Test;

import com.techprep.dasher.errors.DispatchNodeAlreadyRegisteredException;
import com.techprep.dasher.errors.InvalidAvailabilityStateException;
import com.techprep.dasher.errors.UnknownDispatchNodeException;

public class RoundRobinRouterTest {
    private static RoundRobinRouter router(String... nodes) {
        return new RoundRobinRouter(List.of(nodes));
    }

    @Test
    public void nodesTakeTurnsInOrder() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2", "dispatch-3");

        assertEquals(
                List.of("dispatch-1", "dispatch-2", "dispatch-3", "dispatch-1", "dispatch-2", "dispatch-3"),
                routeMany(router, 6));
    }

    @Test
    public void aNodeMarkedUpAgainRejoinsTheRotation() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2", "dispatch-3");
        router.markDown("dispatch-2");
        router.markUp("dispatch-2");

        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-3"), routeMany(router, 3));
        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-3"), router.healthyNodes());
    }

    @Test
    public void healthyNodesExcludesNodesMarkedDown() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2", "dispatch-3");
        router.markDown("dispatch-3");

        assertEquals(List.of("dispatch-1", "dispatch-2"), router.healthyNodes());
    }

    @Test
    public void unknownNodeCannotBeMarked() {
        RoundRobinRouter router = router("dispatch-1");

        assertThrows(UnknownDispatchNodeException.class, () -> router.markDown("dispatch-9"));
    }

    @Test
    public void addedNodeJoinsTheEndOfTheRotation() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2");
        router.addNode("dispatch-3");

        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-3", "dispatch-1"), routeMany(router, 4));
        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-3"), router.healthyNodes());
    }

    @Test
    public void nodeAddedAtRuntimeAndMarkedDownIsSkipped() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2");
        router.addNode("dispatch-3");
        router.markDown("dispatch-3");

        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-1", "dispatch-2"), routeMany(router, 4));
    }

    @Test
    public void addingAKnownNodeIsRejected() {
        RoundRobinRouter router = router("dispatch-1");

        assertThrows(DispatchNodeAlreadyRegisteredException.class, () -> router.addNode("dispatch-1"));
    }

    @Test
    public void nodeSetAvailableByHealthCheckRejoinsTheRotation() {
        RoundRobinRouter router = router("dispatch-1", "dispatch-2", "dispatch-3");
        router.markDown("dispatch-2");
        router.setAvailability("dispatch-2", "AVAILABLE");

        assertEquals(List.of("dispatch-1", "dispatch-2", "dispatch-3"), routeMany(router, 3));
    }

    @Test
    public void unknownAvailabilityStateIsRejected() {
        RoundRobinRouter router = router("dispatch-1");

        assertThrows(InvalidAvailabilityStateException.class, () -> router.setAvailability("dispatch-1", "DOWN"));
    }
}
