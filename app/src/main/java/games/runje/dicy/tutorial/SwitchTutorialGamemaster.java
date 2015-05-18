package games.runje.dicy.tutorial;

import android.app.Activity;

import java.util.List;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;

/**
 * Created by Thomas on 04.04.2015.
 */
public class SwitchTutorialGamemaster extends AnimatedGamemaster
{
    public static String LogKey = "SwitchTutorialGamemaster";

    public SwitchTutorialGamemaster(Board board, Rules rules, Activity activity, List<Player> players)
    {
        super(players, rules, activity, board);
    }

    public void startGame()
    {
        Logger.logInfo(LogKey, "Starting Game");
        //RelativeLayout root = (RelativeLayout) getActivity().getWindow().getDecorView().getRootView();
        /*GameLayout root = (GameLayout) getActivity().findViewById(R.id.points);
        Context context = root.getContext();
        ImageView image = new ImageView(context);
        image.setClickable(true);
        image.setBackgroundColor(Color.WHITE);
        image.setAlpha(0.7f);/*
        root.addView(image, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);*/


        //new Hider(root.getBoard());
        //root.setForeground(getActivity().getResources().getDrawable(R.drawable.green_square));
    }
}
