package games.runje.dicy.tutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.StartActivity;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.GameInfo;
import games.runje.dicy.layouts.DicyProgress;
import games.runje.dicy.layouts.ProgressBlinkAnimation;
import games.runje.dicy.util.ActivityUtilities;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 04.04.2015.
 */
public class SwitchTutorialGamemaster extends AnimatedGamemaster
{
    public static String LogKey = "SwitchTutorialGamemaster";
    private BoardSelector boardSelector;
    private TextView lastText;
    private int tutorialStep = 0;
    private int skillsExecuted = 0;
    public SwitchTutorialGamemaster(LocalGame game, Rules rules, final Activity activity, final Board board)
    {
        super(game, rules, activity, board);
        getRules().setMinStraight(3);
        getRules().setPointLimit(0);
        getRules().setGameEndPoints(1000);
        DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        progress.setMaxProgress(0);
        progress.postInvalidate();

        showTextOverPlayers(R.string.start_tutorial);
        boardSelector = new BoardSelector(getAnimatedBoard(), (ViewGroup) activity.findViewById(R.id.board));
        highlightMove();
    }

    @Override
    public void nextFromUser()
    {
        clearLastText();
        super.nextFromUser();
    }

    private void highlightMove()
    {
        Move move = Strategy.getBestSwitchMove(BoardChecker.getPossiblePointMoves(board, getRules()));
        ((AnimatedBoard) board).highlightElements(move);
    }

    private void hidePlayers()
    {
        View v = activity.findViewById(R.id.players);

    }

    @Override
    public void executeSkillFromUser(Skill s)
    {
        clearLastText();
        super.executeSkillFromUser(s);

        if (game.getPlayingPlayer().isHuman())
        {
            skillsExecuted++;
            if (skillsExecuted == 3)
            {
                tutorialStep++;
            }
        }
    }

    private void showTextOverPlayers(int stringId)
    {
        showText(stringId, R.id.players_container);

    }

    private void showTextOverOtherPlayer(int stringId)
    {
        showText(stringId, R.id.player2_container);
    }

    private void showTextOverBoard(int stringId)
    {
        showText(stringId, R.id.board);
    }


    @Override
    protected void transitionToNormal()
    {
        super.transitionToNormal();

        clearLastText();
        clearAnimations();
        showAll();
        if (game.getPlayingPlayer().isHuman())
        {
            showNextTutorialStep();
        }

    }

    private void clearAnimations()
    {
        activity.findViewById(R.id.image_next).clearAnimation();
        activity.findViewById(R.id.player1).findViewById(R.id.player_points).clearAnimation();
        DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        progress.clearAnimation();
        progress.setPointLimitAlpha(255);
        activity.findViewById(R.id.skill1).clearAnimation();
        activity.findViewById(R.id.skill3).clearAnimation();
        activity.findViewById(R.id.skill2).clearAnimation();
        activity.findViewById(R.id.player_progress).clearAnimation();
    }

    private void clearLastText()
    {
        if (lastText != null)
        {
            ActivityUtilities.removeView(lastText);
        }
    }

    @Override
    public void gameOver()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("The Winner is " + game.getWinner())
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent = new Intent(((Dialog) dialog).getContext(), StartActivity.class);
                        activity.startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void showNextTutorialStep()
    {
        switch (tutorialStep)
        {
            case 0:
                // Points
                showTextOverBoard(R.string.step1_tutorial);
                hideBoard();
                highlightPointList();
                disableNext();
                tutorialStep++;
                break;

            case 1:
                // Gravity
                showTextOverPlayers(R.string.step2_tutorial);
                boardSelector.highlightBorders();
                disableNext();
                tutorialStep++;
                // TODO: disable skills
                break;

            case 2:
                // PointLimit
                DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
                int newLimit = 500;
                progress.setMaxProgress(newLimit);
                progress.postInvalidate();
                rules.setPointLimit(newLimit);
                showTextOverOtherPlayer(R.string.step3_tutorial);
                highlightNext();
                highlightBracketPoints();
                highlightPointLimit();
                // TODO: disable skills
                tutorialStep++;
                break;

            case 3:
                // skills
                if (skillsExecuted == 0)
                {
                    DicyProgress progress2 = (DicyProgress) activity.findViewById(R.id.dicy_progress);
                    int newLimit2 = 50;
                    progress2.setMaxProgress(newLimit2);
                    progress2.postInvalidate();
                    rules.setPointLimit(newLimit2);
                    for (Skill skill : game.getPlayers().get(0).getSkills())
                    {
                        skill.setCurrentLoad(skill.getMaxLoad());
                    }

                    showTextOverBoard(R.string.step4_tutorial);
                }
                highlightSkills();
                break;

            case 4:
                // sudden death
                showTextOverBoard(R.string.step5_tutorial);
                highlightPlayerPoints();
                tutorialStep++;
                break;
        }


    }

    private void highlightPlayerPoints()
    {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        activity.findViewById(R.id.player_progress).startAnimation(animation);
    }

    private void highlightSkills()
    {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        activity.findViewById(R.id.skill1).startAnimation(animation);
        activity.findViewById(R.id.skill3).startAnimation(animation);
        activity.findViewById(R.id.skill2).startAnimation(animation);
    }

    private void disableNext()
    {
        controls.setEnabledNext(false);
    }

    private void highlightPointLimit()
    {
        final DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        Animation animation = new ProgressBlinkAnimation(progress);

        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(600);
        progress.startAnimation(animation);

    }

    private void highlightBracketPoints()
    {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        activity.findViewById(R.id.player1).findViewById(R.id.player_points).startAnimation(animation);
    }

    private void highlightNext()
    {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        activity.findViewById(R.id.image_next).startAnimation(animation);
    }


    private void highlightPointList()
    {
        ViewGroup container = (ViewGroup) activity.findViewById(R.id.game_info);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
        final View Pointlist = container.findViewById(R.id.pointList);
        Pointlist.startAnimation(animation);

        Pointlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                GameInfo.createPointListDialog(activity, rules, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showAll();
                        clearLastText();
                        Pointlist.clearAnimation();
                        ((Controls) getControls()).reinitPointList();
                        showNextTutorialStep();
                    }
                }).show();

            }
        });


    }

    private void hideBoard()
    {
        boardSelector.hideAll();
    }

    private void showAll()
    {
        boardSelector.clearAll();
    }

    private void showText(int stringId, int containerId)
    {
        lastText = new TextView(activity);
        lastText.setText(stringId);
        lastText.setClickable(true);
        lastText.setBackgroundColor(Color.BLACK);
        lastText.setTextColor(Color.WHITE);
        ViewGroup container = (ViewGroup) activity.findViewById(containerId);
        container.addView(lastText, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        lastText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                clearLastText();
            }
        });
    }


}
