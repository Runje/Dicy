package games.runje.dicymodel;

import java.io.IOException;
import java.net.Socket;

import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.NextMessage;
import games.runje.dicymodel.communication.messages.RecreateBoardMessage;
import games.runje.dicymodel.communication.messages.RecreateElementsMessage;
import games.runje.dicymodel.communication.messages.SkillMessage;
import games.runje.dicymodel.communication.messages.SwitchMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.04.2015.
 */
public class HostGamemaster extends AbstractGamemaster
{
    public static String LogKey = "HostGamemaster";
    private final Socket firstClient;
    private final Socket secondClient;
    private final long firstId;
    private final long secondId;

    public HostGamemaster(Board board, Rules rules, LocalGame game, Socket firstClient, long firstId, Socket secondClient, long secondId)
    {
        super(rules, game, board);
        this.firstClient = firstClient;
        this.secondClient = secondClient;
        this.firstId = firstId;
        this.secondId = secondId;
        this.controls = new DummyControls();
    }

    public void switchElements(Coords first, Coords second, long fromId)
    {
        Logger.logInfo(LogKey, String.format("Switching %s with %s from %d", first, second, fromId));
        sendMessageToOther(new SwitchMessage(first, second), fromId);
        switchElements(first, second);
    }

    @Override
    protected void startRecreateAnimation()
    {
        Message msg = new RecreateElementsMessage(recreateElements);
        sendMessageToBoth(msg);
        super.startRecreateAnimation();
    }

    private void sendMessageToBoth(Message msg)
    {
        sendMessage(msg, firstClient, firstId);
        sendMessage(msg, secondClient, secondId);
    }

    public void sendMessageToOther(Message message, long id)
    {
        if (id == Message.ServerId)
        {
            return;
        }

        long toId = 0;
        Socket socket = null;
        if (id == firstId)
        {
            toId = secondId;
            socket = secondClient;
        }
        else
        {
            toId = firstId;
            socket = firstClient;
        }

        sendMessage(message, socket, toId);
    }

    public void sendMessage(Message message, Socket s, long id)
    {
        try
        {
            message.setFromId(Message.ServerId);
            message.setToId(id);
            Logger.logInfo(LogKey, "Sending " + message.getName() + " from " + message.getFromId() + " to " + message.getToId());
            s.getOutputStream().write(message.toByte());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void next(long fromId)
    {
        super.next();
        sendMessageToOther(new NextMessage(), fromId);
    }

    public void changeGravity(Gravity gravity, long fromId)
    {
        board.setGravity(gravity);
        sendMessageToOther(new GravityMessage(gravity), fromId);
    }

    protected void startRecreateBoardAnimation()
    {
        Logger.logDebug(LogKey, "Old board: " + board.toString());
        board.shuffle(false, rules);
        Logger.logDebug(LogKey, "New board: " + board.toString());
        sendMessageToBoth(new RecreateBoardMessage(board));
        endRecreateBoardAnimation();
    }

    public void executeFirstSkillMessage(Skill s, long fromId)
    {
        sendMessageToOther(new SkillMessage(s, getIndex(s), true), fromId);
        executeSkillFromUser(s);

        if (s.getName().equals(Skill.Shuffle))
        {
            ShuffleSkill shuffleSkill = (ShuffleSkill) s;
            shuffleSkill.shuffle(board);
            SkillMessage msg = new SkillMessage(s, getIndex(s), false, shuffleSkill.getNewBoard());

            sendMessageToBoth(msg);
            endWait(s.getPos());
        }
    }

    public void executeSecondSkillMessage(Skill s, long fromId)
    {
        //this.activeSkill = s;
        //getGame().setStrikePossible(false);
        sendMessageToOther(new SkillMessage(s, getIndex(activeSkill), false), fromId);
        endWait(s.getPos());
    }

}
