package games.runje.dicy.controller;

import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 26.04.2015.
 */
public interface BoardListener extends DiceListener
{
    void changeGravityFromUser(Gravity newGravity);
}
