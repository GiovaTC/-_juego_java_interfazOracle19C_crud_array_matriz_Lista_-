package model;

public class Player {
    private int id;
    private String name;
    private int attempts;
    private String result;

    public Player(int id, String name, int attempts, String result) {
        this.id = id;
        this.name = name;
        this.attempts = attempts;
        this.result = result;
    }

    public Player(String name, int attempts, String result) {
        this.name = name;
        this.attempts = attempts;
        this.result = result;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAttempts() { return attempts; }
    public String getResult() { return result; }
}
