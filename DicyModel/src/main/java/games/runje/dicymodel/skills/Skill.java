package games.runje.dicymodel.skills;

import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;

/**
 * Created by thomas on 08.02.15.
 */
public class Skill
{
    public static final String Help = "Help";
    public static final String Change = "Change";
    public static final String Shuffle = "Shuffle";
    public static final String[] AllSkills =  { Help, Change, Shuffle};
    protected int imageId;

    // TODO: POS?
    protected Coords pos = new Coords(-100, -100);
    protected boolean waiting;
    protected boolean switchSkill = false;
    private int loadValue;
    private int maxLoad;
    private int currentLoad;
    private String name;

    public Skill(Skill skill)
    {
        this(skill.getLoadValue(), skill.getMaxLoad(), skill.getName(), skill.getCurrentLoad());
    }

    public Skill(int value, int max, String name, int currentLoad)
    {
        loadValue = value;
        maxLoad = max;

        this.currentLoad = currentLoad;
        this.name = name;

    }

    public static Skill createSkill(String skill, int skillValue, int max)
    {
        switch (skill)
        {
            case Skill.Help:
                return new HelpSkill(skillValue, max);

            case Skill.Change:
                return new ChangeSkill(skillValue, max);

            case Skill.Shuffle:
                return new ShuffleSkill(skillValue, max);
        }

        return null;
    }

    public boolean isWaiting()
    {
        return waiting;
    }

    public void setWaiting(boolean waiting)
    {
        Logger.logInfo(name, "set waiting " + waiting);
        this.waiting = waiting;
    }

    public int getLoadValue()
    {
        return loadValue;
    }

    public void load(int number)
    {
        currentLoad += number;

        if (currentLoad > maxLoad)
        {
            currentLoad = maxLoad;
        }
    }

    public boolean isExecutable()
    {
        return maxLoad <= currentLoad;
    }

    public String getName()
    {
        return name;
    }

    public void pay()
    {
        currentLoad -= maxLoad;

        if (currentLoad < 0)
        {
            currentLoad = 0;
        }
    }

    public int getMaxLoad()
    {
        return maxLoad;
    }

    public int getCurrentLoad()
    {
        return currentLoad;
    }

    public void setCurrentLoad(int currentLoad)
    {
        this.currentLoad = currentLoad;
    }

    public Coords getPos()
    {
        return pos;
    }

    public void setPos(Coords pos)
    {
        this.pos = pos;
    }

    public void startWaiting(Board board, AbstractGamemaster gm, boolean isPlaying)
    {
       waiting = true;
    }

    public void execute(Board board, AbstractGamemaster gm)
    {
        startExecute(board, gm);
    }

    protected void startExecute(Board board, AbstractGamemaster gm)
    {
        Logger.logDebug("Skill", "start Execute in Base");
        gm.endExecuteSkill();
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public boolean isSwitchSkill()
    {
        return switchSkill;
    }

    public void setSwitchSkill(boolean switchSkill)
    {
        this.switchSkill = switchSkill;
    }

    @Override
    public String toString()
    {
        return "Skill{" +
                "imageId=" + imageId +
                ", pos=" + pos +
                ", waiting=" + waiting +
                ", switchSkill=" + switchSkill +
                ", loadValue=" + loadValue +
                ", maxLoad=" + maxLoad +
                ", currentLoad=" + currentLoad +
                ", name='" + name + '\'' +
                '}';
    }
}
