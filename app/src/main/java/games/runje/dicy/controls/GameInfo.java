package games.runje.dicy.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicy.layouts.PointList;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 27.04.2015.
 */
public class GameInfo
{
    private final TextView limit;
    private final Button nextButton;
    private final TextView current;
    private final TextView movePointsText;
    private final Activity activity;
    private final ControlHandler handler;
    private final LocalGame game;
    private boolean enabled;

    public GameInfo(Activity activity, final ControlHandler handler, LocalGame game)
    {
        this.game = game;
        this.activity = activity;
        this.handler = handler;
        this.limit = (TextView) activity.findViewById(R.id.goal_text);
        this.limit.setText("" + game.getGameEndPoints());
        current = (TextView) activity.findViewById(R.id.currentPoints);
        current.setText("0");

        movePointsText = (TextView) activity.findViewById(R.id.switchPointsText);
        movePointsText.setText("0\\" + game.getPointsLimit());

        nextButton = (Button) activity.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                handler.nextFromUser();
            }
        });

        initPointListButton();
    }


    private void initPointListButton()
    {
        Button b = (Button) activity.findViewById(R.id.pointList);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                Rules rules = GameInfo.this.handler.getRules();

                List<PointElement> elements = new ArrayList<PointElement>();
                for (int length = 3; length < 6; length++)
                {
                    for (int value = 1; value < 7; value++)
                    {
                        elements.add(new PointElement(PointType.XOfAKind, length, value, null, Orientation.Down, rules.getPoints(length, value, PointType.XOfAKind)));
                        if ((length == 3 && value < 5) || (length == 4 && value < 4) || (length == 5 && value < 3))
                        {
                            elements.add(new PointElement(PointType.Straight, length, value, null, Orientation.Down, rules.getPoints(length, value, PointType.Straight)));
                        }

                    }


                }
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage("Points")
                        .setTitle("List").setView(new PointList(activity, elements));
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {

                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void update()
    {
        this.current.setText("" + Integer.toString(game.getMovePoints()));

        int pointsLimit = game.getPointsLimit();
        String sLimit = Integer.toString(pointsLimit);


        this.movePointsText.setText("" + Integer.toString(game.getSwitchPoints()) + "\\" + sLimit);


        if (game.getSwitchPoints() >= game.getPointsLimit())
        {
            movePointsText.setTextColor(Color.parseColor(PlayerLayout.HtmlBlue));
        }
        else
        {
            movePointsText.setTextColor(Color.RED);
        }
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        this.nextButton.setEnabled(enabled);
    }
}
