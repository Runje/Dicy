package games.runje.dicy.animatedData;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 13.10.2014.
 */
public class AnimatedBoard extends Board
{
    public static final String LogKey = "AnimatedBoard";

    /**
     * Length of a dice.
     */
    private final int diceSize = 100;

    private Activity activity;

    private ArrayList<ArrayList<AnimatedBoardElement>> animatedBoard;

    private RelativeLayout relativeLayout;

    public AnimatedBoard(int rows, int columns, Activity activity)
    {
        super(rows, columns);
        this.activity = activity;

        createAnimatedBoard(rows, columns);
    }

    public AnimatedBoard(int[] dices, Activity a)
    {
        super((int) Math.sqrt(dices.length));
        int root = (int) Math.sqrt(dices.length);
        this.activity = a;
        Board b = Board.createElementsBoard(dices);
        this.board = b.getBoard();
        createAnimatedBoard(root, root);
    }

    public static AnimatedBoard createBoardNoPoints(int rows, int columns, Activity activity, Rules rules)
    {
        while (true)
        {
            AnimatedBoard b = new AnimatedBoard(rows, columns, activity);
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(b, rules);
            if (BoardChecker.getAll(b, rules).size() == 0 && moves.size() > 0)
            {
                Logger.logInfo(LogKey, "Possible Moves: " + moves);
                return b;
            }
        }
    }

    /**
     * Fills the animated board with dummy dices and creates the layout.
     *
     * @param rows
     * @param columns
     */
    private void createAnimatedBoard(int rows, int columns)
    {
        this.animatedBoard = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++)
        {
            this.animatedBoard.add(new ArrayList<AnimatedBoardElement>(columns));

            List<AnimatedBoardElement> row = this.animatedBoard.get(i);

            // fill rows with dummy dices
            for (int j = 0; j < columns; j++)
            {
                row.add(new AnimatedBoardElement(this.activity, this.getElement(i, j)));
            }
        }

