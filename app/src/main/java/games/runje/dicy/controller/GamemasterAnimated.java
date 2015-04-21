package games.runje.dicy.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.AnimationHandler;
import games.runje.dicy.animatedData.FallingAnimation;
import games.runje.dicy.animatedData.PointsAnimation;
import games.runje.dicy.animatedData.animatedSkills.AnimatedSkill;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicymodel.AbstractGamemaster;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 11.03.2015.
 */
public class GamemasterAnimated extends AbstractGamemaster
{
    private final String LogKey = "GamemasterAnimated";
    AnimatedBoard animatedBoard;
    private Activity activity;

    public GamemasterAnimated(Board board, Rules rules, Activity activity, GameControls controls, LocalGame game)
    {
        super(board, rules, controls, game);
        this.activity = activity;
        this.controls = controls;

    }

    public void init()
    {
        animatedBoard = new AnimatedBoard(board, activity, null, this);
        this.board = animatedBoard;
        controls.setGamemaster(this);
        controls.setAnimatedBoard(animatedBoard);
        // start game when point calculation are ready
        controls.setEnabledControls(false);
        controls.update();
    }


    public AnimatedBoard getAnimatedBoard()
    {
        return animatedBoard;
    }

    @Override
    protected void startSwitchAnimation(Coords first, Coords second)
    {
        Logger.logInfo(LogKey, "Start Animation Switching elements");

        //check if possible
        if (!(second.row < 0 || second.row >= board.getNumberOfRows() ||
                second.column < 0 || second.column >= board.getNumberOfColumns()))
        {
            animatedBoard.switchElements(first, second, true);
        }
        else
        {
            //endSwitchAnimation();
        }



    }

    @Override
    protected void startPointAnimation(ArrayList<PointElement> elements)
    {
        Logger.logInfo(LogKey, "Start Point animated Animation");
        new PointsAnimation(elements, null, this).start();
    }

    @Override
    protected void startDeleteAnimation(ArrayList<PointElement> elements)
    {
        animatedDelete(elements);
        endDeleteAnimation();
    }

    protected void animatedDelete(ArrayList<PointElement> elements)
    {
        board.deleteElements(elements);
        ArrayList<Coords> coords = Coords.pointElementsToCoords(elements);

        for (Coords c : coords)
        {
            animatedBoard.getAnimatedElement(c).remove();
            animatedBoard.getAnimatedElement(c).setValue(0);
        }
    }

    @Override
    protected void startFallAnimation()
    {
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                endFallAnimation();
            }
        });

        animatedBoard.moveElementsFromGravity(animationHandler);
    }

    @Override
    protected void startRecreateAnimation()
    {
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                endRecreateAnimation();
            }
        });

        animatedBoard.recreateElements(recreateElements, animationHandler);
    }


    public Activity getActivity()
    {
        return activity;
    }

    public void startGame(Board board, Rules r, LocalGame game)
    {
        super.startGame(board, rules, game);

        Logger.logInfo(LogKey, "Starting Game");
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                activity.setContentView(R.layout.game);
                View mainView = (View) activity.findViewById(R.id.board);
                mainView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // start AI
                        for (Player pl : GamemasterAnimated.this.game.getPlayers())
                        {
                            if (pl.isAi())
                            {
                                // TODO: gamemaster
                                new AIController(pl, GamemasterAnimated.this.activity, null, GamemasterAnimated.this);
                            }
                        }

                        for (Player player : GamemasterAnimated.this.game.getPlayers())
                        {
                            List<Skill> animatedSkills = new ArrayList<>();
                            for (Skill skill : player.getSkills())
                            {
                                animatedSkills.add(AnimatedSkill.create(skill));
                                Logger.logInfo(LogKey, "Animated Skill created: " + skill.getName());
                            }

                            player.setSkills(animatedSkills);
                        }

                        GamemasterAnimated.this.animatedBoard = new AnimatedBoard(GamemasterAnimated.this.board, activity, null, GamemasterAnimated.this);
                        GamemasterAnimated.this.board = animatedBoard;
                        GamemasterAnimated.this.controls = new LocalGameControls(activity, GamemasterAnimated.this.game, getAnimatedBoard(), null, GamemasterAnimated.this);
                        GamemasterAnimated.this.controls.setGamemaster(GamemasterAnimated.this);
                        new CalcPointLimit(GamemasterAnimated.this.board, rules, controls, GamemasterAnimated.this.game).execute();

                        //LocalGameActivity.this.gmAnimated = new GamemasterAnimated(bb, rules, LocalGameActivity.this, controls, game);


                        LinearLayout boardContainer = (LinearLayout) activity.findViewById(R.id.board);
                        boardContainer.addView(getAnimatedBoard().getBoardLayout(), ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                });

            }
        });


    }


    public void switchElementsFromUser(Coords first, Coords second)
    {
        switchElements(first, second);
    }

    public void nextFromUser()
    {
        next();
    }

    public void changeGravityFromUser(Gravity gravity)
    {
        changeGravity(gravity);
    }

    public void changeGravity(Gravity gravity)
    {
        if (board.getGravity() == gravity)
        {
            return;
        }

        getAnimatedBoard().getBoardLayout().updateGravity(gravity);
        board.setGravity(gravity);
        controls.setEnabledControls(false);
        AnimationHandler animationHandler = new AnimationHandler(new Runnable()
        {
            @Override
            public void run()
            {
                getControls().setEnabledControls(true);
            }
        });

        for (int i = 0; i < board.getNumberOfRows(); i++)
        {
            for (int j = 0; j < board.getNumberOfColumns(); j++)
            {
                Coords pos = new Coords(i, j);
                AnimatedBoardElement aE = animatedBoard.getAnimatedElement(pos);

                animationHandler.addAnimation(new FallingAnimation(aE, pos, animatedBoard, animationHandler));
            }
        }

        animationHandler.start();
    }
}
