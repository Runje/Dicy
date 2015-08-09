package games.runje.dicymodel;

import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 16.03.2015.
 */
public interface GameControls
{
    void setEnabledControls(boolean enabled);

    boolean areControlsEnabled();

    void update();

    void setSkillEnabled(Skill skill);

    void restore();

    void save();

    void setPointLimit(int i);

    void highlightPoints();

    void clearHighlights();
}