        this.createAbsoluteLayout();
    }

    /**
     * Creates an absolute Layout.
     */
    private void createAbsoluteLayout()
    {
        this.relativeLayout = new RelativeLayout(this.activity);
        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(diceSize * (rows + 1), diceSize * (columns + 1));
        relativeLayout.setLayoutParams(tableParams);

        for (ArrayList<AnimatedBoardElement> row : this.animatedBoard)
        {
            for (AnimatedBoardElement iv : row)
            {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diceSize, diceSize);
                params.leftMargin = CoordsToX(iv.getPosition());
                params.topMargin = CoordsToY(iv.getPosition());
                relativeLayout.addView(iv, params);
            }
        }
    }

    public int CoordsToX(Coords pos)
    {
        //offset + 1 to have one column on the left for fall down
        return diceSize * (pos.column + 1);
    }

    public int CoordsToY(Coords pos)
    {
        // offset + 1 to have one row on top for fall down
        return diceSize * (pos.row + 1);
    }

    public RelativeLayout getRelativeLayout()
    {
        return this.relativeLayout;
    }

    public boolean switchElements(Coords first, Coords second, boolean switchBackPossible)
    {
        Logger.logDebug(LogKey, "Animated Switch, first: " + first + ", second: " + second + ", Board: " + this.board + "\n" + this.animatedBoard);

        boolean switchback = super.switchElements(first, second, switchBackPossible, Gamemaster.getInstance().getRules());

        if (!switchBackPossible)
        {
            switchback = false;
        }

        AnimatedBoardElement firstImage = this.getAnimatedElement(first);
        AnimatedBoardElement secondImage = this.getAnimatedElement(second);

        SwitchAnimation s = new SwitchAnimation(firstImage, secondImage, switchback);
        s.start();

        return switchback;
    }

    public void setAnimatedElement(Coords coords, AnimatedBoardElement element)
    {
        this.animatedBoard.get(coords.row).set(coords.column, element);
    }

    @Override
    public String toString()
    {
        String s = "Animated Board:\n";
        for (int row = 0; row < this.rows; row++)
        {
            s += "Row " + row + ": ";
            for (int column = 0; column < this.columns; column++)
            {
                s += this.animatedBoard.get(row).get(column).getValue() + " ";
            }

            s += "\n";
        }
        return s + super.toString();
    }

    public AnimatedBoardElement getAnimatedElement(Coords pos)
    {
        return this.animatedBoard.get(pos.row).get(pos.column);
    }

    /**
     * Changes the color of of a imageview to black and white.
     *
     * @param elements
     */
    public void highlightElements(ArrayList<PointElement> elements)
    {
        ArrayList<Coords> highlightCoords = Coords.pointElementsToCoords(elements);
        for (ArrayList<AnimatedBoardElement> row : this.animatedBoard)
        {
            for (AnimatedBoardElement element : row)
            {
                Coords pos = element.getPosition();

                if (highlightCoords.contains(pos))
                {
                    element.setHighlight(true);
                }
                else
                {
                    element.setHighlight(false);
                }
            }
        }
    }

    /**
     * Recreates the NoElements and lets them fall down.
     *
     * @return Recreated elements
     */
    public ArrayList<BoardElement> recreateElements()
    {
        ArrayList<BoardElement> elements = super.recreateElements();
        Logger.logInfo(LogKey, "Recreating elements: " + elements);
        int[] max = getMaxFallLength(elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = new AnimatedBoardElement(this.activity, element);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diceSize, diceSize);

            int x = 0;
            int y = 0;

            switch (this.gravity)
            {
                case Up:
                    x = CoordsToX(pos);
                    y = CoordsToY(new Coords(pos.row + max[pos.column], pos.column));
                    break;
                case Down:
                    x = CoordsToX(pos);
                    y = CoordsToY(new Coords(pos.row - max[pos.column], pos.column));
                    break;
                case Right:
                    x = CoordsToX(new Coords(pos.row, pos.column - max[pos.row]));
                    y = CoordsToY(pos);
                    break;
                case Left:
                    x = CoordsToX(new Coords(pos.row, pos.column + max[pos.row]));
                    y = CoordsToY(pos);
                    break;
            }

            animatedElement.setX(x);
            animatedElement.setY(y);
            relativeLayout.addView(animatedElement, params);
            Logger.logDebug(LogKey, "Pos: " + pos + ". X: " + params.leftMargin + " Y: " + params.topMargin);
            new FallAnimation(animatedElement, pos).start();
        }

        return elements;
    }

    /**
     * Gets the maximum fall length for each column/row depending on the gravity.
     *
     * @param fallingElements
     * @return
     */
    private int[] getMaxFallLength(ArrayList<BoardElement> fallingElements)
    {
        int n;
        if (gravity == Gravity.Down || gravity == Gravity.Up)
        {
            n = columns;
        }
        else
        {
            n = rows;
        }

        int max[] = new int[n];
        for (int i = 0; i < n; i++)
        {
            max[i] = -1;
        }

        switch (this.gravity)
        {

            case Up:
                for (BoardElement e : fallingElements)
                {
                    //+1 for length
                    int r = rows - e.getPosition().row;
                    if (max[e.getPosition().column] < r)
                    {
                        max[e.getPosition().column] = r;
                    }

                }
                break;
            case Down:
                for (BoardElement e : fallingElements)
                {
                    //+1 for length
                    int r = e.getPosition().row + 1;
                    if (max[e.getPosition().column] < r)
                    {
                        max[e.getPosition().column] = r;
                    }

                }
                break;
            case Right:
                for (BoardElement e : fallingElements)
                {
                    //+1 for length
                    int r = e.getPosition().column + 1;
                    if (max[e.getPosition().row] < r)
                    {
                        max[e.getPosition().row] = r;
                    }

                }
                break;
            case Left:
                for (BoardElement e : fallingElements)
                {
                    //+1 for length
                    int r = columns - e.getPosition().column;
                    if (max[e.getPosition().row] < r)
                    {
                        max[e.getPosition().row] = r;
                    }

                }
                break;
        }

        return max;
    }

    public void deleteElements(ArrayList<PointElement> elements)
    {
        super.deleteElements(elements);
        new PointsAnimation(elements).start();
    }

    /**
     * Checks if the Animated Board and the internal board are consistent.
     *
     * @return Result of the check.
     */
    public boolean consistencyCheck()
    {
        String animatedBoard = "Animated:\n";
        for (int row = 0; row < this.rows; row++)
        {
            animatedBoard += "Row " + row + ": ";
            for (int column = 0; column < this.columns; column++)
            {
                animatedBoard += this.animatedBoard.get(row).get(column).getValue() + " ";
            }

            animatedBoard += "\nBoard:\n";
        }

        String board = super.toString();
        boolean result = board.equals(animatedBoard);
        if (!result)
        {
            Logger.logError(LogKey, this.toString());
        }

        return result;
    }

    /**
     * Lets the Elements fall down.
     *
     * @return Fallen down elements.
     */
    public ArrayList<BoardElement> moveElementsFromGravity()
    {
        ArrayList<BoardElement> elements = this.determineFallingElements();
        Logger.logInfo(LogKey, "Falling elements: " + elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = this.getAnimatedElement(pos);

            Coords newPos = determineFallingPosition(element);
            new FallAnimation(animatedElement, newPos).start();
        }

        super.moveElementsFromGravity();
        return elements;
    }

    public Activity getActivity()
    {
        return activity;
    }
}