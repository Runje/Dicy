package games.runje.dicy.controls;

import android.view.View;

import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 06.01.2015.
 */
public class GravityListener implements View.OnClickListener
{
    Gravity gravity;
    private AnimatedGamemaster gamemaster;

    public GravityListener(Gravity g, AnimatedGamemaster gm)
    {
        this.gravity = g;
        this.gamemaster = gm;
    }

    @Override
    public void onClick(View view)
    {

        gamemaster.updateGravity(this.gravity);
        gamemaster.sendMessageToServer(new GravityMessage(this.gravity));
    }
}
