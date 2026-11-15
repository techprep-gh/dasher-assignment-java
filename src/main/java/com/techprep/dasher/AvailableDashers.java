package com.techprep.dasher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techprep.dasher.errors.DasherAlreadyOnlineException;
import com.techprep.dasher.errors.UnknownDasherException;

/** Dashers waiting for an offer, with O(1) add, remove and access by position. */
public class AvailableDashers {
    private final List<Dasher> dashers = new ArrayList<>();
    private final Map<String, Integer> positions = new HashMap<>();

    public int size() {
        return dashers.size();
    }

    public boolean contains(String dasherId) {
        return positions.containsKey(dasherId);
    }

    public void add(Dasher dasher) {
        if (positions.containsKey(dasher.id())) {
            throw new DasherAlreadyOnlineException(dasher.id());
        }
        positions.put(dasher.id(), dashers.size());
        dashers.add(dasher);
    }

    public Dasher remove(String dasherId) {
        Integer position = positions.remove(dasherId);
        if (position == null) {
            throw new UnknownDasherException(dasherId);
        }
        Dasher removed = dashers.get(position);
        Dasher last = dashers.remove(dashers.size() - 1);
        if (position < dashers.size()) {
            dashers.set(position, last);
            positions.put(last.id(), position);
        }
        return removed;
    }

    public Dasher at(int position) {
        return dashers.get(position);
    }

    public List<String> ids() {
        return dashers.stream().map(Dasher::id).toList();
    }
}
