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
 * Created by Thomas on 08.10.2014.
 */
public class GetStraightsTests
{
    @Test
    public void none()
    {
        noneHelper(new int[]{1, 2, 1,
                1, 3, 3,
                3, 1, 3});
        noneHelper(new int[]{1, 1, 2,
                1, 1, 2,
                3, 3, 1});
        noneHelper(new int[]{2, 2, 1,
                3, 2, 2,
                1, 3, 2});
    }

    private void noneHelper(int[] board)
    {
        Board b = Board.createElementsBoard(board);
        Rules rules = new Rules();
        rules.setMinStraight(3);
        ArrayList<PointElement> elements = BoardChecker.getStraights(b, rules);

        assertEquals(new ArrayList<PointElement>(), elements);
    }

    @Test
    public void pointsRow()
    {
        Rules rules = new Rules();
        rules.setMinStraight(3);
        rules.initStraightPoints(2);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(0, 0), new Coords(0, 1), new Coords(0, 2)}, Orientation.Right, rules.getStraightPoints(3)));
        pointsHelper(new int[]{1, 2, 3,
                        1, 3, 2,
                        3, 1, 3},
                expectedElements, rules);

        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(1, 0), new Coords(1, 1), new Coords(1, 2)}, Orientation.Right, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(2, 0), new Coords(2, 1), new Coords(2, 2)}, Orientation.Right, rules.getStraightPoints(3)));
        pointsHelper(new int[]{1, 2, 3,
                        3, 2, 1,
                        1, 2, 3},
                expectedElements, rules);


        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.Straight, 5, 5, new Coords[]{new Coords(0, 0), new Coords(0, 1), new Coords(0, 2), new Coords(0, 3), new Coords(0, 4)}, Orientation.Right, rules.getStraightPoints(5)));
        expectedElements.add(new PointElement(PointType.Straight, 5, 5, new Coords[]{new Coords(1, 0), new Coords(1, 1), new Coords(1, 2), new Coords(1, 3), new Coords(1, 4)}, Orientation.Right, rules.getStraightPoints(5)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(2, 2), new Coords(2, 3), new Coords(2, 4)}, Orientation.Right, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 6, new Coords[]{new Coords(3, 0), new Coords(3, 1), new Coords(3, 2)}, Orientation.Right, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 6, new Coords[]{new Coords(4, 1), new Coords(4, 2), new Coords(4, 3)}, Orientation.Right, rules.getStraightPoints(3)));

        pointsHelper(new int[]{1, 2, 3, 4, 5,
                        5, 4, 3, 2, 1,
                        1, 2, 1, 2, 3,
                        6, 5, 4, 4, 5,
                        5, 4, 5, 6, 5},
                expectedElements, rules);
    }

    @Test
    public void pointsColumn()
    {
        Rules rules = new Rules();
        rules.setMinStraight(3);
        rules.initStraightPoints(2);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)}, Orientation.Down, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 3, new Coords[]{new Coords(2, 0), new Coords(3, 0), new Coords(4, 0)}, Orientation.Down, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 4, new Coords[]{new Coords(2, 1), new Coords(3, 1), new Coords(4, 1)}, Orientation.Down, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 6, new Coords[]{new Coords(2, 2), new Coords(3, 2), new Coords(4, 2)}, Orientation.Down, rules.getStraightPoints(3)));
        expectedElements.add(new PointElement(PointType.Straight, 5, 5, new Coords[]{new Coords(0, 3), new Coords(1, 3), new Coords(2, 3), new Coords(3, 3), new Coords(4, 3)}, Orientation.Down, rules.getStraightPoints(5)));
        expectedElements.add(new PointElement(PointType.Straight, 3, 6, new Coords[]{new Coords(0, 4), new Coords(1, 4), new Coords(2, 4)}, Orientation.Down, rules.getStraightPoints(3)));
        pointsHelper(new int[]{1, 2, 6, 1, 6,
                        2, 6, 4, 2, 5,
                        3, 2, 6, 3, 4,
                        2, 3, 5, 4, 6,
                        1, 4, 4, 5, 2},
                expectedElements, rules);
    }

    @Test
    public void pointsDiagonalDownRight()
    {
        Rules rules = new Rules();
        rules.setMinStraight(4);
        rules.initStraightPoints(2);
        rules.setDiagonalActive(true);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.Straight, 4, 6, new Coords[]{new Coords(1, 1), new Coords(2, 2), new Coords(3, 3), new Coords(4, 4)}, Orientation.DownRight, rules.getStraightPoints(4)));
        pointsHelper(new int[]{1, 2, 6, 6, 6,
                        2, 6, 4, 2, 5,
                        3, 2, 5, 3, 4,
                        2, 3, 5, 4, 6,
                        1, 4, 5, 4, 3},
                expectedElements, rules);
    }

    @Test
    public void pointsDiagonalDownLeft()
    {
        Rules rules = new Rules();
        rules.setMinStraight(4);
        rules.initStraightPoints(2);
        rules.setDiagonalActive(true);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.Straight, 4, 4, new Coords[]{new Coords(1, 3), new Coords(2, 2), new Coords(3, 1), new Coords(4, 0)}, Orientation.DownLeft, rules.getStraightPoints(4)));
        pointsHelper(new int[]{1, 2, 6, 6, 6,
                        2, 6, 4, 4, 5,
                        3, 2, 3, 3, 4,
                        2, 2, 5, 4, 6,
                        1, 4, 5, 4, 3},
                expectedElements, rules);
    }

    private void pointsHelper(int[] board, ArrayList<PointElement> expectedElements, Rules rules)
    {
        Board b = Board.createElementsBoard(board);
        ArrayList<PointElement> elements = BoardChecker.getStraights(b, rules);
        assertEquals(expectedElements, elements);
    }
}
