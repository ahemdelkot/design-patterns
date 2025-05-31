package Observer;

import java.util.ArrayList;
import java.util.List;

public class Ruler {
    private int player1Score = 0;
    private int player2Score = 0;
    List<Players> playersList = new ArrayList<>() ;

    public void startGame(Players players){
        playersList.add(players);
    }
    public void endGame(Players players){
        playersList.remove(players);
    }
    public void notifyPlayer(){
        for (Players players : playersList) {
            players.update(player1Score , player2Score);
        }
    }
    public void increasingPlayer1Score(){
        ++player1Score;
        notifyPlayer();
    }
    public void increasingPlayer2Score(){
        ++player2Score;
        notifyPlayer();
    }
    public int getPlayer1Score(){
        return player1Score;
    }
    public int getPlayer2Score(){
        return player2Score;
    }
    public void reset(Players players){
        player1Score = 0;
        player2Score = 0;
        players.move = true;
    }
}
