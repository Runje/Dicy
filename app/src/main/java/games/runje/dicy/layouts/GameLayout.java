package games.runje.dicy.layouts;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import games.runje.dicy.R;

/**
 * Created by Thomas on 23.03.2015.
 */
public class GameLayout extends FrameLayout
{
    private BoardLayout board;
    private View leftPanel;
    private RelativeLayout rightPanel;
    private View playerAbove;
    private View playerBelow;

    public GameLayout(Activity activity, BoardLayout board, View left, RelativeLayout right, View above, View below)
    {
        super(activity);
        RelativeLayout rl = new RelativeLayout(activity);
        this.board = board;
        this.leftPanel = left;

        this.rightPanel = right;
        this.playerAbove = above;
        this.playerBelow = below;

        board.setId(View.generateViewId());
        leftPanel.setId(View.generateViewId());
        rightPanel.setId(View.generateViewId());
        playerAbove.setId(View.generateViewId());
        playerBelow.setId(View.generateViewId());

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;


        //board.setMaxWidth(2/3.0 * screenWidth);

        RelativeLayout.LayoutParams boardParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        boardParams.addRule(RelativeLayout.BELOW, left.getId());
        boardParams.topMargin = 20;
        //boardParams.addRule(RelativeLayout.RIGHT_OF, leftPanel.getId());

        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //leftParams.addRule(RelativeLayout.BELOW, playerAbove.getId());

        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ABOVE, board.getId());
        rightParams.addRule(RelativeLayout.RIGHT_OF, left.getId());
        rightParams.leftMargin = 200;
        rightParams.bottomMargin = 20;

        RelativeLayout.LayoutParams aboveParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        aboveParams.addRule(RelativeLayout.RIGHT_OF, playerBelow.getId());
        aboveParams.addRule(RelativeLayout.BELOW, board.getId());
        aboveParams.leftMargin = 20;
        aboveParams.topMargin = 20;

        RelativeLayout.LayoutParams belowParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        belowParams.addRule(RelativeLayout.BELOW, board.getId());
        belowParams.topMargin = 20;


        rl.addView(board, boardParams);
        rl.addView(leftPanel, leftParams);
        rl.addView(rightPanel, rightParams);
        rl.addView(playerAbove, aboveParams);
        rl.addView(playerBelow, belowParams);

        addView(rl, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        setId(R.id.points);
    }

    public BoardLayout getBoard()
    {
        return board;
    }


}
