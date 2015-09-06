package games.runje.dicy.controller;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 27.04.2015.
 */
public interface AIControllerHandler
{

    Game getGame();

    boolean areControlsEnabled();

    void next(boolean force);

    void switchElements(Coords first, Coords second);

    Rules getRules();

    Board getBoard();

    void executeSkill(Skill s);

    void changeGravity(Gravity gravity);

    void executeOnTouch(Coords first);
}
