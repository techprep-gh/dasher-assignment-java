package com.techprep.dasher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.techprep.dasher.errors.UnknownOrderException;

public class AssignmentService {
    private final RoundRobinRouter router;
    private final AvailableDashers pool = new AvailableDashers();
    private final RandomDasherPicker picker;
    private final Map<String, Assignment> active = new HashMap<>();
    private final Map<String, Dasher> dashers = new HashMap<>();

    public AssignmentService(RoundRobinRouter router, Random random) {
        this.router = router;
        this.picker = new RandomDasherPicker(pool, random);
    }

    public AssignmentService(RoundRobinRouter router) {
        this(router, new Random());
    }

    public void goOnline(Dasher dasher) {
        pool.add(dasher);
        dashers.put(dasher.id(), dasher);
    }

    public void goOffline(String dasherId) {
        pool.remove(dasherId);
    }

    public boolean isOnline(String dasherId) {
        return pool.contains(dasherId);
    }

    public List<String> availableDashers() {
        return pool.ids();
    }

    /** Offers the order to a random available dasher, or returns empty if nobody is free. */
    public Optional<Assignment> assign(Order order) {
        Optional<Dasher> dasher = picker.pick();
        if (dasher.isEmpty()) {
            return Optional.empty();
        }
        String node = router.route(order);
        pool.remove(dasher.get().id());
        Assignment assignment = new Assignment(order.id(), dasher.get().id(), node);
        active.put(order.id(), assignment);
        return Optional.of(assignment);
    }

    public Optional<Assignment> activeAssignment(String orderId) {
        return Optional.ofNullable(active.get(orderId));
    }

    /** Closes the delivery and puts the dasher back in the pool for the next order. */
    public void complete(String orderId) {
        Assignment assignment = active.remove(orderId);
        if (assignment == null) {
            throw new UnknownOrderException(orderId);
        }
        pool.add(dashers.get(assignment.dasherId()));
    }
}
