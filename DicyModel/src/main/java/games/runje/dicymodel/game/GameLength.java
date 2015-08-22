package games.runje.dicymodel.game;

/**
 * Created by Thomas on 21.08.2015.
 */
public enum GameLength
{
    Short, Normal, Long;

    public static int LengthToFactor(GameLength length)
    {
        int f = 0;
        switch (length)
        {
            case Short:
                f = 5;
                break;
            case Normal:
                f = 10;
                break;
            case Long:
                f = 20;
                break;
        }

        return f;
    }
}
