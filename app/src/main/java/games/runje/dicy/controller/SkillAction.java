package games.runje.dicy.controller;

import games.runje.dicymodel.skills.Skill;

/**
 * Created by thomas on 08.02.15.
 */
public class SkillAction extends Action
{
    private Skill skill;
    private AnimatedGamemaster gamemaster;

    public SkillAction(Skill skill, AnimatedGamemaster gm)
    {
        this.skill = skill;
        this.gamemaster = gm;
    }

    @Override
    public void execute()
    {
        gamemaster.executeSkill(skill, gamemaster.getFromId());
    }

    @Override
    public boolean isPossible()
    {
        // TODO: Only if loaded or it costs
        return true;
    }
}
