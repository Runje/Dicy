package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;

/**
 * Created by Thomas on 05.03.2015.
 */
public class PointList extends ScrollView
{
    public PointList(Context context, List<PointElement> elements)
    {
        super(context);
        TableLayout l = new TableLayout(context);
        for (PointElement element : elements)
        {
            int points = element.getPoints();
            int value = element.getDiceValue();
            int size = 50;
            int maxLength = 5;
            View dices = null;
            switch (element.getType())
            {
                case Straight:
                    dices = new StraightLayout(context, element.getMultiplicity(), size, value);
                    break;
                case FullHouse:
                    break;
                case XOfAKind:
                    dices = new XOfAKindLayout(context, element.getMultiplicity(), size, maxLength, value);
                    break;
            }

            TextView pointText = new TextView(context);
            pointText.setText(Integer.toString(points));
            pointText.setTextSize(20f);
            pointText.setTextColor(Color.RED);
            TableLayout.LayoutParams p = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            p.leftMargin = 100;

            TableRow row = new TableRow(context);
            row.addView(dices);
            row.addView(pointText);
            l.addView(row);

        }

        addView(l);
    }

    public static PointList createPointList(Context context, Rules rules)
    {
        List<PointElement> elements = new ArrayList<PointElement>();
        for (int length = rules.getMinXOfAKind(); length < 6; length++)
        {
            for (int value = 1; value < 7; value++)
            {
                elements.add(new PointElement(PointType.XOfAKind, length, value, null, Orientation.Down, rules.getPoints(length, value, PointType.XOfAKind)));
            }


        }

        for (int length = rules.getMinStraight(); length < 6; length++)
        {
            for (int value = 1; value < 7; value++)
            {
                if ((length == 3 && value < 5) || (length == 4 && value < 4) || (length == 5 && value < 3))
                {
                    elements.add(new PointElement(PointType.Straight, length, value, null, Orientation.Down, rules.getPoints(length, value, PointType.Straight)));
                }

            }


        }

        return new PointList(context, elements);
    }
}
