package games.runje.dicy.animatedData.animatedSkills;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 31.03.2015.
 */
public class AnimatedHelpSkill extends HelpSkill
{
    public AnimatedHelpSkill(int value, int max, String name)
    {
        super(value, max, name);
        setImageId(R.drawable.blueyellowchip);
    }

    public AnimatedHelpSkill(Skill skill)
    {
        super(skill);
        setImageId(R.drawable.blueyellowchip);
    }

    @Override
    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        Move move = BoardChecker.getPossiblePointMoves(board, gm.getRules()).get(0);
        ((AnimatedBoard) board).highlightElements(move);
        gm.endExecuteSkill();
    }
}
