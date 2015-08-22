package games.runje.dicymodel.game;

import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 19.08.2015.
 */
public interface StatisticHandler
{

    void movePoints(int movePoints, Player playingPlayer);

    void switchPoints(int switchPoints, Player playingPlayer);
}
