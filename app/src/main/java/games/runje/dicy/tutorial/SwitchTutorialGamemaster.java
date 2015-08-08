package games.runje.dicy.tutorial;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controller.AnimatedGamemaster;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.GameInfo;
import games.runje.dicy.layouts.DicyProgress;
import games.runje.dicy.util.ViewUtilities;
import games.runje.dicymodel.Logger;
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
    private int tutorialStep = 3;

    public SwitchTutorialGamemaster(LocalGame game, Rules rules, final Activity activity, final Board board)
    {
        super(game, rules, activity, board);
        getRules().setMinStraight(3);
        getRules().setPointLimit(0);
        getRules().setGameEndPoints(1000);
        game.setPointsLimit(0);
        game.setGameEndPoints(1000);
        DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        progress.setMaxProgress(0);
        progress.postInvalidate();

        showTextOverPlayers(R.string.start_tutorial);
        boardSelector = new BoardSelector(getAnimatedBoard(), (ViewGroup) activity.findViewById(R.id.board));
        highlightMove();
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
    }

    private void clearLastText()
    {
        if (lastText != null)
        {
            ViewUtilities.removeView(lastText);
        }
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
                game.setPointsLimit(newLimit);
                showTextOverOtherPlayer(R.string.step3_tutorial);
                highlightNext();
                highlightBracketPoints();
                highlightPointLimit();
                disableNext();
                // TODO: disable skills
                tutorialStep++;
                break;

            case 3:
                // skills
                DicyProgress progress2 = (DicyProgress) activity.findViewById(R.id.dicy_progress);
                int newLimit2 = 50;
                progress2.setMaxProgress(newLimit2);
                progress2.postInvalidate();
                rules.setPointLimit(newLimit2);
                game.setPointsLimit(newLimit2);
                for (Skill skill : game.getPlayers().get(0).getSkills())
                {
                    skill.setCurrentLoad(skill.getMaxLoad());
                }

                showTextOverBoard(R.string.step4_tutorial);
                highlightSkills();
                tutorialStep++;
                break;

            case 4:
                // sudden death
                break;
        }


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
        activity.findViewById(R.id.image_next).setEnabled(false);
    }

    private void highlightPointLimit()
    {
        final DicyProgress progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t)
            {
                int newAlpha = (int) (interpolatedTime * 255);
                progress.setPointLimitAlpha(newAlpha);
                progress.postInvalidate();
            }
        };

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

    public void startGame()
    {
        Logger.logInfo(LogKey, "Starting Game");
        //RelativeLayout root = (RelativeLayout) getActivity().getWindow().getDecorView().getRootView();
        /*GameLayout root = (GameLayout) getActivity().findViewById(R.id.points);
        Context context = root.getContext();
        ImageView image = new ImageView(context);
        image.setClickable(true);
        image.setBackgroundColor(Color.WHITE);
        image.setAlpha(0.7f);/*
        root.addView(image, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);*/


        //new Hider(root.getBoard());
        //root.setForeground(getActivity().getResources().getDrawable(R.drawable.green_square));
    }
}
