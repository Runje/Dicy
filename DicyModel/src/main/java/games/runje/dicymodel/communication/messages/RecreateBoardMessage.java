package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Board;

/**
 * Created by thomas on 22.02.15.
 */
public class RecreateBoardMessage extends Message
{
    public static final String Name = "RecreateBoardMessage";
    Board board;
    private String LogKey = Name;

    public RecreateBoardMessage(Board board)
    {
        this.board = board;
        this.contentLength = MessageConverter.boardLength;
    }

    public RecreateBoardMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        board = MessageConverter.byteToBoard(buffer);
    }

    public String getName()
    {
        return Name;
    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        buffer.put(MessageConverter.boardToByte(board));
        return buffer.array();
    }

    public Board getBoard()
    {
        return board;
    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        Logger.logInfo(LogKey, "RecreateBoardMessage is executed");
        gamemaster.recreateBoard(board);
    }
}
