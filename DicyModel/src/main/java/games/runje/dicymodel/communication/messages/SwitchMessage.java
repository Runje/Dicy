package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 17.02.2015.
 */
public class SwitchMessage extends Message
{
    public static final String Name = "SwitchMessage";
    private Coords second;
    private Coords first;

    public SwitchMessage(Coords first, Coords second)
    {
        this.first = first;
        this.second = second;
        this.contentLength = 4 * MessageConverter.sizeLength;
    }

    public SwitchMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        first = MessageConverter.byteToCoords(buffer);
        second = MessageConverter.byteToCoords(buffer);
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void execute(Gamemaster gamemaster)
    {
        // TODO: check if possible
        gamemaster.switchElements(first, second, fromId);
    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        buffer.put(MessageConverter.coordsToByte(first));
        buffer.put(MessageConverter.coordsToByte(second));
        return buffer.array();
    }
}
