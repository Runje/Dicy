package games.runje.dicymodel.ai;

import java.util.List;

import games.runje.dicymodel.data.Move;

/**
 * Created by Thomas on 18.01.2015.
 */
public class Strategy
{
    public static Move getBestSwitchMove(List<Move> moves)
    {
        int max = 0;
        Move maxMove = null;
        for (Move move : moves)
        {
            if (move.getPoints() > max)
            {
                maxMove = move;
                max = move.getPoints();
            }
        }

        return maxMove;
    }
}
