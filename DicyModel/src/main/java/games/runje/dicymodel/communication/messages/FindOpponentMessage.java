package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;

/**
 * Created by Thomas on 23.02.2015.
 */
public class FindOpponentMessage extends Message
{
    public static final String Name = "FindOpponentMessage";

    public FindOpponentMessage()
    {
        this.contentLength = 0;
    }

    public FindOpponentMessage(ByteBuffer buffer, int length)
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
        gamemaster.findOpponent(fromId);
    }

    public byte[] contentToByte()
    {
        return new byte[0];
    }

}
