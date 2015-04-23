package games.runje.dicy.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicy.layouts.PointList;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 08.01.2015.
 */
public class LocalGameControls extends Controls
{

    private RelativeLayout points;
    private TextView pointLimit;
    private Button next;
    private TextView limit;
    private TextView movePoints;
    private TextView currentPoints;
    private String LogKey = "LocalGameControls";
    private boolean started = false;
    private Button pointList;

    public LocalGameControls(Activity context, LocalGame g, AnimatedBoard board, GamemasterAnimated gmAnimated)
    {
        super(context, board, gmAnimated);
        this.game = g;
        Next();
        pointList();
    }

    public Button getPointList()
    {
        return pointList;
    }

    public Button getNext()
    {
        return next;
    }

    public TextView getPointLimit()
    {
        return pointLimit;
    }

    public RelativeLayout getPoints()
    {
        return points;
    }

    @Override
    public void setGamemaster(AbstractGamemaster gamemaster)
    {
        // TODO:
        this.gmAnimated = (GamemasterAnimated) gamemaster;
        points();
    }



    private View pointLimit()
    {
        TextView limit = (TextView) activity.findViewById(R.id.goal_text);
        limit.setText("Limit: " + game.getPointsLimit());
        this.pointLimit = limit;
        return limit;
    }


    public View points()
    {

        RelativeLayout l = new RelativeLayout(getContext());
        //l.addView(playerPoints());
        playerPoints();
        TextView current = (TextView) activity.findViewById(R.id.currentPoints);
        current.setText("0");
        current.setId(R.id.currentPointsText);

        TextView movePointsText = (TextView) activity.findViewById(R.id.switchPointsText);
        movePointsText.setText("0\\" + game.getPointsLimit());
        movePointsText.setId(R.id.switchPointsText);

        ImageView switchImage = new ImageView(getContext());
        switchImage.setImageResource(R.drawable.dice3d);
        switchImage.setId(View.generateViewId());

        ImageView currentImage = new ImageView(getContext());
        currentImage.setImageResource(R.drawable.dicychip);
        currentImage.setId(View.generateViewId());

        ImageView raceflag = new ImageView(getContext());
        raceflag.setImageResource(R.drawable.raceflag2);
        raceflag.setId(View.generateViewId());

        TextView goal = new TextView(getContext());
        goal.setText("??");
        goal.setTextColor(Color.parseColor(PlayerLayout.HtmlGreen));
        goal.setTextSize(15);
        goal.setId(R.id.gameEndpoints);


        RelativeLayout.LayoutParams pC = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pC.addRule(RelativeLayout.BELOW, R.id.playersPoints);
        pC.addRule(RelativeLayout.RIGHT_OF, currentImage.getId());
        pC.leftMargin = 10;

        RelativeLayout.LayoutParams pM = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pM.addRule(RelativeLayout.BELOW, R.id.currentPointsText);
        pM.addRule(RelativeLayout.RIGHT_OF, switchImage.getId());
        pM.leftMargin = 10;
        pM.topMargin = 20;

        RelativeLayout.LayoutParams pG = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pG.addRule(RelativeLayout.BELOW, R.id.switchPointsText);
        pG.addRule(RelativeLayout.RIGHT_OF, raceflag.getId());
        pG.leftMargin = 10;
        pG.topMargin = 20;

        RelativeLayout.LayoutParams pI = new RelativeLayout.LayoutParams(50, 50);
        pI.addRule(RelativeLayout.BELOW, R.id.switchPointsText);
        pI.topMargin = 20;

        RelativeLayout.LayoutParams pS = new RelativeLayout.LayoutParams(50, 50);
        pS.addRule(RelativeLayout.BELOW, R.id.currentPointsText);
        pS.topMargin = 20;

        RelativeLayout.LayoutParams pD = new RelativeLayout.LayoutParams(50, 50);
        //pD.topMargin = 20;
        //pD.addRule(RelativeLayout.BELOW, R.id.currentPointsText);


        this.points = l;
        this.currentPoints = current;
        this.movePoints = movePointsText;
        this.limit = (TextView) activity.findViewById(R.id.goal_text);
        return l;
    }

