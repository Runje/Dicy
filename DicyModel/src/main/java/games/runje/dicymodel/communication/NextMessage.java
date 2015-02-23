package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;

/**
 * Created by thomas on 22.02.15.
 */
public class NextMessage extends Message
{
    public static final String Name = "NextMessage";

    public NextMessage()
    {
        this.length = MessageConverter.sizeLength + MessageConverter.nameLength;
    }

    public NextMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void execute(Gamemaster gamemaster)
    {
        gamemaster.next();
    }

    @Override
    public byte[] toByte()
    {
        // TODO: make method in base class because first two lines are uses in each sub class
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        return buffer.array();
    }
}
