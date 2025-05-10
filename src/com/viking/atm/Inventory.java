package com.viking.atm;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Horse Track ATM Simulation
 * Created by Shefali Aggarwal
 */

public class Inventory {
    private final Map<Integer, Integer> cash = new LinkedHashMap<>();

    public Inventory() { restock(); }

    public void restock() {
        cash.put(100, 10);
        cash.put(20, 10);
        cash.put(10, 10);
        cash.put(5, 10);
        cash.put(1, 10);
    }

    public boolean dispense(int amount, Map<Integer, Integer> result) {
        result.clear();
        int tempAmount = amount;
        Map<Integer, Integer> temp = new LinkedHashMap<>(cash);

        for (int denom : temp.keySet()) {
            int count = Math.min(tempAmount / denom, temp.get(denom));
            if (count > 0) {
                result.put(denom, count);
                tempAmount -= denom * count;
            }
        }

        if (tempAmount == 0) {
            result.forEach((k, v) -> cash.put(k, cash.get(k) - v));
            return true;
        }
        return false;
    }

    public void print() {
        System.out.println("Inventory:");
        for (Map.Entry<Integer, Integer> entry : cash.entrySet()) {
            System.out.println("$" + entry.getKey() + "," + entry.getValue());
        }
    }
}
