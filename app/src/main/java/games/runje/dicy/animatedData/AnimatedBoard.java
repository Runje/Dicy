package games.runje.dicy.animatedData;

import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.NoElement;
import games.runje.dicymodel.data.PointElement;

/**
 * Created by Thomas on 13.10.2014.
 */
public class AnimatedBoard extends Board
{
    public static final String LogKey = "AnimatedBoard";
    private GamemasterAnimated gmAnimated;

    private Activity activity;

    private ArrayList<ArrayList<AnimatedBoardElement>> animatedBoard;

    private BoardLayout gameLayout;
    private AnimatedGamemaster gamemaster;

    public AnimatedBoard(int rows, int columns, Activity activity, AnimatedGamemaster gm, GamemasterAnimated gmAnimated)
    {
        super(rows, columns);
        this.activity = activity;
        this.gamemaster = gm;
        this.gmAnimated = gmAnimated;

        createAnimatedBoard(rows, columns);
    }

    public AnimatedBoard(int[] dices, Activity a, AnimatedGamemaster gm)
    {
        super((int) Math.sqrt(dices.length));
        this.gamemaster = gm;
        int root = (int) Math.sqrt(dices.length);
        this.activity = a;
        Board b = Board.createElementsBoard(dices);
        this.board = b.getBoard();
        createAnimatedBoard(root, root);

    }

    public AnimatedBoard(Board b, Activity activity, AnimatedGamemaster gm, GamemasterAnimated gamemasterAnimated)
    {
        super(b.getNumberOfRows(), b.getNumberOfColumns());
        this.activity = activity;
        this.gamemaster = gm;
        this.gmAnimated = gamemasterAnimated;

        for (int i = 0; i < b.getNumberOfRows(); i++)
        {
            for (int j = 0; j < b.getNumberOfColumns(); j++)
            {
                getElement(i, j).setValue(b.getElement(i, j).getValue());
            }
        }

        createAnimatedBoard(rows, columns);
    }

    public static AnimatedBoard createBoardNoPoints(int rows, int columns, Activity activity, Rules rules, AnimatedGamemaster gm)
    {
        while (true)
        {
            AnimatedBoard b = new AnimatedBoard(rows, columns, activity, gm, null);
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(b, rules);
            if (BoardChecker.getAll(b, rules).size() == 0 && moves.size() > 0)
            {
                AnimatedLogger.logInfo(LogKey, "Possible Moves: " + moves);
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
                row.add(new AnimatedBoardElement(this.activity, this.getElement(i, j), gamemaster, gmAnimated));
            }
        }

        // TODO: heightWeight
        this.gameLayout = new BoardLayout(this, 0.5);
    }

    public BoardLayout getBoardLayout()
    {
        return this.gameLayout;
    }

    @Override
    public void recreateBoard()
    {
        super.recreateBoard();
        updateBoard();
    }

