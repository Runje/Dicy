package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
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
        this.length = 4 * MessageConverter.sizeLength + MessageConverter.sizeLength + MessageConverter.nameLength;
    }

    public SwitchMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
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
        gamemaster.switchElements(first, second);
    }

    @Override
    public byte[] toByte()
    {
        // TODO: make method in base class because first two lines are uses in each sub class
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        buffer.put(MessageConverter.coordsToByte(first));
        buffer.put(MessageConverter.coordsToByte(second));
        return buffer.array();
    }
}
