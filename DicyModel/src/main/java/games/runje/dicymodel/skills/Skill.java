package games.runje.dicymodel.skills;

/**
 * Created by thomas on 08.02.15.
 */
public class Skill
{
    public static final String Help = "Help";
    public static final String Change = "Change";
    private int loadValue;
    private int maxLoad;
    private int currentLoad;
    private String name;

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
}
