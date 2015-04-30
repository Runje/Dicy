package games.runje.dicymodel.ai;

import java.util.ArrayList;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 18.01.2015.
 */
public class Simulator
{
    private final Rules rules;
    // if the board has to be copied
    private Board board;

    public Simulator(Board b, Rules r)
    {
        // copy board
        this.board = new Board(b);
        this.rules = r;
    }

    public Simulator(Board board, Rules rules, boolean copy)
    {
        if (copy)
        {
            this.board = new Board(board);
        }
        else
        {
            this.board = board;
        }

        this.rules = rules;
    }

    public static int getLimit(Rules rules, Board b)
    {
        int points = 0;
        int n = 30;

        for (int i = 0; i < n; i++)
        {
            Board board = Board.createBoardNoPoints(rules);
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

            if (moves.size() == 0)
            {
                // should never happen
                return -1;
            }

            Simulator s = new Simulator(board, rules, false);
            points += s.makeMove(Strategy.getBestSwitchMove(moves), n / 10);
        }

        int average = points / n;
        return average / 2;
    }

    public int makeOnlySwitch(Move m)
    {
        board.switchElements(m.getFirst(), m.getSecond());
        ArrayList<PointElement> pointElements = BoardChecker.getAll(board, rules);
        int points = Utilities.getPointsFrom(pointElements);


        // switch back
        board.switchElements(m.getSecond(), m.getFirst());
        return points;
    }

    public int makeMove(Move m, int n)
    {
        int points = 0;
        for (int i = 0; i < n; i++)
        {
            Board b = new Board(board);
            b.switchElements(m.getFirst(), m.getSecond());
            while (true)
            {
                ArrayList<PointElement> pointElements = BoardChecker.getAll(b, rules);
                int newPoints = Utilities.getPointsFrom(pointElements);
                if (newPoints == 0)
                {
                    break;
                }

                points += newPoints;

                b.deleteElements(pointElements);
                b.moveElementsFromGravity();
                b.recreateElements();
            }
        }

        return points / n;
    }

    public Board getBoard()
    {
        return board;
    }
}
