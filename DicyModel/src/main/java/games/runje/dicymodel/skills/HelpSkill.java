package games.runje.dicymodel.skills;

/**
 * Created by Thomas on 31.03.2015.
 */
public class HelpSkill extends Skill
{
    public HelpSkill(int value, int max, String name)
    {
        super(value, max, name);
    }


    public HelpSkill(Skill skill)
    {
        super(skill);
    }
}
