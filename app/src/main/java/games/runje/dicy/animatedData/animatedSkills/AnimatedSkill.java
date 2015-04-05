package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 31.03.2015.
 */
public class AnimatedSkill extends Skill
{
    private int imageId;

    public AnimatedSkill(int value, int max, String name)
    {
        super(value, max, name);
    }

    public static Skill create(Skill skill)
    {
        switch (skill.getName())
        {
            case Skill.Help:
                return new AnimatedHelpSkill(skill);
            case Skill.Change:
                return new AnimatedChangeSkill(skill);
        }
        return null;
    }

    public int getImageId()
    {
        return imageId;
    }
}
