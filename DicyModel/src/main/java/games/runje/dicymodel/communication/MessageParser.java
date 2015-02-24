package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.communication.messages.FindOpponentMessage;
import games.runje.dicymodel.communication.messages.GravityMessage;
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
    public static Message parse(ByteBuffer buffer, int length)
    {
        String name = MessageConverter.byteToString(buffer, MessageConverter.nameLength);
        long fromId = buffer.getLong();
        long toId = buffer.getLong();
        System.out.println("Parsing Message " + name + ", from " + fromId + " to " + toId);
        switch (name)
        {
            case StartGameMessage.Name:
                return new StartGameMessage(buffer, length);
            case SwitchMessage.Name:
                return new SwitchMessage(buffer, length);
            case RecreateElementsMessage.Name:
                return new RecreateElementsMessage(buffer, length);
            case RecreateBoardMessage.Name:
                return new RecreateBoardMessage(buffer, length);
            case NextMessage.Name:
                return new NextMessage(buffer, length);
            case SkillMessage.Name:
                return new SkillMessage(buffer, length);
            case GravityMessage.Name:
                return new GravityMessage(buffer, length);
            case FindOpponentMessage.Name:
                return new FindOpponentMessage(buffer, length);

        }

        return null;
    }
}
