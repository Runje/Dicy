package games.runje.dicymodel.game;

import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 16.11.2015.
 */
public class TourneyGame
{
    Player player1;

    public Player getPlayer1()
    {
        return player1;
    }

    public Player getPlayer2()
    {
        return player2;
    }

    public TourneyGame(Player player1, Player player2)
    {

        this.player1 = player1;
        this.player2 = player2;
    }

    Player player2;
}
