package games.runje.dicy.animatedData;

import android.app.Activity;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
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
        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
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

    public void switchElements(Coords first, Coords second)
    {
        Logger.logDebug(LogKey, "Animated Switch, first: " + first + ", second: " + second + ", Board: " + this.board + "\n" + this.animatedBoard);

        super.switchElements(first, second);

        AnimatedBoardElement firstImage = this.getAnimatedElement(first);
        AnimatedBoardElement secondImage = this.getAnimatedElement(second);

        SwitchAnimation s = new SwitchAnimation(firstImage, secondImage);
        s.start();
    }

    public void setAnimatedElement(Coords coords, AnimatedBoardElement element)
    {
        this.animatedBoard.get(coords.row).set(coords.column, element);
    }

    private int getImageResource(Coords pos)
    {
        return AnimatedBoardElement.ElementToImageResource(this.getElement(pos));
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
     * TODO: Usage?
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
                    y = CoordsToY(new Coords(rows, pos.column));
                    break;
                case Down:
                    x = CoordsToX(pos);
                    y = CoordsToY(new Coords(-1, pos.column));
                    break;
                case Right:
                    x = CoordsToX(new Coords(pos.row, -1));
                    y = CoordsToY(pos);
                    break;
                case Left:
                    x = CoordsToX(new Coords(pos.row, columns));
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

    public void deleteElements(ArrayList<PointElement> elements)
    {
        super.deleteElements(elements);
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);
        for (Coords c : coords)
        {
            this.getAnimatedElement(c).remove();
            this.getAnimatedElement(c).setValue(0);
        }
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
