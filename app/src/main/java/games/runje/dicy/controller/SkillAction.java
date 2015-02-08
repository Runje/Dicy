package games.runje.dicy.controller;

import games.runje.dicymodel.skills.Skill;

/**
 * Created by thomas on 08.02.15.
 */
public class SkillAction extends Action
{
    private Skill skill;
    public SkillAction(Skill skill)
    {
        this.skill = skill;
    }
    @Override
    public void execute()
    {
        Gamemaster.getInstance().executeSkill(skill);
    }

    @Override
    public boolean isPossible()
    {
        // TODO: Only if loaded or it costs
        return true;
    }
}
