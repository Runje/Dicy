package games.runje.dicymodel;

import java.util.ArrayList;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 05.10.2014.
 */
public class Utilities
{
    private static int id;

    /**
     * Checks if a number is a square
     *
     * @param n number to check.
     * @return Whether n is a square
     */
    public static boolean isPerfectSquare(long n)
    {
        if (n < 0)
            return false;

        long tst = (long) (Math.sqrt(n) + 0.5);
        return tst * tst == n;
    }

    public static int getPointsFrom(ArrayList<PointElement> pointElements)
    {
        int points = 0;
        for (PointElement e : pointElements)
        {
            points += e.getPoints();
        }

        return points;
    }

    public static boolean coordsInBoard(Coords position, Board board)
    {
        return (position.column >= 0) && (position.row >= 0) && (position.row < board.getNumberOfRows()) && (position.column < board.getNumberOfColumns());
    }

    public static int generateViewId()
    {
        id++;
        return id;
    }


}
