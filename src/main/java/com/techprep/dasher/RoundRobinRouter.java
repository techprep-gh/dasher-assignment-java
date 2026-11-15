package com.techprep.dasher;

import java.util.ArrayList;
import java.util.List;

import com.techprep.dasher.errors.DispatchNodeAlreadyRegisteredException;
import com.techprep.dasher.errors.NoHealthyDispatchNodeException;

/** Spreads offers across dispatch nodes in turn, skipping nodes that are unavailable. */
public class RoundRobinRouter {
    private final List<String> nodes;
    private final NodeAvailability availability;
    private int cursor = 0;

    public RoundRobinRouter(List<String> nodes) {
        this.nodes = new ArrayList<>(nodes);
        this.availability = new NodeAvailability(this.nodes);
    }

    public void addNode(String node) {
        if (nodes.contains(node)) {
            throw new DispatchNodeAlreadyRegisteredException(node);
        }
        nodes.add(node);
        availability.register(node);
    }

    public void setAvailability(String node, String state) {
        availability.setState(node, state);
    }

    public void markDown(String node) {
        setAvailability(node, NodeAvailability.UNAVAILABLE);
    }

    public void markUp(String node) {
        setAvailability(node, NodeAvailability.AVAILABLE);
    }

    public List<String> healthyNodes() {
        return nodes.stream().filter(availability::isAvailable).toList();
    }

    /** Picks the dispatch node for this order. Round robin does not look at the order. */
    public String route(Order order) {
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(cursor);
            cursor = (cursor + 1) % nodes.size();
            if (availability.isAvailable(node)) {
                return node;
            }
        }
        throw new NoHealthyDispatchNodeException();
    }
}
