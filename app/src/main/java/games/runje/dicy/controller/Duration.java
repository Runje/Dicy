package games.runje.dicy.controller;

/**
 * Created by Thomas on 11.11.2015.
 */
public enum Duration
{
    Normal, Fast, Faster, Instantly;

    public double getFactor()
    {
        switch (this)
        {
            case Normal:
                return 1;
            case Fast:
                return 0.5;
            case Faster:
                return 0.25;
            case Instantly:
                return 0;
        }

        return 1;
    }
}
