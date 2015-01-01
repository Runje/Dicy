package games.runje.dicymodeltests;

import org.junit.Test;

import java.util.ArrayList;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thomas on 13.10.2014.
 */
public class getAllTests
{
    @Test
    public void allPoints()
    {
        Rules rules = new Rules();
        rules.setDiagonalActive(true);
        rules.setMinStraight(3);
        rules.initStraightPoints(2);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)}, Orientation.Down, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 1), new Coords(2, 2)}, Orientation.DownRight, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(0, 0), new Coords(0, 1), new Coords(0, 2)}, Orientation.Right, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(0, 2), new Coords(1, 2), new Coords(2, 2)}, Orientation.Down, rules.getStraightPoints(3)));
        pointsHelper(new int[]{1, 2, 3,
                        1, 1, 2,
                        1, 2, 1},
                expectedElements, rules);
    }

    private void pointsHelper(int[] board, ArrayList<PointElement> expectedElements, Rules rules)
    {
        Board b = Board.createElementsBoard(board);
        ArrayList<PointElement> elements = BoardChecker.getAll(b, rules);
        assertEquals(expectedElements, elements);
    }

}
