package games.runje.dicy.controller;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoard;
import games.runje.dicy.controls.Controls;
import games.runje.dicy.controls.LocalGameControls;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.communication.ConnectionToServer;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.NextMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
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

    public OnlineGamemaster(Activity activity, long id)
    {
        this.activity = activity;
        this.fromId = id;
    }

    public static OnlineGamemaster getInstance()
    {
        return instance;
    }

    public void startGame(Board board, Rules rules, LocalGame game)
    {
        System.out.println("Starting Game from OnlineGamemaster");
        this.board = board;
        this.rules = rules;
        createOnlineGame(game);
    }

    public void next(long id)
    {
        super.next();
        controls.update();
    }

    public void changeGravity(Gravity gravity, long fromId)
    {
        super.changeGravity(gravity, fromId);
        updateGravity(gravity);
        controls.update();
    }

    public void createOnlineGame(LocalGame game)
    {
        this.game = game;
        this.rules = new Rules();
        AnimatedBoard b = new AnimatedBoard(board, activity, this);
        b.setGravity(Gravity.Down);
        Controls controls = new LocalGameControls(activity, game, b, this);
        setBoard(b);
        setControls(controls);

        update();

        RelativeLayout l = new RelativeLayout(this.activity);
        // TODO: create local game here
        AnimatedBoard animatedBoard = (AnimatedBoard) this.board;
        animatedBoard.setGravity(Gravity.Down);
        RelativeLayout bL = animatedBoard.getGameLayout();
        RelativeLayout.LayoutParams pB = (RelativeLayout.LayoutParams) bL.getLayoutParams();
        pB.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        bL.setId(R.id.board);
        l.addView(bL, pB);

        RelativeLayout controlsL = this.controls;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.board);
        params.topMargin = 50;
        l.addView(controlsL, params);
        activity.setContentView(l);
        this.controls.update();

    }

    public void sendMessageToServer(Message message)
    {
        ConnectionToServer.sendMessage(message, fromId);
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

    public void switchElements(Coords first, Coords second, long fromId)
    {
        Action a = new SwitchAction(first, second, true, this);
        performAction(a);
        //board.switchElements(first, second);
        //updateAfterSwitch();
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

    public void next()
    {
        locked = true;
        Logger.logInfo(LogKey, "Next");
        super.next();
        sendMessageToServer(new NextMessage());
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

    public void recreateBoard(Board board)
    {
        Logger.logInfo(LogKey, "Recreating Board");
        ((AnimatedBoard) this.board).updateBoard(board);
    }
}
