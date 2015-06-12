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
    protected int imageId;
    protected Coords pos;
    private int loadValue;
    private int maxLoad;
    private int currentLoad;
    private String name;
    protected boolean waiting;

    public boolean isWaiting()
    {
        return waiting;
    }

    public Skill(Skill skill)
    {
        this(skill.getLoadValue(), skill.getMaxLoad(), skill.getName());
    }

    public Skill(int value, int max, String name)
    {
        loadValue = value;
        maxLoad = max;

        currentLoad = 0;
        this.name = name;

    }

    public int getLoadValue()
    {
        return loadValue;
    }

    public void load(int number)
    {
        currentLoad += number;
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

    public Coords getPos()
    {
        return pos;
    }

    public void setPos(Coords pos)
    {
        this.pos = pos;
    }

    public void startWaiting(Board board, AbstractGamemaster gm)
    {
        gm.endWait(null);
    }

    public void execute(Board board, AbstractGamemaster gm)
    {
        pay();
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
}
