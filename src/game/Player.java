package game;

public class Player {
    private String name;
    private int score;

    // constructor, getters, setters, etc.
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
}

