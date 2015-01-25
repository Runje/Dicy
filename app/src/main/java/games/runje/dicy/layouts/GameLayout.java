package games.runje.dicy.layouts;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.controller.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;

/**
 * Created by thomas on 25.01.15.
 */
public class GameLayout extends RelativeLayout
{
    AnimatedBoard board;

    public int getDiceSize()
    {
        return diceSize;
    }

    int diceSize;

    public GameLayout(AnimatedBoard b, double heightWeight)
    {
        super(b.getActivity());

        this.board = b;
        DisplayMetrics dm = new DisplayMetrics();
        board.getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        diceSize = screenWidth / (board.getNumberOfColumns() + 1);
        // min(halfHeight, diceSize)
        int halfHeight = (int) (heightWeight * screenHeight / (board.getNumberOfRows() + 1));
        diceSize = Math.min(diceSize, halfHeight);
        createAbsoluteLayout();
    }

    private void createAbsoluteLayout()
    {

        RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(diceSize * (board.getNumberOfRows() + 1), diceSize * (board.getNumberOfColumns() + 1));
        this.setLayoutParams(tableParams);

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
        return getDiceSize() * (pos.column + 1);
    }

    public int CoordsToY(Coords pos)
    {
        // offset + 1 to have one row on top for fall down
        return getDiceSize() * (pos.row + 1);
    }
}
