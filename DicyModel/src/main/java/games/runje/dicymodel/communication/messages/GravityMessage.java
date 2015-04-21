package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.HostGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 23.02.2015.
 */
public class GravityMessage extends Message
{
    public static final String Name = "GravityMessage";

    private Gravity gravity;
    private String LogKey = "GravityMessage";

    public GravityMessage(Gravity g)
    {
        this.contentLength = MessageConverter.gravityLength;
        this.gravity = g;
    }

    public GravityMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        this.gravity = Gravity.valueOf(MessageConverter.byteToString(buffer, MessageConverter.gravityLength));
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void executeAtHost(HostGamemaster gamemaster)
    {
        gamemaster.changeGravity(gravity, fromId);
    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        gamemaster.changeGravityFromHost(gravity);
    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        byte[] bytes = MessageConverter.stringToByte(gravity.toString());
        Logger.logInfo(LogKey, "contentlength: " + contentLength + ", bytesLength: " + bytes.length + ", gravity: " + gravity);
        buffer.put(bytes);
        MessageConverter.fillBufferWithZero(buffer, MessageConverter.gravityLength - bytes.length);
        return buffer.array();
    }

}
