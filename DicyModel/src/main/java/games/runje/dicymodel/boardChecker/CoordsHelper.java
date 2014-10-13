package games.runje.dicymodel.boardChecker;

import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Orientation;

/**
 * Created by Thomas on 08.10.2014.
 */
public class CoordsHelper
{
    /**
     * Calculates the start Point of a Element
     *
     * @param firstPoint  in the board of the element
     * @param orientation of the element
     * @param length      of the element
     * @param endIndex    last index of the element in the line
     * @return the start point of the Element.
     */
    public static Coords calcStartPoint(Coords firstPoint, Orientation orientation, int length, int endIndex)
    {
        switch (orientation)
        {
            case Right:
                return new Coords(firstPoint.row, endIndex - length + 1);
            case Down:
                return new Coords(endIndex - length + 1, firstPoint.column);
            case DownLeft:
                return new Coords(firstPoint.row + endIndex - length + 1, firstPoint.column - (endIndex - length + 1));
            case DownRight:
                return new Coords(firstPoint.row + endIndex - length + 1, firstPoint.column + endIndex - length + 1);
            default:
                return null;
        }
    }

    /**
     * Calculates all Coords on the way if you go from the starting points "length" steps in the direction of the orientation.
     *
     * @param startPoint  starting point of the element
     * @param orientation orientation of the element
     * @param length      length of the element
     * @return All coords between the starting point and the end of this element.
     */
    public static Coords[] calcCoords(Coords startPoint, Orientation orientation, int length)
    {
        Coords[] coords = new Coords[length];

        switch (orientation)
        {
            case Right:
                for (int i = 0; i < length; i++)
                {
                    coords[i] = new Coords(startPoint.row, startPoint.column + i);
                }

                break;
            case Down:
                for (int i = 0; i < length; i++)
                {
                    coords[i] = new Coords(startPoint.row + i, startPoint.column);
                }

                break;
            case DownLeft:
                for (int i = 0; i < length; i++)
                {
                    coords[i] = new Coords(startPoint.row + i, startPoint.column - i);
                }
                break;
            case DownRight:
                for (int i = 0; i < length; i++)
                {
                    coords[i] = new Coords(startPoint.row + i, startPoint.column + i);
                }
                break;
        }

        return coords;
    }
}
