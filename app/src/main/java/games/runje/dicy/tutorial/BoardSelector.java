package games.runje.dicy.tutorial;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 02.08.2015.
 */
public class BoardSelector
{
    private final AnimatedBoard board;
    private final ViewGroup container;
    private BoardLayout boardLayout;
    private View allHider;
    private List<List<View>> diceHider;

    public BoardSelector(AnimatedBoard board, ViewGroup boardContainer)
    {
        this.board = board;
        this.container = boardContainer;
        diceHider = new ArrayList<>();
        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            ArrayList<View> row = new ArrayList<>();
            diceHider.add(row);
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                row.add(Hider.createHideView(board.getActivity()));
            }
        }

        initBoardLayout();
    }

    private void initBoardLayout()
    {
        if (boardLayout == null)
        {
            boardLayout = (BoardLayout) container.findViewById(R.id.board_layout);
        }
    }

    public void hideAll()
    {
        if (this.allHider == null)
        {
            this.allHider = Hider.createHideView(board.getActivity());
        }

        container.addView(allHider, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        allHider.bringToFront();
        container.requestLayout();
    }

    public void clearAll()
    {
        if (this.allHider != null)
        {
            ViewUtilities.removeView(this.allHider);
        }

        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            List<View> row = diceHider.get(i);
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                ViewUtilities.removeView(row.get(j));
            }
        }

        if (boardLayout != null)
        {
            boardLayout.getAboveBorder().clearAnimation();
            boardLayout.getBelowBorder().clearAnimation();
            boardLayout.getLeftBorder().clearAnimation();
            boardLayout.getRightBorder().clearAnimation();
        }
    }

    private View getDiceHider(Coords pos)
    {
        return diceHider.get(pos.row).get(pos.column);
    }

    public void hideDice(Coords pos)
    {
        View hider = getDiceHider(pos);

        initBoardLayout();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boardLayout.diceSize, boardLayout.diceSize);
        params.leftMargin = boardLayout.CoordsToX(pos);
        params.topMargin = boardLayout.CoordsToY(pos);
        boardLayout.addView(hider, params);

    }

    public void highlightBorders()
    {
        initBoardLayout();
        Animation animation = AnimationUtils.loadAnimation(boardLayout.getContext(), R.anim.blink);
        boardLayout.getAboveBorder().startAnimation(animation);
        boardLayout.getBelowBorder().startAnimation(animation);
        boardLayout.getLeftBorder().startAnimation(animation);
        boardLayout.getRightBorder().startAnimation(animation);
    }
}
