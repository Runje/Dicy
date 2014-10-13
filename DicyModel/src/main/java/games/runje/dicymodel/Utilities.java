package games.runje.dicymodel;

/**
 * Created by Thomas on 05.10.2014.
 */
public class Utilities
{
    /**
     * Checks if a number is a square
     *
     * @param n number to check.
     * @return Whether n is a square
     */
    public static boolean isPerfectSquare(long n)
    {
        if (n < 0)
            return false;

        long tst = (long) (Math.sqrt(n) + 0.5);
        return tst * tst == n;
    }
}
