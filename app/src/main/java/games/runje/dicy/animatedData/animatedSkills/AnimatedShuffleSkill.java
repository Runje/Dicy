package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 20.06.2015.
 */
public class AnimatedShuffleSkill extends ShuffleSkill
{
    public AnimatedShuffleSkill(Skill skill)
    {
        super(skill);
        setImageId(R.drawable.blueyellowchip);
    }


}
