package games.runje.dicy.tutorial;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import games.runje.dicy.R;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.layouts.GameLayout;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 04.04.2015.
 */
public class SwitchTutorialGamemaster extends GamemasterAnimated
{
    private String LogKey = "SwitchTutorialGamemaster";

    public SwitchTutorialGamemaster(Board board, Rules rules, Activity activity, GameControls controls, LocalGame game)
    {
        super(board, rules, activity, controls, game);
    }

    @Override
    public void startGame()
    {
        Logger.logInfo(LogKey, "Starting Game");
        //RelativeLayout root = (RelativeLayout) getActivity().getWindow().getDecorView().getRootView();
        GameLayout root = (GameLayout) getActivity().findViewById(R.id.points);
        Context context = root.getContext();
        ImageView image = new ImageView(context);
        image.setClickable(true);
        image.setBackgroundColor(Color.WHITE);
        image.setAlpha(0.7f);/*
        root.addView(image, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);*/


        new Hider(root.getBoard());
        //root.setForeground(getActivity().getResources().getDrawable(R.drawable.green_square));
    }
}
