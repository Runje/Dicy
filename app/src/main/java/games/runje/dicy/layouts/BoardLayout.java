package games.runje.dicy.layouts;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by thomas on 25.01.15.
 */
public class BoardLayout extends RelativeLayout implements View.OnClickListener
{
    private final String LogKey = "BoardLayout";
    AnimatedBoard board;
    int diceSize;
    private int XOffset = 1;
    private int YOffset = 1;
    private Border above;
    private Border below;
    private Border left;
    private Border right;

    public BoardLayout(AnimatedBoard b, double heightWeight)
    {
        super(b.getActivity());
        LinearLayout boardContainer = (LinearLayout) b.getGamemaster().getActivity().findViewById(R.id.board);
        this.board = b;
        DisplayMetrics dm = new DisplayMetrics();
        board.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        screenWidth = boardContainer.getWidth();
        screenHeight = boardContainer.getHeight();

        Logger.logInfo(LogKey, "Width: " + screenWidth + ", Height: " + screenHeight);

        diceSize = screenWidth / (board.getNumberOfColumns() + 3);
        // min(halfHeight, diceSize)
        int halfHeight = screenHeight / (board.getNumberOfRows() + 3);
        diceSize = Math.min(diceSize, halfHeight);
        createAbsoluteLayout();
        //this.setBackgroundColor(Color.BLUE);
    }

    public int getYOffset()
    {
        return YOffset;
    }

    public void setYOffset(int YOffset)
    {
        this.YOffset = YOffset;

    }

    public int getXOffset()
    {
        return XOffset;
    }

    public void setXOffset(int XOffset)
    {
        this.XOffset = XOffset;
    }

    public int getDiceSize()
    {
        return diceSize;
    }

    private void createAbsoluteLayout()
    {

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(diceSize * (board.getNumberOfRows() + 2), diceSize * (board.getNumberOfColumns() + 2));
        this.setLayoutParams(tableParams);


        //make borders
        // above
        RelativeLayout.LayoutParams paramsA = new RelativeLayout.LayoutParams(diceSize * (board.getNumberOfColumns() + 1), diceSize);
        paramsA.leftMargin = CoordsToX(new Coords(0, -1));
        this.above = new Border(getContext());
        above.setId(R.id.borderAbove);
        above.setOnClickListener(this);
        this.addView(above, paramsA);

        // below
        RelativeLayout.LayoutParams paramsB = new RelativeLayout.LayoutParams(diceSize * (board.getNumberOfColumns() + 1), diceSize);
        paramsB.leftMargin = CoordsToX(new Coords(0, -1));
        paramsB.topMargin = CoordsToY(new Coords(board.getNumberOfRows(), 0));
        this.below = new Border(getContext());
        below.setId(R.id.borderBelow);
        below.setOnClickListener(this);
        this.addView(below, paramsB);

        // left
        RelativeLayout.LayoutParams paramsL = new RelativeLayout.LayoutParams(diceSize, diceSize * (board.getNumberOfColumns() + 1));
        paramsL.leftMargin = CoordsToX(new Coords(0, -2));
        paramsL.topMargin = CoordsToY(new Coords(-1, 0));
        this.left = new Border(getContext());
        left.setId(R.id.borderLeft);
        left.setOnClickListener(this);
        this.addView(this.left, paramsL);

        // right
        RelativeLayout.LayoutParams paramsR = new RelativeLayout.LayoutParams(diceSize, diceSize * (board.getNumberOfColumns() + 1));
        paramsR.leftMargin = CoordsToX(new Coords(1, board.getNumberOfColumns()));
        paramsR.topMargin = CoordsToY(new Coords(-1, 0));
        this.right = new Border(getContext());
        right.setOnClickListener(this);
        this.right.setId(R.id.borderRight);
        this.addView(right, paramsR);

        // make background
        for (int row = -1; row < board.getNumberOfRows(); row++)
        {
            for (int column = -1; column < board.getNumberOfColumns(); column++)
            {
                Coords pos = new Coords(row, column);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diceSize, diceSize);
                params.leftMargin = CoordsToX(pos);
                params.topMargin = CoordsToY(pos);

                ImageView background = new ImageView(getContext());
                background.setImageResource(R.drawable.dicy_square);
                this.addView(background, params);
            }
        }

        for (int row = 0; row < board.getNumberOfRows(); row++)
        {
            for (int column = 0; column < board.getNumberOfColumns(); column++)
            {
                AnimatedBoardElement iv = board.getAnimatedElement(new Coords(row, column));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(diceSize, diceSize);
                params.leftMargin = CoordsToX(iv.getPosition());
                params.topMargin = CoordsToY(iv.getPosition());

                this.addView(iv, params);
            }
        }
    }

    public int CoordsToX(Coords pos)
    {
        //offset + 1 to have one column on the left for fall down
        return getDiceSize() * (pos.column + XOffset + 1);
    }

    public int CoordsToY(Coords pos)
    {
        // offset + 1 to have one row on top for fall down
        return getDiceSize() * (pos.row + YOffset + 1);
    }

    public void setMaxWidth(double maxWidth)
    {
        diceSize = (int) maxWidth / (board.getNumberOfColumns() + 1);
        createAbsoluteLayout();
    }

    public Border getAboveBorder()
    {
        return above;
    }

    public Border getBelowBorder()
    {
        return below;
    }

    public Border getLeftBorder()
    {
        return left;
    }

    public Border getRightBorder()
    {
        return right;
    }

    @Override
    public void onClick(View view)
    {
        Gravity newGravity = null;
        switch (view.getId())
        {
            case (R.id.borderAbove):
                newGravity = Gravity.Up;
                break;
            case (R.id.borderBelow):
                newGravity = Gravity.Down;
                break;
            case (R.id.borderLeft):
                newGravity = Gravity.Left;
                break;
            case (R.id.borderRight):
                newGravity = Gravity.Right;
                break;
        }

        board.getGamemaster().changeGravityFromUser(newGravity);
    }

    public void updateGravity(Gravity gravity)
    {
        int brown = Color.parseColor("#795548");
        above.setBackgroundColor(brown);
        below.setBackgroundColor(brown);
        left.setBackgroundColor(brown);
        right.setBackgroundColor(brown);

        int yellow = Color.YELLOW;

        switch (gravity)
        {

            case Up:
                board.getBoardLayout().setYOffset(0);
                above.setBackgroundColor(yellow);
                break;
            case Down:
                board.getBoardLayout().setYOffset(1);
                below.setBackgroundColor(yellow);
                break;
            case Right:
                board.getBoardLayout().setXOffset(1);
                right.setBackgroundColor(yellow);
                break;
            case Left:
                board.getBoardLayout().setXOffset(0);
                left.setBackgroundColor(yellow);
                break;
        }
    }
}
