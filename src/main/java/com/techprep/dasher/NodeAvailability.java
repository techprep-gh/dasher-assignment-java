package com.techprep.dasher;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.techprep.dasher.errors.InvalidAvailabilityStateException;
import com.techprep.dasher.errors.UnknownDispatchNodeException;

/** Availability state per dispatch node, as reported by health checks. */
public class NodeAvailability {
    public static final String AVAILABLE = "AVAILABLE";
    public static final String UNAVAILABLE = "UNAVAILABLE";
    private static final Set<String> STATES = Set.of(AVAILABLE, UNAVAILABLE);

    private final Map<String, String> states = new HashMap<>();

    public NodeAvailability(Iterable<String> nodes) {
        for (String node : nodes) {
            register(node);
        }
    }

    public void register(String node) {
        states.put(node, AVAILABLE);
    }

    public void setState(String node, String state) {
        if (!states.containsKey(node)) {
            throw new UnknownDispatchNodeException(node);
        }
        if (!STATES.contains(state)) {
            throw new InvalidAvailabilityStateException(state);
        }
        states.put(node, state);
    }

    public boolean isAvailable(String node) {
        return AVAILABLE.equals(states.get(node));
    }
}
