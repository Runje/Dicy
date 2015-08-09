package games.runje.dicy.controls;

import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 27.04.2015.
 */
public interface ControlHandler
{
    void nextFromUser();

    Rules getRules();

    void executeSkillFromUser(Skill skill);

    BoardLayout getBoardLayout();

    LocalGame getGame();

    boolean isEnabledBoard();

    void setEnabledBoard(boolean enabled);

    void gameOver();
}
