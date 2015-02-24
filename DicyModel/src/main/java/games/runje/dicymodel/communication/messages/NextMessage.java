package games.runje.dicymodel.communication.messages;

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
        this.contentLength = 0;
    }

    public NextMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
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

    public byte[] contentToByte()
    {
        return new byte[0];
    }
}
