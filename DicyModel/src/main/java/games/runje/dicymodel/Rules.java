package games.runje.dicymodel;

import games.runje.dicymodel.data.PointType;

/**
 * Created by Thomas on 01.10.2014.
 */
public class Rules
{
    /**
     * The maximum length of one row/column.
     */
    public final static int maxLengthOfRow = 10;
    /**
     * Points can be achieved diagonally.
     */
    private boolean diagonal = false;
    /**
     * The number of dices.
     */
    private int numberOfDices;
    /**
     * If a Full House gives Points.
     */
    private boolean fullHouseActive = false;
    /**
     * The minimum of same dices for points.
     */
    private int minXOfAKind;
    /**
     * The points for a straight. The index is the length of the straight.
     */
    private int[] straightPoints;

    /**
     * The points for a Full House. The index is the value of the three of a kind.
     */
    private int[] fullHousePoints;

    /**
     * Points for x of a kind. The first index is the X, the second index is the value of the dice.
     */
    private int[][] xOfAKindPoints;

    /**
     * The minimum following dices for a straight.
     */
    private int minStraight;
    private int pointLimit;

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

        this.initStraightPoints(2);
        this.initFullHousePoints(0);
        this.initXOfAKindPoints(this.minXOfAKind, this.maxLengthOfRow, 1, 2);
        // TODO: Calculate
        this.pointLimit = 65;
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
    }

    /**
     * Initialize the full house points.
     *
     * @param points Points for the lowest Full House.
     */
    private void initFullHousePoints(int points)
    {
        this.fullHousePoints = new int[this.numberOfDices + 1];
        for (int i = 0; i < this.numberOfDices + 1; i++)
        {
            if (this.fullHouseActive)
            {
                this.fullHousePoints[i] = points * i;
            }
            else
            {
                this.fullHousePoints[i] = 0;
            }
        }
    }

    /**
     * Initialize the straight points array. The first straight gives "points" points, for each dice more the points are doubled.
     *
     * @param points The first active straight gives "points" points, for each dice more the points are doubled.
     */
    public void initStraightPoints(int points)
    {
        this.straightPoints = new int[this.numberOfDices + 1];
        int factor = 1;
        for (int i = 0; i < this.numberOfDices + 1; i++)
        {
            //if (this.minStraight <= i)
            {
                this.straightPoints[i] = points * factor;
                // double the factor
                factor *= 2;
            }
            //else
            {
                //this.straightPoints[i] = 0;
            }
        }
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
        return this.maxLengthOfRow;
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

    public int getStraightPoints(int length)
    {
        return this.straightPoints[length];
    }

    public int getPoints(int length, int value, PointType type)
    {
        switch (type)
        {
            case Straight:
                return this.getStraightPoints(length);
            case FullHouse:
                break;
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
    }
}
