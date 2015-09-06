package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 31.03.2015.
 */
public class AnimatedHelpSkill extends HelpSkill
{
    private String LogKey = "AnimatedHelpSkill";

    public AnimatedHelpSkill(Skill skill)
    {
        super(skill);
    }

    @Override
    public void startWaiting(Board board, AbstractGamemaster gm, boolean isPlaying)
    {
        super.startWaiting(board, gm, isPlaying);
        AnimatedBoard animatedBoard = ((AnimatedBoard) board);
        if (waiting)
        {
            Logger.logInfo(LogKey, "Reverting Help Skill Waiting");
            // Do nothing, can not be reverted
            // revert action
        }
        else
        {
            waiting = true;

            ((AnimatedBoard) board).highlightElements(move);
            animatedBoard.getBoardLayout().setEnabledGravity(isPlaying);
        }
    }

    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        waiting = false;
        gm.endExecuteSkill();
    }
}
