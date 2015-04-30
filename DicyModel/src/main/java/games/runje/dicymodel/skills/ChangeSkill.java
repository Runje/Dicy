package games.runje.dicymodel.skills;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 21.04.2015.
 */
public class ChangeSkill extends Skill
{
    public ChangeSkill(Skill skill)
    {
        super(skill);
    }

    public ChangeSkill(int value, int max)
    {
        super(value, max, Skill.Change);
    }


    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        // TODO
        board.getElement(pos).setValue(6);
        Logger.logInfo("ChangeSkill", "Setting value = 6 of " + pos);
        super.startExecute(board, gm);
    }
}