    private void updateBoard()
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                Coords pos = new Coords(row, column);
                getAnimatedElement(pos).setValue(getElement(pos).getValue());
            }
        }
    }

    public boolean switchElements(Coords first, Coords second, boolean switchBackPossible)
    {
        AnimatedLogger.logDebug(LogKey, "Animated Switch, first: " + first + ", second: " + second + ", Board: " + this.board + "\n" + this.animatedBoard);
        Rules rules = null;
        if (gamemaster != null)
        {
            rules = gamemaster.getRules();
        }
        else
        {
            rules = gmAnimated.getRules();
        }

        boolean switchback = super.switchElements(first, second, switchBackPossible, rules);

        if (!switchBackPossible)
        {
            switchback = false;
        }

        AnimatedBoardElement firstImage = this.getAnimatedElement(first);
        AnimatedBoardElement secondImage = this.getAnimatedElement(second);

        SwitchAnimation s = new SwitchAnimation(firstImage, secondImage, switchback, gamemaster, gmAnimated);
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
                    AnimatedLogger.logDebug(LogKey, "Setting highlight for " + pos);
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
        AnimatedLogger.logInfo(LogKey, "Recreating elements: " + elements);
        recreateElements(elements);
        return elements;
    }

    /**
     * recreates the given elements
     *
     * @param elements the new elements
     */
    public void recreateElements(ArrayList<BoardElement> elements)
    {
        if (gamemaster != null)
        {
            gamemaster.lock();
        }

        super.recreateElements(elements);

        int[] max = getMaxFallLength(elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = new AnimatedBoardElement(this.activity, element, gamemaster, gmAnimated);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gameLayout.getDiceSize(), gameLayout.getDiceSize());

            int x = 0;
            int y = 0;

            switch (this.gravity)
            {
                case Up:
                    x = gameLayout.CoordsToX(pos);
                    y = gameLayout.CoordsToY(new Coords(pos.row + max[pos.column], pos.column));
                    break;
                case Down:
                    x = gameLayout.CoordsToX(pos);
                    y = gameLayout.CoordsToY(new Coords(pos.row - max[pos.column], pos.column));
                    break;
                case Right:
                    x = gameLayout.CoordsToX(new Coords(pos.row, pos.column - max[pos.row]));
                    y = gameLayout.CoordsToY(pos);
                    break;
                case Left:
                    x = gameLayout.CoordsToX(new Coords(pos.row, pos.column + max[pos.row]));
                    y = gameLayout.CoordsToY(pos);
                    break;
            }

            animatedElement.setX(x);
            animatedElement.setY(y);
            gameLayout.addView(animatedElement, params);
            AnimatedLogger.logDebug(LogKey, "Pos: " + pos + ". X: " + params.leftMargin + " Y: " + params.topMargin);
            new FallAnimation(animatedElement, pos, gamemaster, gmAnimated).start();
            if (gamemaster != null)
            {
                gamemaster.unlock();
            }
        }

    }

    public void recreateElements(ArrayList<BoardElement> elements, AnimationHandler animationHandler)
    {
        if (gamemaster != null)
        {
            gamemaster.lock();
        }

        super.recreateElements(elements);

        int[] max = getMaxFallLength(elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = new AnimatedBoardElement(this.activity, element, gamemaster, gmAnimated);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(gameLayout.getDiceSize(), gameLayout.getDiceSize());

            int x = 0;
            int y = 0;

            switch (this.gravity)
            {
                case Up:
                    x = gameLayout.CoordsToX(pos);
                    y = gameLayout.CoordsToY(new Coords(pos.row + max[pos.column], pos.column));
                    break;
                case Down:
                    x = gameLayout.CoordsToX(pos);
                    y = gameLayout.CoordsToY(new Coords(pos.row - max[pos.column], pos.column));
                    break;
                case Right:
                    x = gameLayout.CoordsToX(new Coords(pos.row, pos.column - max[pos.row]));
                    y = gameLayout.CoordsToY(pos);
                    break;
                case Left:
                    x = gameLayout.CoordsToX(new Coords(pos.row, pos.column + max[pos.row]));
                    y = gameLayout.CoordsToY(pos);
                    break;
            }

            animatedElement.setX(x);
            animatedElement.setY(y);
            gameLayout.addView(animatedElement, params);
            AnimatedLogger.logDebug(LogKey, "Pos: " + pos + ". X: " + params.leftMargin + " Y: " + params.topMargin);
            animationHandler.addAnimation(new FallingAnimation(animatedElement, pos, this, animationHandler));

        }

        animationHandler.start();
        if (gamemaster != null)
        {
            gamemaster.unlock();
        }

    }


    /**
     * Gets the maximum fall totalLength for each column/row depending on the gravity.
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
                    //+1 for totalLength
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
                    //+1 for totalLength
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
                    //+1 for totalLength
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
                    //+1 for totalLength
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
            AnimatedLogger.logError(LogKey, this.toString());
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
        AnimatedLogger.logInfo(LogKey, "Falling elements: " + elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = this.getAnimatedElement(pos);

            Coords newPos = determineFallingPosition(element);
            new FallAnimation(animatedElement, newPos, gamemaster, gmAnimated).start();
        }

        super.moveElementsFromGravity();
        return elements;
    }

    public ArrayList<BoardElement> moveElementsFromGravity(AnimationHandler animationHandler)
    {
        ArrayList<BoardElement> elements = this.determineFallingElements();
        AnimatedLogger.logInfo(LogKey, "Falling elements: " + elements);
        for (BoardElement element : elements)
        {
            Coords pos = element.getPosition();
            AnimatedBoardElement animatedElement = this.getAnimatedElement(pos);

            Coords newPos = determineFallingPosition(element);
            animationHandler.addAnimation(new FallingAnimation(animatedElement, newPos, this, animationHandler));
        }

        animationHandler.start();
        super.moveElementsFromGravity();
        return elements;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void highlightElements(Move move)
    {
        List<AnimatedBoardElement> l = new ArrayList<>();
        l.add(getAnimatedElement(move.getFirst()));
        l.add(getAnimatedElement(move.getSecond()));
        float brightness = 100;
        float[] colorMatrix = {1f, 0f, 0f, 0, brightness, // red
                0f, 1f, 0f, 0, brightness, // green
                1f, 1f, 1f, 0, brightness, // blue
                0, 0, 0, 1, 0 // alpha
        };

        ColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        for (AnimatedBoardElement e : l)
        {
            e.setColorFilter(filter);
        }
    }

    public void changeToSelectListener()
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                this.animatedBoard.get(row).get(column).getTouchListener().setSwitchEnabled(false);
            }
        }
    }

    public void changeToSwitchListener()
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                this.animatedBoard.get(row).get(column).getTouchListener().setSwitchEnabled(true);
            }
        }
    }

    public void disable()
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                this.animatedBoard.get(row).get(column).getTouchListener().setDisabled(true);
            }
        }
    }

    public void enable()
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                this.animatedBoard.get(row).get(column).getTouchListener().setDisabled(false);
            }
        }
    }

    public void changeElement(Coords position, int newValue)
    {
        super.changeElement(position, newValue);
        getAnimatedElement(position).setValue(newValue);
    }

    public boolean hasNoElements()
    {
        for (ArrayList<BoardElement> row : this.board)
        {
            for (BoardElement element : row)
            {
                if (element instanceof NoElement)
                {
                    return true;
                }

            }
        }

        return false;
    }

    public void recreateBoard(Board board)
    {
        for (int row = 0; row < this.rows; row++)
        {
            for (int column = 0; column < this.columns; column++)
            {
                Coords pos = new Coords(row, column);
                getElement(pos).setValue(board.getElement(pos).getValue());
                getAnimatedElement(pos).setValue(board.getElement(pos).getValue());
            }
        }
    }

    public GamemasterAnimated getGamemaster()
    {
        return gmAnimated;
    }
}
