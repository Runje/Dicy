package games.runje.dicymodel.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static int getLimit(Rules rules)
    {
        int n = 30;
        List<Integer> pointList = new ArrayList<>(n);
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
            pointList.add(s.makeMove(Strategy.getBestSwitchMove(moves), 10));
        }

        Collections.sort(pointList);
        //  quantil
        return pointList.get((int) (n / 3.5));
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

        List<Integer> pointList = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
        {
            Board b = new Board(board);
            b.switchElements(m.getFirst(), m.getSecond());
            int points = 0;
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

            pointList.add(points);
        }

        Collections.sort(pointList);

        // median
        return pointList.get(n / 2);
    }

    public Board getBoard()
    {
        return board;
    }
}
