package games.runje.dicy.animatedData.animatedSkills;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
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
            case Skill.Shuffle:
                return new AnimatedShuffleSkill(skill);
        }
        return null;
    }

    public static int getImageId(String skill)
    {
        switch (skill)
        {
            case Skill.Help:
                return R.drawable.blueyellowchip;
            case Skill.Change:
                return R.drawable.bluewhitechip;
            case Skill.Shuffle:
                return R.drawable.bluewhitechip;
        }

        return 0;
    }

}
