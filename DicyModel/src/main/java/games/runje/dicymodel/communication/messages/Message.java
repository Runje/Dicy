package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.HostGamemaster;
import games.runje.dicymodel.communication.MessageConverter;

/**
 * Created by Thomas on 14.02.2015.
 */
public abstract class Message
{
    public static final long ServerId = 0;
    protected final int headerLength = MessageConverter.sizeLength + MessageConverter.nameLength + MessageConverter.idLength + MessageConverter.idLength;
    protected int contentLength;
    protected String content;
    protected long fromId;
    protected long toId;

    public abstract String getName();

    public int getTotalLength()
    {
        return headerLength + contentLength;
    }

    public String getContent()
    {
        return content;
    }

    protected byte[] lengthAndNameToByte()
    {
        int size = MessageConverter.sizeLength + MessageConverter.nameLength;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.putInt(getTotalLength());
        buffer.put(MessageConverter.stringToByte(getName(), MessageConverter.nameLength));
        return buffer.array();
    }

    protected abstract byte[] contentToByte();

    public void executeAtHost(HostGamemaster gamemaster)
    {

    }

    public void executeAtClient(ClientGamemaster gamemaster)
    {

    }

    public byte[] toByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.put(lengthAndNameToByte());
        buffer.putLong(fromId);
        buffer.putLong(toId);
        buffer.put(contentToByte());
        return buffer.array();
    }

    public long getFromId()
    {
        return fromId;
    }

    public void setFromId(long fromId)
    {
        this.fromId = fromId;
    }

    public long getToId()
    {
        return toId;
    }

    public void setToId(long toId)
    {

        this.toId = toId;
    }
}
