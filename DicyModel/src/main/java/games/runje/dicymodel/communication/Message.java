package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;

/**
 * Created by Thomas on 14.02.2015.
 */
public abstract class Message
{
    protected int length;
    protected String content;

    public abstract String getName();

    public int getLength()
    {
        return length;
    }

    public String getContent()
    {
        return content;
    }

    protected byte[] lengthAndNameToByte()
    {
        int size = MessageConverter.sizeLength + MessageConverter.nameLength;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(length);
        // TODO: hier wird getName nicht von der abgeleiteten klasse aufgerufen!
        buffer.put(MessageConverter.stringToByte(getName()));
        MessageConverter.fillBufferWithZero(buffer, MessageConverter.nameLength - getName().length());
        return buffer.array();
    }

    public abstract void execute(Gamemaster gamemaster);

    public abstract byte[] toByte();
}
