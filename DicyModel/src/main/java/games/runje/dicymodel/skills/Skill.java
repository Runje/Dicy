package games.runje.dicymodel.skills;

/**
 * Created by thomas on 08.02.15.
 */
public class Skill
{
    private int loadValue;
    private int maxLoad;
    private int currentLoad;

    public static final String Help = "Help";
    private String name;
    private int currentValue;

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

    public void execute()
    {
        currentLoad -= maxLoad;
    }

    public int getMaxLoad()
    {
        return maxLoad;
    }

    public int getCurrentValue()
    {
        return currentValue;
    }
}
