package com.techprep.dasher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Fixtures {
    private Fixtures() {}

    public static AssignmentService service() {
        return new AssignmentService(new RoundRobinRouter(List.of("dispatch-1", "dispatch-2")), new Random(7));
    }

    public static Dasher dasher(String id) {
        return new Dasher(id, "Dasher " + id);
    }

    public static Order order(String id) {
        return new Order(id, "Golden Wok", "12 Mission St");
    }

    public static List<String> routeMany(RoundRobinRouter router, int count) {
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            nodes.add(router.route(order("o" + i)));
        }
        return nodes;
    }
}
