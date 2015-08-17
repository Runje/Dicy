package games.runje.dicymodel;

import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 13.04.2015.
 */
public class DummyControls implements GameControls
{
    @Override
    public void setEnabledControls(boolean enabled)
    {

    }

    @Override
    public boolean areControlsEnabled()
    {
        return false;
    }

    @Override
    public void update()
    {

    }

    @Override
    public void setSkillEnabled(Skill skill)
    {

    }

    @Override
    public void restore()
    {

    }

    @Override
    public void save()
    {

    }

    @Override
    public void setPointLimit(int i)
    {

    }

    @Override
    public void onResume()
    {

    }

    @Override
    public void onPause()
    {

    }

    @Override
    public void highlightPoints()
    {

    }

    @Override
    public void clearHighlights()
    {

    }

}
