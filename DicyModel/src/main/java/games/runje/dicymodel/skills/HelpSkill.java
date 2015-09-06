package games.runje.dicymodel.skills;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;

/**
 * Created by Thomas on 31.03.2015.
 */
public class HelpSkill extends Skill
{
    protected Move move;

    public HelpSkill(int value, int max)
    {
        super(value, max, Skill.Help, 0);
    }

    public HelpSkill(Skill skill)
    {
        super(skill);
    }

    @Override
    public void startWaiting(Board board, AbstractGamemaster gm, boolean isPlaying)
    {
        move = Strategy.getBestSwitchMove(BoardChecker.getPossiblePointMoves(board, gm.getRules()));
    }

    @Override
    public boolean isSwitchSkill()
    {
        return true;
    }

    public Move getMove()
    {
        return move;
    }

    public void setMove(Move move)
    {
        this.move = move;
    }
}
