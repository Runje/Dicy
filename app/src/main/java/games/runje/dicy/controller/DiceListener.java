package games.runje.dicy.controller;

import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 26.04.2015.
 */
public interface DiceListener
{
    void executeOnTouch(Coords pos);

    void executeOnSwitch(Coords first, Coords second);

    int getPointsFromSwitch(Coords position, Coords second);

    void setAllEnabled(boolean b);
}
