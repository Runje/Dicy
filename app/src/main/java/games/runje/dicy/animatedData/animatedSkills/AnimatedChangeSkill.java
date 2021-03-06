package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 02.04.2015.
 */
public class AnimatedChangeSkill extends Skill
{


    private String LogKey = "AnimatedChangeSkill";

    public AnimatedChangeSkill(Skill skill)
    {
        super(skill);
        setImageId(R.drawable.bluewhitechip);
    }

    @Override
    public void startWaiting(Board board, AbstractGamemaster gm, boolean isPlaying)
    {
        AnimatedBoard animatedBoard = ((AnimatedBoard) board);
        if (waiting)
        {
            Logger.logInfo(LogKey, "Reverting Change Skill Waiting");
            // revert action
            waiting = false;
            if (isPlaying)
            {
                animatedBoard.changeToSwitchListener();
            }

            gm.cancelWaiting();
        }
        else
        {
            waiting = true;
            if (isPlaying)
            {
                animatedBoard.changeToSelectListener();
                animatedBoard.getBoardLayout().setEnabledGravity(true);

                gm.getControls().setSkillEnabled(this);
            }
        }
    }

    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        waiting = false;
        int newValue = this.getLoadValue();
        board.changeElement(getPos(), newValue);
        AnimatedBoard b = (AnimatedBoard) board;
        b.changeToSwitchListener();
        gm.endExecuteSkill();
    }


}
