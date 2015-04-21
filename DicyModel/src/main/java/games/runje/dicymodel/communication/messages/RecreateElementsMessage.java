package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.BoardElement;

/**
 * Created by Thomas on 17.02.2015.
 */
public class RecreateElementsMessage extends Message
{
    public static final String Name = "RecreateMessage";
    private final String LogKey = Name;
    private ArrayList<BoardElement> elements;

    public RecreateElementsMessage(ArrayList<BoardElement> e)
    {
        elements = e;
        contentLength = MessageConverter.sizeLength + elements.size() * MessageConverter.boardElementLength;
    }

    public RecreateElementsMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        elements = new ArrayList<>();

        int size = buffer.getInt();
        for (int i = 0; i < size; i++)
        {
            elements.add(MessageConverter.byteToBoardElement(buffer));
        }

        Logger.logInfo(LogKey, "RecreateMessage Elements: " + elements);
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public String toString()
    {
        return "RecreateElementsMessage{" +
                "elements=" + elements +
                '}';
    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        gamemaster.startRecreateAnimation(elements);
    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        buffer.putInt(elements.size());
        for (BoardElement element : elements)
        {
            buffer.put(MessageConverter.boardElementToByte(element));
        }

        return buffer.array();
    }
}
