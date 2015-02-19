package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.data.BoardElement;

/**
 * Created by Thomas on 17.02.2015.
 */
public class RecreateElementsMessage extends Message
{
    public static final String Name = "RecreateMessage";
    private ArrayList<BoardElement> elements;

    public RecreateElementsMessage(ArrayList<BoardElement> e)
    {
        elements = e;
        length = MessageConverter.nameLength + MessageConverter.sizeLength + MessageConverter.sizeLength + elements.size() * MessageConverter.boardElementLength;
    }

    public RecreateElementsMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
        elements = new ArrayList<>();

        int size = buffer.getInt();
        for (int i = 0; i < size; i++)
        {
            elements.add(MessageConverter.byteToBoardElement(buffer));
        }
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
    public void execute(Gamemaster gamemaster)
    {
        gamemaster.updateAfterFall(elements);
    }

    @Override
    public byte[] toByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        buffer.putInt(elements.size());
        for (BoardElement element : elements)
        {
            buffer.put(MessageConverter.boardElementToByte(element));
        }

        return buffer.array();
    }
}
