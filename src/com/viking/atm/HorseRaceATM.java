package com.viking.atm;

import java.util.*;

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

        horses.get(0).setWinner(true);
    }

    public void start() {
        printStatus();

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("q")) break;
            else if (input.equalsIgnoreCase("r")) {
                System.out.println(input);
                inventory.restock();
            } else if (input.toLowerCase().startsWith("w ")) {
                System.out.println(input);
                setWinner(input);
            } else {
                processBet(input);
            }

            printStatus();
        }
    }

    private void printStatus() {
        inventory.print();
        System.out.println("Horses:");
        horses.forEach(h -> System.out.printf("%d,%s,%d,%s%n",
                h.getNumber(), h.getName(), h.getOdds(), h.isWinner() ? "won" : "lost"));
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
            String betStr = parts[1];

            if (betStr.contains(".") || !betStr.matches("\\d+")) {
                System.out.println("Invalid Bet: " + betStr);
                return;
            }

            int betAmount = Integer.parseInt(betStr);
            Horse horse = getHorse(horseNum);
            if (horse == null) {
                System.out.println("Invalid Horse Number: " + horseNum);
                return;
            }

            if (!horse.isWinner()) {
                System.out.println("No Payout: " + horse.getName());
                return;
            }

            int payout = betAmount * horse.getOdds();
            Map<Integer, Integer> payoutMap = new LinkedHashMap<>();
            if (inventory.dispense(payout, payoutMap)) {
                System.out.println("Payout: " + horse.getName() + ",$" + payout);
                System.out.println("Dispensing:");
                List<Integer> order = Arrays.asList(1, 5, 10, 20, 100);
                for (int denom : order) {
                    System.out.println("$" + denom + "," + payoutMap.getOrDefault(denom, 0));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Command: " + input);
        }
    }

    private Horse getHorse(int number) {
        return horses.stream().filter(h -> h.getNumber() == number).findFirst().orElse(null);
    }
}
