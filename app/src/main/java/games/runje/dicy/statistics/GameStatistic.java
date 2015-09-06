package games.runje.dicy.statistics;

import java.util.Date;

import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 15.06.2015.
 */
public class GameStatistic
{
    private PlayerStatistic player1;

    private PlayerStatistic player2;
    private long id;
    private int p2Points;
    private int p1Points;
    private boolean p1Started;
    // 0 or 1
    private boolean p1Won;
    private RuleVariant ruleVariant;

    private Date date;

    private GameLength length;

    public GameStatistic(long id, String player1, String player2, boolean p1started, boolean p1won, int p1Points, int p2Points, Date date, GameLength length, RuleVariant rv)
    {
        this.id = id;
        this.player1 = new PlayerStatistic(player1);
        this.player2 = new PlayerStatistic(player2);
        this.p1Started = p1started;
        this.p1Won = p1won;
        this.p1Points = p1Points;
        this.p2Points = p2Points;
        this.date = date;
        this.length = length;
        this.ruleVariant = rv;
    }

    public GameStatistic(PlayerStatistic player1, PlayerStatistic player2, boolean p1Won, int p1Points, int p2Points, GameLength length, RuleVariant rv)
    {
        this.player1 = player1;
        this.player2 = player2;
        this.p1Won = p1Won;
        this.p1Points = p1Points;
        this.p2Points = p2Points;
        date = new Date();
        this.length = length;
        this.ruleVariant = rv;
    }

    public RuleVariant getRuleVariant()
    {
        return ruleVariant;
    }

    public void setRuleVariant(RuleVariant ruleVariant)
    {
        this.ruleVariant = ruleVariant;
    }

    public GameLength getLength()
    {
        return length;
    }

    public void setLength(GameLength length)
    {
        this.length = length;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public boolean isP1Started()
    {
        return p1Started;
    }

    public void setP1Started(boolean p1Started)
    {
        this.p1Started = p1Started;
    }

    public boolean isP1Won()
    {
        return p1Won;
    }

    public void setP1Won(boolean p1Won)
    {
        this.p1Won = p1Won;
    }

    public boolean hasP1Won()
    {
        return p1Won;
    }

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

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public boolean hasP1Started()
    {
        return p1Started;
    }

    public int getP2Points()
    {
        return p2Points;
    }

    public void setP2Points(int p2Points)
    {
        this.p2Points = p2Points;
    }

    public int getP1Points()
    {
        return p1Points;
    }

    public void setP1Points(int p1Points)
    {
        this.p1Points = p1Points;
    }

    @Override
    public String toString()
    {
        return "GameStatistic{" +
                "player1=" + player1 +
                ", player2=" + player2 +
                ", id=" + id +
                ", p2Points=" + p2Points +
                ", p1Points=" + p1Points +
                ", p1Started=" + p1Started +
                ", p1Won=" + p1Won +
                ", ruleVariant=" + ruleVariant +
                ", date=" + date +
                ", length=" + length +
                '}';
    }
}
