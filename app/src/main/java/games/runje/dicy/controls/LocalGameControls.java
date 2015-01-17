package games.runje.dicy.controls;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.controller.Gamemaster;
import games.runje.dicy.game.LocalGame;

/**
 * Created by Thomas on 08.01.2015.
 */
public class LocalGameControls extends Controls
{

    public LocalGameControls(Context context, LocalGame g)
    {
        super(context);
        this.game = g;
        addView(points());
        RelativeLayout.LayoutParams pA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pA.addRule(RelativeLayout.BELOW, R.id.next);
        addView(gravityArrows(), pA);

        RelativeLayout.LayoutParams pN = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pN.addRule(RelativeLayout.BELOW, R.id.points);
        addView(Next(), pN);
    }


    public View points()
    {

        RelativeLayout l = new RelativeLayout(getContext());
        l.addView(playerPoints());
        TextView current = new TextView(getContext());
        current.setText("Current Points: 0");
        current.setId(R.id.currentPointsText);

        TextView movePointsText = new TextView(getContext());
        movePointsText.setText("Switch Points: 0");
        movePointsText.setId(R.id.switchPointsText);


        RelativeLayout.LayoutParams pC = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pC.addRule(RelativeLayout.BELOW, R.id.playersPoints);

        RelativeLayout.LayoutParams pM = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pM.addRule(RelativeLayout.BELOW, R.id.currentPointsText);
        //l.addView(pointsText2, pA);
        l.addView(movePointsText, pM);
        l.addView(current, pC);
        l.setId(R.id.points);
        return l;
    }

    public View Next()
    {
        Button b = new Button(getContext());
        b.setId(R.id.next);
        b.setText("Next");
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Gamemaster.getInstance().next();
            }
        });

        b.setId(R.id.next);
        return b;
    }

    public void update()
    {
        updatePoints();
        updateGravity();
    }

    public void updatePoints()
    {
        updatePlayerPoints();


        TextView c = (TextView) findViewById(R.id.currentPointsText);
        c.setText("Current Points: " + Integer.toString(game.getMovePoints()));

        TextView m = (TextView) findViewById(R.id.switchPointsText);
        m.setText("Switch Points: " + Integer.toString(game.getSwitchPoints()));

    }


    public void enable()
    {
        enableNext();
        enableGravity();
    }

    public void disable()
    {
        disableNext();
        disableGravity();
    }

    private void disableNext()
    {
        Button c = (Button) findViewById(R.id.next);
        c.setEnabled(false);
    }

    private void enableNext()
    {
        Button c = (Button) findViewById(R.id.next);
        c.setEnabled(true);
    }
}
