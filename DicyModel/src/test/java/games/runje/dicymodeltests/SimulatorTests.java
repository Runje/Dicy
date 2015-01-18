package games.runje.dicymodeltests;

import org.junit.Test;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Thomas on 18.01.2015.
 */
public class SimulatorTests
{
    @Test
    public void makeOnlySwitch()
    {
        Rules rules = new Rules();
        helperSwitch(new int[]{1, 2, 1,
                        1, 1, 2,
                        3, 4, 5},
                rules, rules.getXOfAKindPoints(3, 1), new Move(new Coords(0, 2), new Coords(1, 2)));
    }

    @Test
    public void makeMove()
    {
        Rules rules = new Rules();
        helperMove(new int[]{3, 2, 1,
                        1, 1, 2,
                        3, 4, 5},
                rules, rules.getXOfAKindPoints(3, 1), new Move(new Coords(0, 2), new Coords(1, 2)));
    }

    private void helperSwitch(int[] board, Rules rules, int expectedPoints, Move m)
    {
        Board b = Board.createElementsBoard(board);
        Simulator s = new Simulator(b, rules);
        int points = s.makeOnlySwitch(m);
        assertEquals(expectedPoints, points);
        assertEquals(b.toString(), s.getBoard().toString());
    }

    private void helperMove(int[] board, Rules rules, int expectedPoints, Move m)
    {
        Board b = Board.createElementsBoard(board);
        Simulator s = new Simulator(b, rules);
        int points = s.makeMove(m, 10);
        assertTrue(expectedPoints <= points);
        assertEquals(b.toString(), s.getBoard().toString());
    }
}
