package games.runje.dicymodel.skills;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 20.06.2015.
 */
public class ShuffleSkill extends Skill
{
    public ShuffleSkill(Skill skill)
    {
        super(skill);
    }

    public ShuffleSkill(int value, int max)
    {
        super(value, max, Skill.Shuffle, 0);
    }

    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        board.shuffle(true, null);
        super.startExecute(board, gm);
    }
}
