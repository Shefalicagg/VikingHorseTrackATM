package com.viking.atm;

public class Horse {
    private final int number;
    private final String name;
    private final int odds;
    private boolean isWinner;

    public Horse(int number, String name, int odds) {
        this.number = number;
        this.name = name;
        this.odds = odds;
        this.isWinner = number == 1;
    }

    public int getNumber() { return number; }
    public String getName() { return name; }
    public int getOdds() { return odds; }
    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { this.isWinner = winner; }

    @Override
    public String toString() {
        return number + "," + name + "," + odds + "," + (isWinner ? "won" : "lost");
    }
}
