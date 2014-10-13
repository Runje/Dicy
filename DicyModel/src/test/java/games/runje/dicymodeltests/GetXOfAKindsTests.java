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
 * Created by Thomas on 01.10.2014.
 */
public class GetXOfAKindsTests
{
    @Test
    public void none()
    {
        noneHelper(new int[]{1, 2, 1,
                2, 3, 2,
                3, 1, 3});
        noneHelper(new int[]{1, 1, 2,
                1, 1, 2,
                3, 3, 1});
        noneHelper(new int[]{3, 2, 1,
                3, 2, 1,
                1, 3, 2});
    }

    private void noneHelper(int[] board)
    {
        Board b = Board.createDiceBoard(board);
        Rules rules = new Rules();
        ArrayList<PointElement> elements = BoardChecker.getXOfAKinds(b, rules);

        assertEquals(new ArrayList<PointElement>(), elements);
    }

    @Test
    public void pointsRow()
    {
        Rules rules = new Rules();
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(0, 1), new Coords(0, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 1)));
        pointsHelper(new int[]{1, 1, 1,
                        2, 3, 2,
                        3, 1, 3},
                expectedElements, rules);

        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 2, new Coords[]{new Coords(1, 0), new Coords(1, 1), new Coords(1, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 2)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 3, new Coords[]{new Coords(2, 0), new Coords(2, 1), new Coords(2, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 3)));
        pointsHelper(new int[]{1, 1, 1,
                        2, 2, 2,
                        3, 3, 3},
                expectedElements, rules);


        rules.setMinXOfAKind(5);
        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 5, 5, new Coords[]{new Coords(4, 0), new Coords(4, 1), new Coords(4, 2), new Coords(4, 3), new Coords(4, 4)}, Orientation.Right, rules.getXOfAKindPoints(5, 5)));

        pointsHelper(new int[]{1, 1, 1, 1, 2,
                        2, 2, 2, 2, 3,
                        3, 3, 3, 3, 4,
                        4, 4, 4, 4, 5,
                        5, 5, 5, 5, 5},
                expectedElements, rules);
    }

    @Test
    public void pointsColumn()
    {
        Rules rules = new Rules();
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)}, Orientation.Down, rules.getXOfAKindPoints(3, 1)));
        pointsHelper(new int[]{1, 3, 1,
                        1, 3, 2,
                        1, 1, 3},
                expectedElements, rules);

        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 2, new Coords[]{new Coords(0, 1), new Coords(1, 1), new Coords(2, 1)}, Orientation.Down, rules.getXOfAKindPoints(3, 2)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 3, new Coords[]{new Coords(0, 2), new Coords(1, 2), new Coords(2, 2)}, Orientation.Down, rules.getXOfAKindPoints(3, 3)));
        pointsHelper(new int[]{1, 2, 3,
                        1, 2, 3,
                        1, 2, 3},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 3, new Coords[]{new Coords(1, 2), new Coords(2, 2), new Coords(3, 2)}, Orientation.Down, rules.getXOfAKindPoints(3, 3)));
        pointsHelper(new int[]{1, 2, 1, 3,
                        1, 2, 3, 6,
                        2, 3, 3, 4,
                        2, 2, 3, 6},
                expectedElements, rules);


        rules.setMinXOfAKind(5);
        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 5, 5, new Coords[]{new Coords(0, 4), new Coords(1, 4), new Coords(2, 4), new Coords(3, 4), new Coords(4, 4)}, Orientation.Down, rules.getXOfAKindPoints(5, 5)));

        pointsHelper(new int[]{1, 2, 3, 4, 5,
                        1, 2, 3, 4, 5,
                        1, 2, 3, 4, 5,
                        1, 2, 3, 4, 5,
                        2, 3, 4, 6, 5},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        rules.setMinXOfAKind(3);
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 5, new Coords[]{new Coords(1, 4), new Coords(2, 4), new Coords(3, 4)}, Orientation.Down, rules.getXOfAKindPoints(3, 5)));

        pointsHelper(new int[]{1, 2, 3, 4, 3,
                        2, 3, 3, 4, 5,
                        1, 2, 6, 3, 5,
                        2, 4, 4, 6, 5,
                        2, 3, 4, 6, 3},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 5, new Coords[]{new Coords(0, 4), new Coords(1, 4), new Coords(2, 4)}, Orientation.Down, rules.getXOfAKindPoints(3, 5)));

        pointsHelper(new int[]{1, 2, 3, 4, 5,
                        2, 3, 3, 4, 5,
                        1, 2, 6, 3, 5,
                        2, 4, 4, 6, 4,
                        2, 3, 4, 6, 3},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 5, new Coords[]{new Coords(2, 4), new Coords(3, 4), new Coords(4, 4)}, Orientation.Down, rules.getXOfAKindPoints(3, 5)));

        pointsHelper(new int[]{1, 2, 3, 4, 6,
                        2, 3, 3, 4, 6,
                        1, 2, 6, 3, 5,
                        2, 4, 4, 6, 5,
                        2, 3, 4, 6, 5},
                expectedElements, rules);
    }

    @Test
    public void pointsDownRight()
    {
        Rules rules = new Rules();
        rules.setDiagonalActive(true);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 1), new Coords(2, 2)}, Orientation.DownRight, rules.getXOfAKindPoints(3, 1)));
        pointsHelper(new int[]{1, 3, 1,
                        1, 1, 2,
                        2, 2, 1},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 6, new Coords[]{new Coords(0, 1), new Coords(1, 2), new Coords(2, 3)}, Orientation.DownRight, rules.getXOfAKindPoints(3, 6)));
        pointsHelper(new int[]{1, 6, 1, 1,
                        1, 1, 6, 1,
                        3, 2, 3, 6,
                        5, 5, 4, 5},
                expectedElements, rules);


    }

    @Test
    public void pointsDownLeft()
    {
        Rules rules = new Rules();
        rules.setDiagonalActive(true);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 2), new Coords(1, 1), new Coords(2, 0)}, Orientation.DownLeft, rules.getXOfAKindPoints(3, 1)));
        pointsHelper(new int[]{2, 3, 1,
                        1, 1, 2,
                        1, 2, 1},
                expectedElements, rules);

        expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 6, new Coords[]{new Coords(1, 3), new Coords(2, 2), new Coords(3, 1)}, Orientation.DownLeft, rules.getXOfAKindPoints(3, 6)));
        pointsHelper(new int[]{2, 3, 1, 5,
                        1, 5, 2, 6,
                        1, 2, 6, 5,
                        4, 6, 5, 6},
                expectedElements, rules);
    }

    @Test
    public void allPoints()
    {
        Rules rules = new Rules();
        rules.setDiagonalActive(true);
        ArrayList<PointElement> expectedElements = new ArrayList<>();
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(0, 1), new Coords(0, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(1, 0), new Coords(1, 1), new Coords(1, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(2, 0), new Coords(2, 1), new Coords(2, 2)}, Orientation.Right, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 0), new Coords(2, 0)}, Orientation.Down, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 1), new Coords(1, 1), new Coords(2, 1)}, Orientation.Down, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 2), new Coords(1, 2), new Coords(2, 2)}, Orientation.Down, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 0), new Coords(1, 1), new Coords(2, 2)}, Orientation.DownRight, rules.getXOfAKindPoints(3, 1)));
        expectedElements.add(new PointElement(PointType.XOfAKind, 3, 1, new Coords[]{new Coords(0, 2), new Coords(1, 1), new Coords(2, 0)}, Orientation.DownLeft, rules.getXOfAKindPoints(3, 1)));
        pointsHelper(new int[]{1, 1, 1,
                        1, 1, 1,
                        1, 1, 1},
                expectedElements, rules);
    }

    private void pointsHelper(int[] board, ArrayList<PointElement> expectedElements, Rules rules)
    {
        Board b = Board.createDiceBoard(board);
        ArrayList<PointElement> elements = BoardChecker.getXOfAKinds(b, rules);
        assertEquals(expectedElements, elements);
    }

}
