package games.runje.dicy.controller;

import android.app.Activity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.communication.ConnectionToServer;
import games.runje.dicymodel.communication.Message;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 16.02.2015.
 */
public class OnlineGamemaster extends AnimatedGamemaster
{
    private static OnlineGamemaster instance = new OnlineGamemaster();
    private String LogKey = "OnlineGamemaster";
    private boolean messageInProcess = false;

    private OnlineGamemaster()
    {
        super();
    }

    private OnlineGamemaster(LocalGame game, Board b, Rules r, Activity a)
    {
        super(game, b, r, a);
    }

    public static OnlineGamemaster getInstance()
    {
        return instance;
    }

    public void startGame(Board board, Rules rules, String[] player)
    {
        System.out.println("Starting Game from OnlineGamemaster");
        this.board = board;
    }

    public void createOnlineGame(Activity activity)
    {
        assert getInstance().board != null;
        this.activity = activity;
        AnimatedBoard b = new AnimatedBoard(getInstance().board, activity, this);
        b.setGravity(Gravity.Down);
        Rules rules = new Rules();
        rules.setPointLimit(Simulator.getLimit(rules, b));
        int length = 5;
        List<String> players = new ArrayList<>();
        players.add("Thomas");
        players.add("Milena");

        List<Strategy> s = new ArrayList<>();
        s.add(null);
        s.add(null);
        LocalGame game = new LocalGame(rules.getPointLimit(), rules.getPointLimit() * length, players, s);

        Controls controls = new LocalGameControls(activity, game, b, this);
        setGame(game);
        setBoard(b);
        setRules(rules);
        setControls(controls);

        update();
    }

    public void sendMessageToClient(Message message)
    {
        ConnectionToServer.sendMessage(message);
    }

    public void updateAfterFall()
    {
        if (isAnimationIsRunning())
        {
            return;
        }

        locked = true;
        AnimatedBoard b = ((AnimatedBoard) board);

        locked = false;

        if (!b.hasNoElements())
        {
            updateAfterSwitch();
        }

    }

    public void updateAfterFall(ArrayList<BoardElement> elements)
    {
        if (isAnimationIsRunning())
        {
            Logger.logInfo(LogKey, "updateAfterFall although animation is running");
            return;
        }

        locked = true;

        this.animationEnded = 0;
        // check board if there have to be created new elements
        board.recreateElements(elements);
        this.animationsWillStart = elements.size();
        locked = false;
        if (elements.size() == 0)
        {
                Logger.logInfo(LogKey, "Move ends");
                // end move
                game.endSwitch();

                controls.update();
                // check if moves are possible
                ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

                controls.enable();
                game.setStrikePossible(true);

        }
    }

    public void updateAfterSwitch()
    {
        if (isAnimationIsRunning())
        {
            return;
        }

        locked = true;

        this.animationEnded = 0;
        this.animationsWillStart = 0;

        ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);

            game.addPointElements(elements, board);
            board.deleteElements(elements);

        controls.update();


        locked = false;
    }

    public void updaterAfterPoints()
    {
        if (isAnimationIsRunning())
        {
            return;
        }

        locked = true;

        this.animationEnded = 0;
        ArrayList<BoardElement> fallingElements = board.moveElementsFromGravity();
        this.animationsWillStart = fallingElements.size();
        if (fallingElements.size() == 0)
        {
            //ArrayList<BoardElement> rElements = board.recreateElements();
            //this.animationsWillStart = rElements.size();
            /*if (rElements.size() == 0)
            {
                // end move
                game.endSwitch();

                controls.update();
                // check if moves are possible
                ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

                if (moves.size() == 0)
                {
                    // recreate board
                    Logger.logInfo(LogKey, "No more moves possible");
                    Activity a = ((AnimatedBoard) board).getActivity();
                    this.board = new AnimatedBoard(board.getNumberOfRows(), board.getNumberOfColumns(), a, this);
                    AnimatedBoard board = (AnimatedBoard) getBoard();
                    RelativeLayout b = board.getGameLayout();
                    b.setId(R.id.board);
                    RelativeLayout l = new RelativeLayout(a);
                    l.addView(b);

                    ((RelativeLayout) controls.getParent()).removeView(controls);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.board);
                    params.topMargin = 50;
                    l.addView(controls, params);
                    a.setContentView(l);
                    this.controls.updatePoints();
                }

                controls.enable();
                game.setStrikePossible(true);
            }*/
        }

        locked = false;
    }

    public void receiveMessage(final ByteBuffer buffer, final int length)
    {
        Logger.logInfo(LogKey, "receiveMessage");
        if (activity == null)
        {
            super.receiveMessage(buffer, length);
        }
        else
        {
            while (isAnimationIsRunning() || messageInProcess)
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            messageInProcess = true;
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    // TODO: enque message and handle them better
                    OnlineGamemaster.super.receiveMessage(buffer, length);
                    messageInProcess = false;
                }
            });
        }
    }
}
