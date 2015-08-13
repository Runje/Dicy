package games.runje.dicy.controller;

import android.app.Activity;

import java.util.ArrayList;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.communication.ConnectionToServer;
import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.NextMessage;
import games.runje.dicymodel.communication.messages.SkillMessage;
import games.runje.dicymodel.communication.messages.SwitchMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.04.2015.
 */
public class AnimatedClientGamemaster extends AnimatedGamemaster implements ClientGamemaster
{
    public static String LogKey = "AnimatedClientGamemaster";
    private boolean messageInProcess = false;
    private boolean waitForMessage = true;

    public AnimatedClientGamemaster(Board board, Rules rules, Activity activity, LocalGame game)
    {
        super(game, rules, activity, board);
    }

    @Override
    public void receiveMessage(final Message msg)
    {
        Logger.logDebug(LogKey, "ReceiveMessage in Gamemaster");

        while (messageInProcess || !waitForMessage)
        {
            try
            {
                Logger.logDebug(LogKey, String.format("Waiting: %b, Processing: %b", waitForMessage, messageInProcess));
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        messageInProcess = true;
        waitForMessage = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msg.executeAtClient(AnimatedClientGamemaster.this);
                messageInProcess = false;
            }
        });
    }

    @Override
    public void switchElementsFromHost(Coords first, Coords second)
    {
        // TODO
        switchElements(first, second);
    }

    public void nextFromUser()
    {
        sendMessageToServer(new NextMessage());
        next(false);
        waitForMessage = true;

    }

    @Override
    public void nextFromHost()
    {
        next(false);
    }

    @Override
    public void startRecreateAnimation(ArrayList<BoardElement> elements)
    {
        this.recreateElements = elements;
        startRecreateAnimation();
    }

    @Override
    public void recreateBoard(Board board)
    {
        animatedBoard.recreateBoard(board);
        stateTransition(GameState.Normal);
    }


    @Override
    public void executeFirstSkillMessageFromHost(Skill s)
    {
        waitForMessage = false;
        super.executeSkillFromUser2(s, true);
        waitForMessage = true;
    }

    @Override
    public void executeSecondSkillMessageFromHost(Skill s)
    {
        waitForMessage = false;
        Logger.logInfo(LogKey, "Executing second skill message");
        endWait(s.getPos());
    }

    @Override
    public void executeSkillFromUser(Skill s)
    {
        waitForMessage = false;
        sendMessageToServer(new SkillMessage(s, getIndex(s), true));
        super.executeSkillFromUser2(s, true);
        // Only on shuffle skill???
        if (s.getName().equals(Skill.Shuffle))
        {
            waitForMessage = true;
        }
    }

    @Override
    public void endWait(Coords pos)
    {
        super.endWait(pos);
    }

    @Override
    public void executeOnTouch(Coords pos)
    {
        activeSkill.setPos(pos);
        sendMessageToServer(new SkillMessage(activeSkill, getIndex(activeSkill), false));
        endWait(pos);
    }

    public void switchElementsFromUser(Coords first, Coords second)
    {
        waitForMessage = false;
        sendMessageToServer(new SwitchMessage(first, second));
        switchElements(first, second);
    }

    public void sendMessageToServer(Message message)
    {
        ConnectionToServer.sendMessage(message);
    }

    @Override
    public void endFallAnimation()
    {
        // TODO: make function transitionToFallen
        this.state = GameState.Fallen;
        this.waitForMessage = true;
        // do nothing more, wait for Message with falling elements
    }

    public void startGame(Board board, Rules r, final LocalGame game)
    {
        this.board = board;
        this.rules = r;
        this.game = game;
        Logger.logInfo(LogKey, "Starting Game with " + game.getPlayers().get(0) + " and " + game.getPlayers().get(1));
    }

    protected void transitionToNormal()
    {
        super.transitionToNormal();
        waitForMessage = true;
    }

    public void changeGravityFromUser(Gravity gravity)
    {
        sendMessageToServer(new GravityMessage(gravity));
        changeGravity(gravity);
    }

    public void changeGravityFromHost(Gravity gravity)
    {
        changeGravity(gravity);
        waitForMessage = true;
    }

    protected void startRecreateBoardAnimation()
    {
        Logger.logInfo(LogKey, "Waiting for Board to be recreated. Old board: " + board.toString());
        waitForMessage = true;
    }
}
