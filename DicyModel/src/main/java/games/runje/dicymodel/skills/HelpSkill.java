package games.runje.dicymodel.skills;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 31.03.2015.
 */
public class HelpSkill extends Skill
{
    public HelpSkill(int value, int max)
    {
        super(value, max, Skill.Help, 0);
    }


    public HelpSkill(Skill skill)
    {
        super(skill);
    }

    @Override
    public boolean isSwitchSkill()
    {
        return true;
    }

}
