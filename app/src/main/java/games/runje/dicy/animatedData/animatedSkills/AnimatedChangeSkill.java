package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 02.04.2015.
 */
public class AnimatedChangeSkill extends Skill
{
    public AnimatedChangeSkill(int value, int max, String name)
    {
        super(value, max, name);
        setImageId(R.drawable.bluewhitechip);
    }

    public AnimatedChangeSkill(Skill skill)
    {
        super(skill);
        setImageId(R.drawable.bluewhitechip);
    }

    @Override
    public void startWaiting(Board board, AbstractGamemaster gm)
    {
        ((AnimatedBoard) board).changeToSelectListener();
        //gm.endWait(this);
    }

    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        int newValue = 6;
        board.changeElement(getPos(), newValue);
        AnimatedBoard b = (AnimatedBoard) board;
        b.changeToSwitchListener();
        gm.endExecute();
    }


}
