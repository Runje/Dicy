package games.runje.dicy.controller;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

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
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.04.2015.
 */
public class AnimatedClientGamemaster extends AnimatedGamemaster implements ClientGamemaster
{
    private String LogKey = "AnimatedClientGamemaster";
    private boolean messageInProcess = false;
    private boolean waitForMessage = true;

    public AnimatedClientGamemaster(Board board, Rules rules, Activity activity, List<Player> players)
    {
        super(players, rules, activity, board);
    }

    @Override
    public void receiveMessage(final Message msg)
    {
        Logger.logInfo(LogKey, "ReceiveMessage in Gamemaster");

        while (messageInProcess || !waitForMessage)
        {
            try
            {
                Logger.logInfo(LogKey, String.format("Waiting: %b, Processing: %b", waitForMessage, messageInProcess));
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        messageInProcess = true;
        waitForMessage = false;
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
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
        next();
        waitForMessage = true;

    }

    @Override
    public void nextFromHost()
    {
        next();
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
    public void executeSkillFromHost(Skill s)
    {
        getGame().setStrikePossible(false);
        Skill skill = getGame().getPlayingPlayer().getSkill(s.getName());
        skill.setPos(s.getPos());
        activeSkill = skill;
        skill.execute(board, this);

        // TODO: waitformessage?
    }

    @Override
    public void executeSkillFromUser(Skill s)
    {
        waitForMessage = false;
        super.executeSkillFromUser(s);
    }

    @Override
    public void endWait(Coords pos)
    {
        super.endWait(pos);
        sendMessageToServer(new SkillMessage(activeSkill));
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
        Logger.logInfo(LogKey, "Starting Game");


    }

    protected void transitionToNormal()
    {
        getGame().setStrikePossible(true);
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
