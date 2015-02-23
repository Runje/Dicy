package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.data.Gravity;

/**
 * Created by Thomas on 23.02.2015.
 */
public class GravityMessage extends Message
{
    public static final String Name = "GravityMessage";

    private Gravity gravity;

    public GravityMessage(Gravity g)
    {
        this.length = MessageConverter.sizeLength + MessageConverter.nameLength + MessageConverter.gravityLength;
        this.gravity = g;
    }

    public GravityMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
        this.gravity = Gravity.valueOf(MessageConverter.byteToString(buffer, MessageConverter.gravityLength));
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void execute(Gamemaster gamemaster)
    {
        gamemaster.changeGravity(gravity);
    }

    @Override
    public byte[] toByte()
    {
        // TODO: make method in base class because first two lines are uses in each sub class
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        byte[] bytes = MessageConverter.stringToByte(gravity.toString());
        buffer.put(bytes);
        MessageConverter.fillBufferWithZero(buffer, MessageConverter.gravityLength - bytes.length);
        return buffer.array();
    }

}
