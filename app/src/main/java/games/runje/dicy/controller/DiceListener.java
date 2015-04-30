package games.runje.dicy.controller;

import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 26.04.2015.
 */
public interface DiceListener
{
    public void executeOnTouch(Coords pos);

    public void executeOnSwitch(Coords first, Coords second);
}
