package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.messages.FindOpponentMessage;
import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.communication.messages.IdentifyMessage;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.NextMessage;
import games.runje.dicymodel.communication.messages.RecreateBoardMessage;
import games.runje.dicymodel.communication.messages.RecreateElementsMessage;
import games.runje.dicymodel.communication.messages.SkillMessage;
import games.runje.dicymodel.communication.messages.StartGameMessage;
import games.runje.dicymodel.communication.messages.SwitchMessage;

/**
 * Created by Thomas on 14.02.2015.
 */
public class MessageParser
{
    public static String LogKey = "MessageParser";

    public static Message parse(ByteBuffer buffer, int length)
    {
        String name = MessageConverter.byteToString(buffer, MessageConverter.nameLength);
        long fromId = buffer.getLong();
        long toId = buffer.getLong();

        Logger.logInfo(LogKey, "Parsing Message " + name + ", from " + fromId + " to " + toId);

        Message msg = null;
        switch (name)
        {
            case StartGameMessage.Name:
                msg = new StartGameMessage(buffer, length);
                break;
            case SwitchMessage.Name:
                msg = new SwitchMessage(buffer, length);
                break;
            case RecreateElementsMessage.Name:
                msg = new RecreateElementsMessage(buffer, length);
                break;
            case RecreateBoardMessage.Name:
                msg = new RecreateBoardMessage(buffer, length);
                break;
            case NextMessage.Name:
                msg = new NextMessage(buffer, length);
                break;
            case SkillMessage.Name:
                msg = new SkillMessage(buffer, length);
                break;
            case GravityMessage.Name:
                msg = new GravityMessage(buffer, length);
                break;
            case FindOpponentMessage.Name:
                msg = new FindOpponentMessage(buffer, length);
                break;
            case IdentifyMessage.Name:
                msg = new IdentifyMessage(buffer, length);
                break;

        }

        msg.setToId(toId);
        msg.setFromId(fromId);
        return msg;
    }
}
