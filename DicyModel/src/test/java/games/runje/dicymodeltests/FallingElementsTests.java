package games.runje.dicymodeltests;

import org.junit.Test;

import java.util.ArrayList;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Dice;
import games.runje.dicymodel.data.Gravity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Thomas on 02.11.2014.
 */
public class FallingElementsTests
{
    @Test
    public void down()
    {
        ArrayList<BoardElement> expectedElements = new ArrayList<>();

        expectedElements.add(new Dice(1, new Coords(0, 0)));
        expectedElements.add(new Dice(2, new Coords(0, 1)));
        expectedElements.add(new Dice(3, new Coords(0, 2)));
        helper(new int[]{1, 2, 3,
                        0, 0, 0,
                        1, 1, 1},
                expectedElements);
        helper(new int[]{1, 2, 3,
                        0, 0, 0,
                        0, 0, 0},
                expectedElements);

        expectedElements.add(new Dice(1, new Coords(1, 0)));
        expectedElements.add(new Dice(1, new Coords(1, 1)));
        expectedElements.add(new Dice(2, new Coords(1, 2)));
        helper(new int[]{1, 2, 3,
                        1, 1, 2,
                        0, 0, 0},
                expectedElements);
    }

    private void helper(int[] board, ArrayList<BoardElement> expectedElements)
    {
        Board b = Board.createElementsBoard(board);
        b.setGravity(Gravity.Down);
        ArrayList<BoardElement> elements = b.determineFallingElements();
        assertEquals(elements.size(), expectedElements.size());
        assertTrue(elements.containsAll(expectedElements));
        assertTrue(expectedElements.containsAll(elements));
    }
}
