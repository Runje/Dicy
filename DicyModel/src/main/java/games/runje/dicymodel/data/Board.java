package games.runje.dicymodel.data;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Utilities;

public class Board
{
    /**
     * Board as Matrix contains BoardElements.
     */
    private ArrayList<ArrayList<BoardElement>> board;

    /**
     * Number of rows.
     */
    private int rows;

    /**
     * Number of columns.
     */
    private int columns;

    /**
     * Initializes an empty Board.
     *
     * @param rows    Number of Rows.
     * @param columns Number of Columns.
     */
    public Board(int rows, int columns)
    {
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
                row.add(new Dice(0));
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
    public static Board createDiceBoard(int[] elements)
    {
        ArrayList<BoardElement> dices = new ArrayList<>(elements.length);
        for (int i = 0; i < elements.length; i++)
        {
            dices.add(new Dice(elements[i]));
        }

        return createBoard(dices);
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
                s += this.board.get(row).get(column).toString() + " ";
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
}
