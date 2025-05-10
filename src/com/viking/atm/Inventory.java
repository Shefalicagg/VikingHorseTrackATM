package com.viking.atm;

import java.util.*;

public class Inventory {
    private final Map<Integer, Integer> cash = new TreeMap<>(Collections.reverseOrder());

    public Inventory() {
        cash.put(100, 10);
        cash.put(20, 10);
        cash.put(10, 10);
        cash.put(5, 10);
        cash.put(1, 10);
    }

    public void restock() {
        cash.replaceAll((k, v) -> 10);
    }

    public void print() {
        System.out.println("Inventory:");
        for (int denom : Arrays.asList(1, 5, 10, 20, 100)) {
            System.out.printf("$%d,%d%n", denom, cash.getOrDefault(denom, 0));
        }
    }

    public boolean dispense(int amount, Map<Integer, Integer> payoutMap) {
        Map<Integer, Integer> original = new TreeMap<>(cash);
        Map<Integer, Integer> result = tryDispense(amount, new ArrayList<>(cash.keySet()), 0, new LinkedHashMap<>());

        if (result == null) {
            System.out.println("Cannot dispense exact amount. Remaining: $" + amount);
            return false;
        }

        // Deduct from inventory and populate payoutMap
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            int denom = entry.getKey();
            int count = entry.getValue();
            payoutMap.put(denom, count);
            cash.put(denom, cash.get(denom) - count);
        }
        return true;
    }

    private Map<Integer, Integer> tryDispense(int amount, List<Integer> denoms, int index, Map<Integer, Integer> current) {
        if (amount == 0) return new LinkedHashMap<>(current);
        if (index >= denoms.size()) return null;

        int denom = denoms.get(index);
        int maxCount = Math.min(amount / denom, cash.get(denom));

        for (int i = maxCount; i >= 0; i--) {
            current.put(denom, i);
            Map<Integer, Integer> result = tryDispense(amount - i * denom, denoms, index + 1, current);
            if (result != null) return result;
        }
        current.remove(denom);
        return null;
    }
}
