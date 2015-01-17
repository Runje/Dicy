package games.runje.dicy.controls;

import android.view.View;

import games.runje.dicy.controller.Gamemaster;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 06.01.2015.
 */
public class GravityListener implements View.OnClickListener
{
    Gravity gravity;

    public GravityListener(Gravity g)
    {
        this.gravity = g;
    }

    @Override
    public void onClick(View view)
    {
        Gamemaster.getInstance().getBoard().setGravity(this.gravity);
        Gamemaster.getInstance().updateGravity();
    }
}
