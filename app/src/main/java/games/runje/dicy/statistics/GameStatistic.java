package games.runje.dicy.statistics;

/**
 * Created by Thomas on 15.06.2015.
 */
public class GameStatistic
{
    private PlayerStatistic player1;

    private PlayerStatistic player2;

    // 0 or 1
    private int wonIndex = -1;

    public PlayerStatistic getPlayer1()
    {
        return player1;
    }

    public void setPlayer1(PlayerStatistic player1)
    {
        this.player1 = player1;
    }

    public PlayerStatistic getPlayer2()
    {
        return player2;
    }

    public void setPlayer2(PlayerStatistic player2)
    {
        this.player2 = player2;
    }

    public int getWonIndex()
    {
        return wonIndex;
    }

    public void setWonIndex(short wonIndex)
    {
        this.wonIndex = wonIndex;
    }

    public GameStatistic(PlayerStatistic player1, PlayerStatistic player2, int wonIndex)
    {

        this.player1 = player1;
        this.player2 = player2;
        this.wonIndex = wonIndex;
    }
}
