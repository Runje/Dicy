package games.runje.dicy.controls;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controller.SkillAction;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 08.01.2015.
 */
public class LocalGameControls extends Controls
{

    public LocalGameControls(Activity context, LocalGame g, AnimatedBoard board, AnimatedGamemaster gm)
    {
        super(context, board, gm);
        this.game = g;
        addView(points());
        RelativeLayout.LayoutParams pA = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        pA.addRule(RelativeLayout.RIGHT_OF, R.id.points);
        addView(gravityArrows(), pA);

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
                gamemaster.performAction(new SkillAction(game.getPlayingPlayer().getSkill(Skill.Help), gamemaster));
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
                gamemaster.performAction(new SkillAction(game.getPlayingPlayer().getSkill(Skill.Change), gamemaster));
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
                gamemaster.next();
            }
        });

        b.setId(R.id.next);
        return b;
    }

    public void update()
    {
        updatePoints();
        updateGravity();
        updateSkills();
        gameOver();
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
