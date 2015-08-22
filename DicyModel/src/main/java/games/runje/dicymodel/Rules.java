package games.runje.dicymodel;

import games.runje.dicymodel.data.PointType;
import games.runje.dicymodel.game.GameLength;

/**
 * Created by Thomas on 01.10.2014.
 */
public class Rules
{
    /**
     * The maximum totalLength of one row/column.
     */
    public final static int maxLengthOfRow = 10;
    private final int rows;
    private final int columns;
    private int lengthFactor;
    private int timeLimitInS;
    private boolean timeLimit;
    private boolean pointLimitSetManually;
    /**
     * Points can be achieved diagonally.
     */
    private boolean diagonal = false;
    /**
     * The number of dices.
     */
    private int numberOfDices;
    /**
     * The minimum of same dices for points.
     */
    private int minXOfAKind;
    /**
     * The points for a straight. The index is the totalLength of the straight.
     */
    private int[][] straightPoints;
    /**
     * Points for x of a kind. The first index is the X, the second index is the value of the dice.
     */
    private int[][] xOfAKindPoints;
    /**
     * The minimum following dices for a straight.
     */
    private int minStraight;
    private int pointLimit;
    private int gameEndPoints;
    private GameLength gameLength;

    /**
     * Creates standard Rules:
     * 6 Dices.
     * No Diagonal.
     * No Full House.
     * No Straight.
     * 3-10 XofAKind.  (1 Points)
     */
    public Rules()
    {
        this.numberOfDices = 6;
        this.minStraight = this.numberOfDices + 1;
        this.minXOfAKind = 3;

        this.columns = 5;
        this.rows = 5;

        this.lengthFactor = 5;
        this.initStraightPoints(2);
        this.initXOfAKindPoints(this.minXOfAKind, maxLengthOfRow, 1, 2);
        // TODO: Calculate
        this.pointLimit = -1;
        this.pointLimitSetManually = false;
        this.gameLength = GameLength.Normal;
    }

    public boolean isTimeLimit()
    {
        return timeLimit;
    }

    public void setTimeLimit(boolean timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public int getTimeLimitInS()
    {
        return timeLimitInS;
    }

    public void setTimeLimitInS(int timeLimitInS)
    {
        this.timeLimitInS = timeLimitInS;
    }

    @Override
    public String toString()
    {
        return "Rules{" +
                "rows=" + rows +
                ", columns=" + columns +
                ", lengthFactor=" + lengthFactor +
                ", pointLimitSetManually=" + pointLimitSetManually +
                ", diagonal=" + diagonal +
                ", numberOfDices=" + numberOfDices +
                ", minXOfAKind=" + minXOfAKind +
                ", minStraight=" + minStraight +
                ", pointLimit=" + pointLimit +
                ", gameEndPoints=" + gameEndPoints +
                '}';
    }

    public int getGameEndPoints()
    {
        return gameEndPoints;
    }

    public void setGameEndPoints(int gameEndPoints)
    {
        this.gameEndPoints = gameEndPoints;
    }

    public int getLengthFactor()
    {
        return lengthFactor;
    }

    public void setLengthFactor(int lengthFactor)
    {
        this.lengthFactor = lengthFactor;
        this.gameEndPoints = lengthFactor * pointLimit;
    }

    public int getColumns()
    {
        return columns;
    }

    public int getRows()
    {
        return rows;
    }

    public boolean isPointLimitSetManually()
    {
        return pointLimitSetManually;
    }

    public void setPointLimitSetManually(boolean pointLimitSetManually)
    {
        this.pointLimitSetManually = pointLimitSetManually;
    }

    /**
     * Initializes the points for x of a kind.
     *
     * @param min    Minimum number of same dices for points.
     * @param max    Maximum number of same dices for points.
     * @param points Points multiplicator depending on the value of the dice.
     * @param factor The increase factor for each more same dice.
     */
    private void initXOfAKindPoints(int min, int max, int points, int factor)
    {
        this.xOfAKindPoints = new int[max + 1][this.numberOfDices + 1];
        int f = 1;
        for (int x = 0; x < max + 1; x++)
        {
            for (int dice = 0; dice < this.numberOfDices + 1; dice++)
            {
                int p;
                if (x < min || dice == 0)
                {
                    p = 0;
                }
                else
                {
                    p = points * dice * f;
                }
                this.xOfAKindPoints[x][dice] = p;
            }

            // increase factor
            f *= factor;
        }

        int[][] xPoints = {{0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 45, 20, 25, 30, 35, 40},
                {0, 155, 55, 75, 95, 115, 135},
                {0, 1000, 400, 500, 600, 700, 800}
        };

        xOfAKindPoints = xPoints;
    }


    /**
     * Initialize the straight points array. The first straight gives "points" points, for each dice more the points are doubled.
     *
     * @param points The first active straight gives "points" points, for each dice more the points are doubled.
     */
    public void initStraightPoints(int points)
    {
        this.straightPoints = new int[this.numberOfDices + 1][this.numberOfDices + 1];
        int factor = 1;
        for (int j = 0; j < this.numberOfDices + 1; j++)
        {
            for (int i = 0; i < this.numberOfDices + 1; i++)
            {
                //if (this.minStraight <= i)
                {
                    this.straightPoints[j][i] = points * factor;
                    // double the factor
                    factor *= 2;
                }
                //else
                {
                    //this.straightPoints[i] = 0;
                }
            }
        }


        int[][] sPoints = {{0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 30, 15, 20, 25, 0, 0},
                {0, 120, 40, 80, 0, 0, 0},
                {0, 500, 250, 0, 0, 0, 0}
        };

        straightPoints = sPoints;
    }

    public int getMinXOfAKind()
    {
        return this.minXOfAKind;
    }

    public void setMinXOfAKind(int minXOfAKind)
    {
        this.minXOfAKind = minXOfAKind;
    }

    public int getMaxLengthOfRow()
    {
        return maxLengthOfRow;
    }

    public int getXOfAKindPoints(int x, int value)
    {
        return this.xOfAKindPoints[x][value];
    }

    public boolean isDiagonalActive()
    {
        return diagonal;
    }

    public void setDiagonalActive(boolean b)
    {
        this.diagonal = b;
    }

    public int getMinStraight()
    {
        return this.minStraight;
    }

    public void setMinStraight(int minStraight)
    {
        this.minStraight = minStraight;
    }

    public int getStraightPoints(int length, int value)
    {
        return this.straightPoints[length][value];
    }

    public int getPoints(int length, int value, PointType type)
    {
        switch (type)
        {
            case Straight:
                return this.getStraightPoints(length, value);
            case XOfAKind:
                return this.getXOfAKindPoints(length, value);
        }

        return 0;
    }

    public int getPointLimit()
    {
        return pointLimit;
    }

    public void setPointLimit(int pointLimit)
    {
        this.pointLimit = pointLimit;
        this.gameEndPoints = lengthFactor * pointLimit;
    }

    public void setStraightPoints(int[][] straightPoints)
    {
        this.straightPoints = straightPoints;
    }

    public void setxOfAKindPoints(int[][] xOfAKindPoints)
    {
        this.xOfAKindPoints = xOfAKindPoints;
    }

    public GameLength getGameLength()
    {
        return gameLength;
    }

    public void setGameLength(GameLength gameLength)
    {
        this.gameLength = gameLength;
    }
}
