import java.awt.*;

public class Player {

    private int score = 0;
    private String name;
    private Team team;
    private Color color;

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void updateScore(int newScore) {
        score = newScore;
        team.updateScore();
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

}
