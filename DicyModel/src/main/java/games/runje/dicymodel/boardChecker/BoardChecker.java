package games.runje.dicymodel.boardChecker;

import java.util.ArrayList;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 05.10.2014.
 */
public class BoardChecker
{
    /**
     * Gets the PointElements for XofAKind.
     *
     * @param board Board to look in.
     * @param rules Rules
     * @return PointElements for XofAKind in this board for these rules.
     */
    public static ArrayList<PointElement> getXOfAKinds(Board board, Rules rules)
    {
        return new XOfAKindFinder(board, rules).find();
    }

    /**
     * Gets the PointElements for straights.
     *
     * @param board Board to look in.
     * @param rules Rules
     * @return PointElements for straights in this board for these rules.
     */
    public static ArrayList<PointElement> getStraights(Board board, Rules rules)
    {
        return new StraightFinder(board, rules).find();
    }

    /**
     * Gets all PointElements.
     *
     * @param board Board to look in.
     * @param rules Rules
     * @return All PointElements in this board for these rules.
     */
    public static ArrayList<PointElement> getAll(Board board, Rules rules)
    {
        ArrayList<PointElement> all = getXOfAKinds(board, rules);
        all.addAll(getStraights(board, rules));
        return all;
    }
}
