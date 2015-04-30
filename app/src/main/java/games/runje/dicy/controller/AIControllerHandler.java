package games.runje.dicy.controller;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.game.Game;

/**
 * Created by Thomas on 27.04.2015.
 */
public interface AIControllerHandler
{

    Game getGame();

    boolean areControlsEnabled();

    void next();

    void switchElements(Coords first, Coords second);

    Rules getRules();

    Board getBoard();
}
