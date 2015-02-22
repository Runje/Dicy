package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 14.02.2015.
 */
public class MessageParser
{
    public static Message parse(ByteBuffer buffer, int length)
    {
        String name = MessageConverter.byteToString(buffer, MessageConverter.nameLength);
        System.out.println("Parsing Message: " + name);
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

        }

        return null;
    }
}