    public View Next()
    {
        Button b = (Button) activity.findViewById(R.id.next_button);
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gmAnimated.nextFromUser();
            }
        });

        this.next = b;
        return b;
    }

    public View pointList()
    {

        Button b = new Button(getContext());
        b.setId(R.id.pointList);
        b.setText("PointList");
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                Rules rules = gmAnimated.getRules();

                List<PointElement> elements = new ArrayList<PointElement>();
                for (int i = 3; i < 6; i++)
                {
                    for (int j = 1; j < 7; j++)
                    {
                        elements.add(new PointElement(PointType.XOfAKind, i, j, null, Orientation.Down, rules.getPoints(i, j, PointType.XOfAKind)));
                    }

                    elements.add(new PointElement(PointType.Straight, i, 1, null, Orientation.Down, rules.getPoints(i, 1, PointType.Straight)));

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
        this.pointList = b;
        return b;
    }


    public void update()
    {
        updatePoints();
        updateGravity();
        updateSkills();
        gameOver();
        ViewGroup vg = (ViewGroup) activity.getWindow().getDecorView();
        vg.invalidate();
    }

    private void updateSkills()
    {
        AnimatedLogger.logDebug(LogKey, "Updating Skills");
        for (PlayerLayout l : playerLayouts)
        {
            l.updateSkills();
        }
    }

    private void gameOver()
    {
        if (game.isGameOver())
        {
            disable();
            FinishedDialog d = new FinishedDialog();
            d.setContext(activity);
            d.setName(game.getWinner());
            AnimatedLogger.logInfo(LogKey, "Before show");
            d.show(activity.getFragmentManager(), "Game is finished");
            AnimatedLogger.logInfo(LogKey, "After show");
        }
    }

    public void updatePoints()
    {
        updatePlayers();

        this.currentPoints.setText("" + Integer.toString(game.getMovePoints()));

        int pointsLimit = game.getPointsLimit();
        Rules rules = gmAnimated.getRules();
        String sLimit = Integer.toString(pointsLimit);
        if (!started)
        {
            if (!rules.isPointLimitSetManually())
            {
                sLimit = "??";
            }
            else
            {
                this.limit.setText("" + game.getGameEndPoints());
                Logger.logInfo(LogKey, "Goal: " + game.getGameEndPoints());
                started = true;
                setEnabledControls(true);
            }
        }

        this.movePoints.setText("" + Integer.toString(game.getSwitchPoints()) + "\\" + sLimit);


        if (game.getSwitchPoints() > game.getPointsLimit())
        {
            movePoints.setTextColor(Color.parseColor(PlayerLayout.HtmlBlue));
        }
        else
        {
            movePoints.setTextColor(Color.RED);
        }
    }


    public void enable()
    {
        if (game.isGameOver())
        {
            return;
        }

        if (game.hasAIPlayerTurn())
        {
            AnimatedLogger.logInfo(LogKey, "Disabling Controls because AI Player has turn");
            disable();
            return;
        }

        AnimatedBoard b = gmAnimated.getAnimatedBoard();

        b.enable();
        enableNext();
        enableGravity();
        enableSkills();
    }

    private void enableSkills()
    {
        // TODO
        for (PlayerLayout l : this.playerLayouts)
        {
            if (game.hasTurn(l.getPlayer()))
            {
                l.setEnabled(true);
            }
            else
            {
                l.setEnabled(false);
            }
        }
    }

    public void disable()
    {
        AnimatedBoard b = gmAnimated.getAnimatedBoard();
        b.disable();
        disableNext();
        disableGravity();
        disableSkills();
    }

    private void disableSkills()
    {
        for (PlayerLayout l : this.playerLayouts)
        {
            l.setEnabled(false);
        }
    }

    private void disableNext()
    {
        this.next.setEnabled(false);
    }

    private void enableNext()
    {
        this.next.setEnabled(true);
    }

}
