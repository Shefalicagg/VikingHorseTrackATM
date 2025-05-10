package com.viking.atm;

import java.util.*;

/**
 * Horse Track ATM Simulation
 * Created by Shefali Aggarwal
 */

public class HorseRaceATM {
    private final List<Horse> horses = new ArrayList<>();
    private final Inventory inventory = new Inventory();
    private final Scanner scanner = new Scanner(System.in);

    public HorseRaceATM() {
        horses.add(new Horse(1, "That Darn Gray Cat", 5));
        horses.add(new Horse(2, "Fort Utopia", 10));
        horses.add(new Horse(3, "Count Sheep", 9));
        horses.add(new Horse(4, "Ms Traitour", 4));
        horses.add(new Horse(5, "Real Princess", 3));
        horses.add(new Horse(6, "Pa Kettle", 5));
        horses.add(new Horse(7, "Gin Stinger", 6));
    }

    public void start() {
        printStatus();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("q")) break;
            else if (input.equalsIgnoreCase("r")) inventory.restock();
            else if (input.toLowerCase().startsWith("w ")) setWinner(input);
            else processBet(input);

            printStatus();
        }
    }

    private void printStatus() {
        inventory.print();
        System.out.println("Horses:");
        horses.forEach(System.out::println);
    }

    private void setWinner(String input) {
        try {
            int num = Integer.parseInt(input.substring(2).trim());
            Horse winner = getHorse(num);
            if (winner == null) {
                System.out.println("Invalid Horse Number: " + num);
                return;
            }
            horses.forEach(h -> h.setWinner(false));
            winner.setWinner(true);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Command: " + input);
        }
    }

    private void processBet(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            System.out.println("Invalid Command: " + input);
            return;
        }

        try {
            int horseNum = Integer.parseInt(parts[0]);
            double bet = Double.parseDouble(parts[1]);
            if (bet != (int) bet) {
                System.out.println("Invalid Bet: " + parts[1]);
                return;
            }

            Horse horse = getHorse(horseNum);
            if (horse == null) {
                System.out.println("Invalid Horse Number: " + horseNum);
                return;
            }

            if (!horse.isWinner()) {
                System.out.println("No Payout: " + horse.getName());
                return;
            }

            int payout = (int) bet * horse.getOdds();
            Map<Integer, Integer> payoutMap = new LinkedHashMap<>();
            if (inventory.dispense(payout, payoutMap)) {
                System.out.println("Payout: " + horse.getName() + ",$" + payout);
                System.out.println("Dispensing:");
                payoutMap.forEach((k, v) -> System.out.println("$" + k + "," + v));
            } else {
                System.out.println("Insufficient Funds: $" + payout);
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid Command: " + input);
        }
    }

    private Horse getHorse(int number) {
        return horses.stream().filter(h -> h.getNumber() == number).findFirst().orElse(null);
    }
}
