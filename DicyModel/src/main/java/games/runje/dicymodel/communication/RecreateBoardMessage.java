package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;

/**
 * Created by thomas on 22.02.15.
 */
public class RecreateBoardMessage extends Message
{
    public static final String Name = "RecreateBoardMessage";
    Board board;

    public RecreateBoardMessage(Board board)
    {
        this.board = board;
        this.length = MessageConverter.boardLength + MessageConverter.sizeLength + MessageConverter.nameLength;
    }

    public RecreateBoardMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
        board = MessageConverter.byteToBoard(buffer);
    }

    public String getName()
    {
        return Name;
    }

    public byte[] toByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        buffer.put(MessageConverter.boardToByte(board));

        return buffer.array();
    }

    public Board getBoard()
    {
        return board;
    }

    @Override
    public void execute(Gamemaster gamemaster)
    {
        System.out.println("RecreateBoardMessage is executed");
        gamemaster.recreateBoard(board);
    }
}
