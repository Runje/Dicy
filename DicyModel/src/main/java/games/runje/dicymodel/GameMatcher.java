package games.runje.dicymodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 23.02.2015.
 */
public class GameMatcher
{
    private static List<Long> playerPool = new ArrayList<>();
    private static int WaitingTimeInSeconds = 10;

    public static void findOpponent(long id)
    {
        long opponentId = lookForOpponent(id);
        if (opponentId != 0)
        {
            System.out.println("Found Game: " + opponentId + " vs. " + id);
            playerPool.remove(opponentId);
        }
        else
        {
            playerPool.add(id);
            waitForOpponent(id);
        }
    }

    private static long lookForOpponent(long id)
    {
        for (int i = 0; i < playerPool.size(); i++)
        {
            Long opponentId = playerPool.get(i);
            if (opponentId == id)
            {
                continue;
            }

            return opponentId;
        }

        return 0;
    }

    private static void waitForOpponent(final long id)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int seconds = 0;
                while (seconds < WaitingTimeInSeconds)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    long opponentId = lookForOpponent(id);
                    if (opponentId != 0)
                    {
                        System.out.println("Found Game: " + opponentId + " vs. " + id);
                        playerPool.remove(opponentId);
                        playerPool.remove(id);
                    }

                    seconds++;
                }

                System.out.println("Waited too long");


            }
        }).start();

    }
}
