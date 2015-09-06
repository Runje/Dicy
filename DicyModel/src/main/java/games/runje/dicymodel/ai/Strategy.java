package games.runje.dicymodel.ai;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;

/**
 * Created by Thomas on 18.01.2015.
 */
public class Strategy
{
    public static final String Human = "Human";
    public static final String Simple = "Simple";

    private double seeFalling;
    private double risk;
    private double seePoints;
    private double gravitation;
    private double skills;
    private double error;

    public Strategy()
    {
    }

    public Strategy(double seeFalling, double risk, double seePoints, double gravitation, double skills, double error)
    {

        this.seeFalling = seeFalling;
        this.risk = risk;
        this.seePoints = seePoints;
        this.gravitation = gravitation;
        this.skills = skills;
        this.error = error;

    }

    public static Move getBestSwitchMove(List<Move> moves)
    {
        int max = 0;
        Move maxMove = null;
        for (Move move : moves)
        {
            if (move.getPoints() > max)
            {
                maxMove = move;
                max = move.getPoints();
            }
        }

        return maxMove;
    }

    public static Strategy getStrategy(String strategy)
    {
        switch (strategy)
        {
            case Human:
                return null;

            case Simple:
                return new Strategy(0.5, 0.5, 0.5, 0.5, 0.5, 0.5);
        }

        String[] parts = strategy.split(",");
        List<Double> values = new ArrayList<>();
        for (String part : parts)
        {
            int index = part.indexOf('=');
            int index2 = part.indexOf('}');
            if (index2 != -1)
            {
                values.add(Double.parseDouble(part.substring(index + 1, index2)));
            } else
            {
                values.add(Double.parseDouble(part.substring(index + 1)));
            }
        }

        return new Strategy(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5));
    }

    public double getError()
    {
        return error;
    }

    public void setError(double error)
    {
        this.error = error;
    }

    public double getSeeFalling()
    {

        return seeFalling;
    }

    public void setSeeFalling(double seeFalling)
    {
        this.seeFalling = seeFalling;
    }

    public double getRisk()
    {
        return risk;
    }

    public void setRisk(double risk)
    {
        this.risk = risk;
    }

    public double getSeePoints()
    {
        return seePoints;
    }

    public void setSeePoints(double seePoints)
    {
        this.seePoints = seePoints;
    }

    public double getGravitation()
    {
        return gravitation;
    }

    public void setGravitation(double gravitation)
    {
        this.gravitation = gravitation;
    }

    public double getSkills()
    {
        return skills;
    }

    public void setSkills(double skills)
    {
        this.skills = skills;
    }

    public Move getNextMove(Rules rules, Board board)
    {
        List<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);
        return getBestSwitchMove(moves);
    }

    @Override
    public String toString()
    {
        return "Strategy{" +
                "seeFalling=" + seeFalling +
                ", risk=" + risk +
                ", seePoints=" + seePoints +
                ", gravitation=" + gravitation +
                ", skills=" + skills +
                ", error=" + error +
                '}';
    }

    public double getValue()
    {
        return 100 / 3.0 * (seeFalling + seePoints + (1 - error));
    }
}
