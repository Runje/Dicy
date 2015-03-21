package games.runje.dicy.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicy.controller.SkillAction;
import games.runje.dicy.layouts.PointList;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.communication.messages.SkillMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Orientation;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.data.PointType;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 08.01.2015.
 */
public class LocalGameControls extends Controls
{

    public LocalGameControls(Activity context, LocalGame g, AnimatedBoard board, AnimatedGamemaster gm, GamemasterAnimated gmAnimated)
    {
        super(context, board, gm, gmAnimated);
        this.game = g;
        addView(points());


        RelativeLayout.LayoutParams pL = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pL.addRule(RelativeLayout.BELOW, R.id.points);
        addView(pointLimit(), pL);

        RelativeLayout.LayoutParams pN = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pN.addRule(RelativeLayout.BELOW, R.id.pointLimit);
        addView(Next(), pN);

        RelativeLayout.LayoutParams pH = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pH.addRule(RelativeLayout.BELOW, R.id.next);
        addView(Help(), pH);

        RelativeLayout.LayoutParams pC = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pC.addRule(RelativeLayout.BELOW, R.id.helpButton);
        addView(Change(), pC);

        RelativeLayout.LayoutParams pP = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pP.addRule(RelativeLayout.RIGHT_OF, R.id.helpButton);
        pP.addRule(RelativeLayout.ALIGN_BASELINE, R.id.helpButton);
        addView(pointList(), pP);
    }

    @Override
    public void setAnimatedBoard(Board board)
    {
        super.setAnimatedBoard(board);
        RelativeLayout.LayoutParams pA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pA.addRule(RelativeLayout.RIGHT_OF, R.id.points);
        addView(gravityArrows(), pA);
    }

    private View Help()
    {
        Button b = new Button(getContext());
        b.setText("Help");
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (gamemaster != null)
                {
                    gamemaster.performAction(new SkillAction(game.getPlayingPlayer().getSkill(Skill.Help), gamemaster));
                    gamemaster.sendMessageToServer(new SkillMessage(Skill.Help));
                }
                else
                {
                    // TODO
                }
            }
        });
        b.setId(R.id.helpButton);
        return b;
    }

    private View Change()
    {
        Button b = new Button(getContext());
        b.setText(Skill.Change);
        b.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (gamemaster != null)
                {
                    gamemaster.performAction(new SkillAction(game.getPlayingPlayer().getSkill(Skill.Change), gamemaster));
                }
                else
                {
                    // TODO
                }
            }
        });
        b.setId(R.id.changeButton);
        return b;
    }

    private View pointLimit()
    {
        TextView limit = new TextView(getContext());
        limit.setText("Limit: " + game.getPointsLimit());
        limit.setId(R.id.pointLimit);
        return limit;
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

        TextView goal = new TextView(getContext());
        goal.setText("Goal: " + game.getGameEndPoints());


        RelativeLayout.LayoutParams pC = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pC.addRule(RelativeLayout.BELOW, R.id.playersPoints);

        RelativeLayout.LayoutParams pM = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pM.addRule(RelativeLayout.BELOW, R.id.currentPointsText);

        RelativeLayout.LayoutParams pG = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pG.addRule(RelativeLayout.BELOW, R.id.switchPointsText);


        l.addView(movePointsText, pM);
        l.addView(current, pC);
        l.addView(goal, pG);
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
                if (gamemaster != null)
                {
                    gamemaster.next();
                }
                else
                {
                    gmAnimated.next();
                }
            }
        });

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

                Rules rules = null;
                if (gamemaster != null)
                {
                    rules = gamemaster.getRules();
                }
                else
                {
                    rules = gmAnimated.getRules();
                }

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
        Button c = (Button) findViewById(R.id.helpButton);
        Skill skill = game.getPlayingPlayer().getSkill(Skill.Help);
        int max = skill.getMaxLoad();
        int v = skill.getCurrentLoad();
        c.setText(Skill.Help + " " + v + "/" + max);

        Button cB = (Button) findViewById(R.id.changeButton);
        skill = game.getPlayingPlayer().getSkill(Skill.Change);
        max = skill.getMaxLoad();
        v = skill.getCurrentLoad();
        cB.setText(Skill.Change + " " + v + "/" + max);
    }

    private void gameOver()
    {
        if (game.isGameOver())
        {
            disable();
            FinishedDialog d = new FinishedDialog();
            d.setName(game.getWinner());
            d.show(activity.getFragmentManager(), "Game is finished");
        }
    }

    public void updatePoints()
    {
        updatePlayers();


        TextView c = (TextView) findViewById(R.id.currentPointsText);
        c.setText("Current Points: " + Integer.toString(game.getMovePoints()));

        TextView m = (TextView) findViewById(R.id.switchPointsText);
        m.setText("Switch Points: " + Integer.toString(game.getSwitchPoints()));

    }


    public void enable()
    {
        if (game.isGameOver())
        {
            return;
        }

        if (game.hasAIPlayerTurn())
        {
            disable();
            return;
        }

        AnimatedBoard b = null;
        if (gamemaster != null)
        {
            b = (AnimatedBoard) gamemaster.getBoard();
        }
        else
        {
            b = gmAnimated.getAnimatedBoard();
        }

        b.enableSwitchListener();
        enableNext();
        enableGravity();
        enableHelp();
        enableChange();
    }

    private void enableChange()
    {
        Button c = (Button) findViewById(R.id.changeButton);
        c.setEnabled(true);
    }

    private void disableChange()
    {
        Button c = (Button) findViewById(R.id.changeButton);
        c.setEnabled(false);
    }

    private void enableHelp()
    {
        Button c = (Button) findViewById(R.id.helpButton);
        c.setEnabled(true);
    }

    private void disableHelp()
    {
        Button c = (Button) findViewById(R.id.helpButton);
        c.setEnabled(false);
    }

    public void disable()
    {
        AnimatedBoard b = null;
        if (gamemaster != null)
        {
            b = (AnimatedBoard) gamemaster.getBoard();
        }
        else
        {
            b = gmAnimated.getAnimatedBoard();
        }
        b.disableSwitchListener();
        disableNext();
        disableGravity();
        disableHelp();
        disableChange();
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
