package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

/**
 * Created by Thomas on 26.02.2015.
 */
public class IdentifyMessage extends Message
{
    public static final String Name = "IdentifyMessage";

    public IdentifyMessage()
    {
        this.contentLength = 0;
    }

    public IdentifyMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
    }

    @Override
    public String getName()
    {
        return Name;
    }

/*    @Override
    public void execute(CommunicationGamemaster gamemaster)
    {
        //gamemaster.next(fromId);
    }*/

    public byte[] contentToByte()
    {
        return new byte[0];
    }
}
