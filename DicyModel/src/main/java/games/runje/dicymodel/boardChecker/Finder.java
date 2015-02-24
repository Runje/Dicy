package games.runje.dicymodel.boardChecker;

import java.util.ArrayList;
import java.util.Collection;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;

/**
 * Created by Thomas on 08.10.2014.
 */
public abstract class Finder
{
    protected final Board board;
    protected final Rules rules;

    public Finder(Board board, Rules rules)
    {
        this.board = board;
        this.rules = rules;
    }

    /**
     * Finds the XOfAKind PointElements.
     *
     * @return The XOfAKind PointElements.
     */
    public ArrayList<PointElement> find()
    {
        ArrayList<PointElement> elements = new ArrayList<>();

        // check rows
        elements.addAll(this.inRows());

        // check columns
        elements.addAll(this.inColumns());

        if (!rules.isDiagonalActive())
        {
            return elements;
        }

        // check DownRight diagonals
        elements.addAll(this.inDiagonalsDownRight());

        // check DownLeft diagonals
        elements.addAll(this.inDiagonalsDownLeft());

        return elements;
    }


    /**
     * Looks in the diagonals in the direction down right.
     *
     * @return The PointElements in the diagonals down right
     */
    protected Collection<? extends PointElement> inDiagonalsDownRight()
    {
        ArrayList<PointElement> elements = new ArrayList<>();
        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            for (int column = 0; column < board.getNumberOfColumns(); column++)
            {
                // getting all DownRight Diagonals through first row and first column
                if (row > 0 && column > 0)
                {
                    break;
                }

                // make diagonal
                ArrayList<BoardElement> diagonal = new ArrayList<>();

                int tempRow = row;
                int tempColumn = column;
                while (tempRow < board.getNumberOfRows() && tempColumn < board.getNumberOfColumns())
                {
                    diagonal.add(board.getElement(tempRow, tempColumn));
                    tempRow++;
                    tempColumn++;
                }

                elements.addAll(getPointElementsFromLine(diagonal, Orientation.DownRight, new Coords(row, column)));
            }
        }

        return elements;
    }

    /**
     * Looks in the diagonals in the direction down left.
     *
     * @return The PointElements in the diagonals down left
     */
    protected Collection<? extends PointElement> inDiagonalsDownLeft()
    {
        ArrayList<PointElement> elements = new ArrayList<>();

        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            for (int column = board.getNumberOfColumns() - 1; column > -1; column--)
            {
                // getting all DownLeft Diagonals through first row and last column
                if (row > 0 && column < board.getNumberOfColumns() - 1)
                {
                    break;
                }
                // make diagonal
                ArrayList<BoardElement> diagonal = new ArrayList<>();

                int tempRow = row;
                int tempColumn = column;
                while (tempRow < board.getNumberOfRows() && tempColumn >= 0)
                {
                    diagonal.add(board.getElement(tempRow, tempColumn));
                    tempRow++;
                    tempColumn--;
                }

                elements.addAll(getPointElementsFromLine(diagonal, Orientation.DownLeft, new Coords(row, column)));
            }
        }

        return elements;
    }

    /**
     * Looks in the columns.
     *
     * @return the PointElements in the columns.
     */
    protected Collection<? extends PointElement> inColumns()
    {
        ArrayList<PointElement> elements = new ArrayList<>();
        for (int column = 0; column < board.getNumberOfColumns(); column++)
        {
            elements.addAll(this.getPointElementsFromLine(board.getColumn(column), Orientation.Down, new Coords(0, column)));
        }

        return elements;
    }

    /**
     * Looks in the rows.
     *
     * @return the PointElements in the rows.
     */
    protected Collection<? extends PointElement> inRows()
    {
        ArrayList<PointElement> elements = new ArrayList<>();
        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            elements.addAll(this.getPointElementsFromLine(board.getRow(row), Orientation.Right, new Coords(row, 0)));
        }

        return elements;
    }

    /**
     * Creates a XOfAKind Point Element.
     *
     * @param length      The totalLength of the element
     * @param endIndex    The last index.
     * @param orientation The orientation.
     * @param value       The highest value of the dice.
     * @param firstPoint  The first Point in the board of the element.
     * @param type        PointType of the element
     * @return Point Element
     */
    protected PointElement createPointElement(int length, int endIndex, Orientation orientation, int value, Coords firstPoint, PointType type)
    {
        // 1 2 2 2 3 --> totalLength = 3 ( Three of a kind)
        //       ^
        //     endIndex
        Coords startPoint = CoordsHelper.calcStartPoint(firstPoint, orientation, length, endIndex);
        Coords[] coords = CoordsHelper.calcCoords(startPoint, orientation, length);
        return new PointElement(type, length, value, coords, orientation, rules.getPoints(length, value, type));
    }

    /**
     * Looks for PointElements in a line.
     *
     * @param line        to look in
     * @param orientation of the line
     * @param firstPoint  first Point of the line in the board
     * @return PointElements in this line.
     */
    abstract protected Collection<? extends PointElement> getPointElementsFromLine(ArrayList<BoardElement> line, Orientation orientation, Coords firstPoint);


}
