package games.runje.dicy.statistics;

/**
 * Created by Thomas on 15.06.2015.
 */
public class PlayerStatistic
{
    private long games;

    private long wins;

    private String name;

    private long id;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId()
    {

        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public PlayerStatistic(long id, String name, long games, long wins)
    {
        this.name = name;
        this.id = id;

        this.games = games;
        this.wins = wins;
    }

    public long getGames()
    {

        return games;
    }

    public void setGames(long games)
    {
        this.games = games;
    }

    public long getWins()
    {
        return wins;
    }

    public void setWins(long wins)
    {
        this.wins = wins;
    }

    @Override
    public String toString()
    {
        return "PlayerStatistic{" +
                "games=" + games +
                ", wins=" + wins +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public void increaseGames()
    {
        games++;
    }

    public void increaseWins()
    {
        wins++;
    }
}
