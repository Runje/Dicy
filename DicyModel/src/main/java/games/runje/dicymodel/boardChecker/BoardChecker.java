package games.runje.dicymodel.boardChecker;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
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

    public static ArrayList<Move> getPossiblePointMoves(Board board, Rules rules)
    {
        ArrayList<Move> allMoves = getAllPossibleMoves(board, rules);
        ArrayList<Move> moves = new ArrayList<>();

        for (Move m : allMoves)
        {
            ArrayList<PointElement> pointElements = getPointsFromMove(m, board, rules);
            if (pointElements.size() > 0)
            {
                int points = 0;
                for (PointElement p : pointElements)
                {
                    points += p.getPoints();
                }

                if (points > 0)
                {
                    m.setPoints(points);
                    m.setPointElements(pointElements);
                    moves.add(m);
                }
            }
        }

        return moves;
    }

    private static ArrayList<PointElement> getPointsFromMove(Move m, Board board, Rules rules)
    {
        board.switchElements(m.getFirst(), m.getSecond());
        ArrayList<PointElement> pointElements = getAll(board, rules);


        // switch back
        board.switchElements(m.getFirst(), m.getSecond());

        return pointElements;
    }

    public static List<Move> getPossiblePointMovesGravity(Board board, Rules rules, List<Move> pointMoves)
    {
        List<Move> gravityMoves = new ArrayList<>();
        Gravity oldGravity = board.getGravity();
        for (Move move : pointMoves)
        {
            // try all 4 gravitys
            for (Gravity gravity : Gravity.values())
            {
                board.setGravity(gravity);
                ArrayList<PointElement> pointElements = getPointsFromMoveWithFalling(move, board, rules);
                gravityMoves.add(new Move(move.getFirst(), move.getSecond(), gravity, pointElements));
            }

        }

        board.setGravity(oldGravity);
        return gravityMoves;
    }

    private static ArrayList<PointElement> getPointsFromMoveWithFalling(Move move, Board board, Rules rules)
    {
        // copy board
        Board newBoard = new Board(board);
        newBoard.switchElements(move.getFirst(), move.getSecond());
        ArrayList<PointElement> pointElements = BoardChecker.getAll(newBoard, rules);
        int newPoints = Utilities.getPointsFrom(pointElements);
        if (newPoints > 0)
        {
            newBoard.deleteElements(pointElements);
            newBoard.moveElementsFromGravity();
            pointElements.addAll(BoardChecker.getAll(newBoard, rules));
        }

        return pointElements;
    }

    public static Move getBestSwitchMove(Board board, Rules rules)
    {
        List<Move> moves = getPossiblePointMoves(board, rules);
        int max = 0;
        Move maxMove = null;
        for (Move move : moves)
        {
            if (move.getPoints() > max)
            {
                maxMove = move;
                max = move.getPoints();
            }
        }

        return maxMove;
    }
    public static ArrayList<Move> getAllPossibleMoves(Board board, Rules rules)
    {
        ArrayList<Move> moves = new ArrayList<>();
        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            for (int column = 0; column < board.getNumberOfColumns(); column++)
            {
                // right
                if (column != board.getNumberOfColumns() - 1)
                {
                    moves.add(new Move(new Coords(row, column), new Coords(row, column + 1)));
                }

                // down
                if (row != board.getNumberOfRows() - 1)
                {
                    moves.add(new Move(new Coords(row, column), new Coords(row + 1, column)));
                }
            }
        }

        return moves;
    }


    public static List<Move> getPossibleChangeSkillMoves(int loadValue, Board board, Rules rules)
    {
        ArrayList<Move> moves = new ArrayList<>();
        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            for (int column = 0; column < board.getNumberOfColumns(); column++)
            {
                BoardElement element = board.getElement(row, column);
                int oldValue = element.getValue();

                element.setValue(loadValue);

                ArrayList<PointElement> pointElements = BoardChecker.getAll(board, rules);
                moves.add(new Move(new Coords(row, column), null, null, pointElements));

                element.setValue(oldValue);
            }
        }

        return moves;
    }

    public static List<Move> getPossibleChangeSkillMovesWithFalling(int loadValue, Board board, Rules rules, List<Move> moves)
    {
        List<Move> gravityMoves = new ArrayList<>();
        Gravity oldGravity = board.getGravity();
        for (Move move : moves)
        {
            // try all 4 gravitys
            for (Gravity gravity : Gravity.values())
            {
                board.setGravity(gravity);
                ArrayList<PointElement> pointElements = getPointsFromChangeWithFalling(loadValue, move, board, rules);
                gravityMoves.add(new Move(move.getFirst(), move.getSecond(), gravity, pointElements));
            }

        }

        board.setGravity(oldGravity);
        return gravityMoves;


    }

    private static ArrayList<PointElement> getPointsFromChangeWithFalling(int loadValue, Move move, Board board, Rules rules)
    {
        // copy board
        Board newBoard = new Board(board);
        newBoard.getElement(move.getFirst()).setValue(loadValue);
        ArrayList<PointElement> pointElements = BoardChecker.getAll(newBoard, rules);
        int newPoints = Utilities.getPointsFrom(pointElements);
        if (newPoints > 0)
        {
            newBoard.deleteElements(pointElements);
            newBoard.moveElementsFromGravity();
            pointElements.addAll(BoardChecker.getAll(newBoard, rules));
        }

        return pointElements;
    }
}
