package games.runje.dicymodel.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.boardChecker.BoardChecker;

public class Board
{
    /**
     * Board as Matrix contains BoardElements.
     */
    protected ArrayList<ArrayList<BoardElement>> board;

    /**
     * Number of rows.
     */
    protected int rows;
    /**
     * Number of columns.
     */
    protected int columns;
    protected Gravity gravity;
    public static String LogKey = "Board";

    /**
     * Initializes an empty Board.
     *
     * @param rows    Number of Rows.
     * @param columns Number of Columns.
     */
    public Board(int rows, int columns)
    {
        this.gravity = Gravity.Down;
        this.columns = columns;
        this.rows = rows;
        this.board = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++)
        {
            this.board.add(new ArrayList<BoardElement>(columns));

            List<BoardElement> row = this.board.get(i);

            // fill rows with dummy dices
            for (int j = 0; j < columns; j++)
            {
                row.add(new Dice(new Coords(i, j)));
            }
        }
    }

    /**
     * Initializes a quadratic board.
     *
     * @param size Number of rows and columns.
     */
    public Board(int size)
    {
        this(size, size);
    }

    /**
     * Copy Constructor.
     *
     * @param b
     */
    public Board(Board b)
    {
        this(b.getNumberOfRows(), b.getNumberOfColumns());
        this.gravity = b.gravity;

        for (int i = 0; i < rows; i++)
        {
            // copy every element
            for (int j = 0; j < columns; j++)
            {
                Coords c = new Coords(i, j);
                this.setElement(c, b.getElement(c).copy());
            }
        }
    }

    /**
     * Creates a board with dices.
     *
     * @param elements each element is the value of the dice. (row after row)
     * @return the created board
     */
    public static Board createBoard(List<BoardElement> elements)
    {
        // check if size is quadratic
        assert Utilities.isPerfectSquare(elements.size()) : "Size must be a square";
        int root = (int) Math.sqrt(elements.size());

        Board board = new Board(root);
        int i = 0;
        for (int row = 0; row < root; row++)
        {
            for (int column = 0; column < root; column++)
            {
                board.set(row, column, elements.get(i));
                i++;
            }
        }

        return board;
    }

    /**
     * Creates a board with dices.
     *
     * @param elements each element is the value of the dice. (row after row)
     * @return the created board
     */
    public static Board createElementsBoard(int[] elements)
    {
        ArrayList<BoardElement> boardElements = new ArrayList<>(elements.length);
        // check if size is quadratic
        assert Utilities.isPerfectSquare(elements.length) : "Size must be a square";
        int root = (int) Math.sqrt(elements.length);
        for (int i = 0; i < elements.length; i++)
        {
            int row = i / root;
            int column = i % root;
            Coords pos = new Coords(row, column);
            int value = elements[i];
            if (value == 0)
            {
                boardElements.add(new NoElement(pos));
            }
            else
            {
                boardElements.add(new Dice(value, pos));
            }
        }

        return createBoard(boardElements);
    }

    public static Board createBoardNoPoints(Rules rules)
    {
        while (true)
        {
            Board b = new Board(rules.getRows(), rules.getColumns());
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(b, rules);
            if (BoardChecker.getAll(b, rules).size() == 0 && moves.size() > 0)
            {
                return b;
            }
        }
    }

    public ArrayList<ArrayList<BoardElement>> getBoard()
    {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<BoardElement>> board)
    {
        this.board = board;
    }

    /**
     * Sets a board element
     *
     * @param row     Row index of the board
     * @param column  column index of the board
     * @param element BoardElement to set
     */
    public void set(int row, int column, BoardElement element)
    {
        this.board.get(row).set(column, element);
    }

    public int getNumberOfRows()
    {
        return rows;
    }

    public int getNumberOfColumns()
    {
        return columns;
    }

    @Override
    public String toString()
    {
        String s = "";
        for (int row = 0; row < this.rows; row++)
        {
            s += "Row " + row + ": ";
            for (int column = 0; column < this.columns; column++)
            {
                s += this.board.get(row).get(column).getValue() + " ";
            }

            s += "\n";
        }
        return s;
    }

    public ArrayList<BoardElement> getRow(int row)
    {
        return this.board.get(row);
    }

    public ArrayList<BoardElement> getColumn(int column)
    {
        ArrayList<BoardElement> list = new ArrayList<>();

        for (int row = 0; row < this.rows; row++)
        {
            list.add(this.board.get(row).get(column));
        }

        return list;
    }

    public BoardElement getElement(int row, int column)
    {
        return this.board.get(row).get(column);
    }

    public BoardElement getElement(Coords coord)
    {
        return getElement(coord.row, coord.column);
    }

    public boolean switchElements(Coords first, Coords second, boolean switchBackPossible, Rules rules)
    {
        // switch
        BoardElement temp = this.getElement(first);
        this.setElement(first, this.getElement(second));
        this.setElement(second, temp);

        // update position
        this.updatePosition(first);
        this.updatePosition(second);

        if (switchBackPossible)
        {
            boolean switchBack = BoardChecker.getAll(this, rules).size() == 0;
            if (switchBack)
            {
                switchElements(second, first);
            }

            return switchBack;
        }

        return false;
    }

    public boolean switchElements(Coords first, Coords second)
    {
        return switchElements(first, second, false, null);
    }

    private void updatePosition(Coords pos)
    {
        BoardElement e = this.getElement(pos);
        e.setPosition(pos);
    }

    public void setElement(int row, int column, BoardElement element)
    {
        this.board.get(row).set(column, element);
    }

    public void setElement(Coords c, BoardElement element)
    {
        setElement(c.row, c.column, element);
    }

    public ArrayList<BoardElement> recreateElements()
    {
        ArrayList<BoardElement> elements = new ArrayList<>();
        for (ArrayList<BoardElement> row : this.board)
        {
            for (BoardElement element : row)
            {
                if (element instanceof NoElement)
                {
                    Coords pos = element.getPosition();
                    element = new Dice(pos);
                    setElement(pos, element);
                    elements.add(element);
                }
            }
        }

        return elements;
    }

    public ArrayList<BoardElement> getRecreateElements()
    {
        ArrayList<BoardElement> elements = new ArrayList<>();
        for (ArrayList<BoardElement> row : this.board)
        {
            for (BoardElement element : row)
            {
                if (element instanceof NoElement)
                {
                    Coords pos = element.getPosition();
                    BoardElement newElement = new Dice(pos);
                    elements.add(newElement);
                }
            }
        }

        return elements;
    }


    /**
     * recreates the given elements
     *
     * @param elements the new elements
     */
    public void recreateElements(ArrayList<BoardElement> elements)
    {
        for (BoardElement element : elements)
        {
            setElement(element.getPosition(), element);
            Logger.logDebug(LogKey, "Setting element with value " + element.getValue() + " at " + element.getPosition());
        }
    }

    public void deleteElements(ArrayList<PointElement> elements)
    {
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);
        for (Coords c : coords)
        {
            this.setElement(c, new NoElement(c));
        }
    }

    public ArrayList<BoardElement> moveElementsFromGravity()
    {
        ArrayList<BoardElement> elements = this.determineFallingElements();
        ArrayList<Coords> fallingPositions = new ArrayList<>();
        for (BoardElement element : elements)
        {
            Coords fallPos = determineFallingPosition(element);
            fallingPositions.add(fallPos);
        }

        for (int i = 0; i < fallingPositions.size(); i++)
        {
            Coords pos = elements.get(i).getPosition();
            moveElement(pos, fallingPositions.get(i));
        }

        return elements;
    }

    private void moveElement(Coords from, Coords to)
    {
        BoardElement element = getElement(from);
        element.setPosition(to);
        setElement(to, element);
        setElement(from, new NoElement(from));
    }

    public Gravity getGravity()
    {
        return gravity;
    }

    public void setGravity(Gravity gravity)
    {
        this.gravity = gravity;
    }

    public ArrayList<BoardElement> determineFallingElements()
    {
        ArrayList<BoardElement> elements = new ArrayList<>();

        for (ArrayList<BoardElement> row : this.board)
        {
            for (BoardElement element : row)
            {
                if (element instanceof NoElement)
                {
                    Collection<? extends BoardElement> boardElements = getElementsFrom(this.gravity, element);
                    // add only if it is not already in list
                    for (BoardElement e : boardElements)
                    {
                        if (!elements.contains(e))
                        {
                            elements.add(e);
                        }
                    }
                }

            }
        }

        // sort elements that the falling elements don't overwrite other "to-fall" elements
        sort(elements);

        return elements;
    }

    private void sort(ArrayList<BoardElement> elements)
    {
        switch (this.gravity)
        {
            case Up:
                Collections.sort(elements, new Comparator<BoardElement>()
                {
                    @Override
                    public int compare(BoardElement e1, BoardElement e2)
                    {

                        return e1.getPosition().row - e2.getPosition().row;
                    }
                });
                break;
            case Down:
                Collections.sort(elements, new Comparator<BoardElement>()
                {
                    @Override
                    public int compare(BoardElement e1, BoardElement e2)
                    {

                        return e2.getPosition().row - e1.getPosition().row;
                    }
                });

                break;
            case Right:
                Collections.sort(elements, new Comparator<BoardElement>()
                {
                    @Override
                    public int compare(BoardElement e1, BoardElement e2)
                    {

                        return e2.getPosition().column - e1.getPosition().column;
                    }
                });

                break;
            case Left:
                Collections.sort(elements, new Comparator<BoardElement>()
                {
                    @Override
                    public int compare(BoardElement e1, BoardElement e2)
                    {

                        return e1.getPosition().column - e2.getPosition().column;
                    }
                });

                break;
        }
    }

    private Collection<? extends BoardElement> getElementsFrom(Gravity gravity, BoardElement element)
    {
        ArrayList<BoardElement> elements = new ArrayList<>();
        Coords position = element.getPosition();
        switch (this.gravity)
        {
            case Down:
                for (int i = position.row - 1; i >= 0; i--)
                {
                    BoardElement e = this.getElement(i, position.column);
                    if (e instanceof NoElement)
                    {
                        continue;
                    }

                    elements.add(e);
                }
                break;
            case Up:
                for (int i = position.row + 1; i < this.getNumberOfRows(); i++)
                {
                    BoardElement e = this.getElement(i, position.column);
                    if (e instanceof NoElement)
                    {
                        continue;
                    }

                    elements.add(e);
                }
                break;
            case Left:
                for (int i = position.column + 1; i < this.getNumberOfColumns(); i++)
                {
                    BoardElement e = this.getElement(position.row, i);
                    if (e instanceof NoElement)
                    {
                        continue;
                    }

                    elements.add(e);
                }
                break;
            case Right:
                for (int i = position.column - 1; i >= 0; i--)
                {
                    BoardElement e = this.getElement(position.row, i);
                    if (e instanceof NoElement)
                    {
                        continue;
                    }

                    elements.add(e);
                }
                break;
        }
        return elements;
    }

    protected Coords determineFallingPosition(BoardElement element)
    {
        Coords pos = element.getPosition();
        int offset = 0;
        switch (this.gravity)
        {
            case Up:
                for (int i = pos.row - 1; i > -1; i--)
                {
                    if ((getElement(i, pos.column) instanceof NoElement))
                    {
                        offset++;
                    }
                }

                return new Coords(pos.row - offset, pos.column);

            case Down:
                for (int i = pos.row + 1; i < rows; i++)
                {
                    if ((getElement(i, pos.column) instanceof NoElement))
                    {
                        offset++;
                    }
                }

                return new Coords(pos.row + offset, pos.column);
            case Right:
                for (int i = pos.column + 1; i < columns; i++)
                {
                    if ((getElement(pos.row, i) instanceof NoElement))
                    {
                        offset++;
                    }
                }

                return new Coords(pos.row, pos.column + offset);
            case Left:
                for (int i = pos.column - 1; i > -1; i--)
                {
                    if ((getElement(pos.row, i) instanceof NoElement))
                    {
                        offset++;
                    }
                }

                return new Coords(pos.row, pos.column - offset);
        }
        return null;
    }

    public void changeElement(Coords position, int newValue)
    {
        getElement(position).setValue(newValue);
    }

    public void recreateBoard()
    {
        for (ArrayList<BoardElement> row : this.board)
        {
            for (BoardElement element : row)
            {
                // TODO: make method shuffle or somethin
                element.setValue(new Dice().getValue());
            }
        }
    }

    public void disable()
    {
        // for animated board
    }

    public void enable()
    {
        // for animated board
    }
}
