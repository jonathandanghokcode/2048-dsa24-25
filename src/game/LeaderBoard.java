
package game;

import java.io.*;
import java.util.*;

public class LeaderBoard {
    private List<Player> players;


    public LeaderBoard() {
        players = new ArrayList<>();
        load();
    }

    public void addPlayer(Player player) {
        players.add(player);
        save();
    }

    public List<Player> getTopPlayers(int n) {
        players.sort((player1, player2) -> Integer.compare(player2.getScore(), player1.getScore()));
        return players.subList(0, Math.min(n, players.size()));
    }

    private void save() {
        try (PrintWriter writer = new PrintWriter(new File("leaderboard.txt"))) {
            for (Player player : players) {
                writer.println(player.getName() + "," + player.getScore());
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void load() {
        try (Scanner scanner = new Scanner(new File("leaderboard.txt"))) {
            while (scanner.hasNextLine()) {
                String[] playerData = scanner.nextLine().split(",");
                String name = playerData[0];
                int score = Integer.parseInt(playerData[1]);
                Player player = new Player(name, score);
                players.add(player);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
