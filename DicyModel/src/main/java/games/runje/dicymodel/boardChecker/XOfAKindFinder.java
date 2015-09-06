package games.runje.dicymodel.boardChecker;

import java.util.ArrayList;
import java.util.Collection;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;

/**
 * Created by Thomas on 08.10.2014.
 */
public class XOfAKindFinder extends Finder
{
    public XOfAKindFinder(Board board, Rules rules)
    {
        super(board, rules);
    }

    /**
     * Looks for PointElements in a line.
     *
     * @param line        to look in
     * @param orientation of the line
     * @param firstPoint  first Point of the line in the board
     * @return PointElements in this line.
     */
    protected Collection<? extends PointElement> getPointElementsFromLine(ArrayList<BoardElement> line, Orientation orientation, Coords firstPoint)
    {
        ArrayList<PointElement> elements = new ArrayList<PointElement>();
        if (line.size() < rules.getMinXOfAKind())
        {
            return elements;
        }

        int value = 0;
        int x = 1;
        for (int i = 0; i < line.size(); i++)
        {
            int newValue = line.get(i).getValue();
            if (newValue != -1 && newValue == value)
            {
                x++;

                // if it is the last one
                if (i == line.size() - 1)
                {
                    if (x >= rules.getMinXOfAKind())
                    {
                        elements.add(this.createPointElement(x, i, orientation, value, firstPoint, PointType.XOfAKind));
                    }
                }
            }
            else
            {
                if (x >= rules.getMinXOfAKind())
                {
                    elements.add(this.createPointElement(x, i - 1, orientation, value, firstPoint, PointType.XOfAKind));
                }

                value = newValue;
                x = 1;
            }
        }

        return elements;
    }

}
