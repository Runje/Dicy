package games.runje.dicymodeltests;

import org.junit.Test;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
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

    @Test
    public void getPointsFromMoveFalling()
    {
        Board b = Board.createElementsBoard(new int[]{3, 1, 1, 2, 5,
                1, 1, 2, 6, 5,
                3, 6, 2, 1, 6,
                2, 3, 5, 2, 2,
                5, 5, 6, 5, 6});
        Rules rules = new Rules();
        Simulator s = new Simulator(b, rules);
        Move m = new Move(new Coords(2, 2), new Coords(3, 2));
        BoardChecker.getPossiblePointMovesGravity(b, rules, BoardChecker.getPossiblePointMoves(b, rules));
    }

    @Test
    public void strategyTest()
    {
        Strategy s = new Strategy(0, 0.1, 0.22, 0.333, 0.999999, 1);
        Strategy s2 = Strategy.getStrategy(s.toString());
        double delta = 0.00000001;
        assertEquals(s.getError(), s2.getError(), delta);
        assertEquals(s.getGravitation(), s2.getGravitation(), delta);
        assertEquals(s.getRisk(), s2.getRisk(), delta);
        assertEquals(s.getSeeFalling(), s2.getSeeFalling(), delta);
        assertEquals(s.getSeePoints(), s2.getSeePoints(), delta);
        assertEquals(s.getSkills(), s2.getSkills(), delta);
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
