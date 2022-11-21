import java.util.ArrayList;

public class Team {

    private String name;
    private ArrayList<Player> players;
    private int score = 0;

    public Team(String name) {
        this.name = name;
    }

    public void addPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void updateScore() {
        score = 0;
        for (int i = 0; i < players.size(); i++) {
            score = score + players.get(i).getScore();
        }
    }

    public String getName(){
        return name;
    }

    public Player getPlayer(int player) {
        return players.get(player);
    }

    public Player getPlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                return players.get(i);
            }
        }
        return null;
    }

    public ArrayList<Player> getAllPlayers() {
        return players;
    }

    public int getPositionOfPlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
