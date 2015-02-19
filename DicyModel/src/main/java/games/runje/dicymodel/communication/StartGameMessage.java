package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;

/**
 * Created by Thomas on 14.02.2015.
 */
public class StartGameMessage extends Message
{
    public static final String Name = "StartGameMessage";
    Board board;
    Rules rules;
    String[] player;

    public StartGameMessage(Board board, Rules rules, String[] player)
    {
        this.board = board;
        this.rules = rules;
        this.player = player;
        this.length = MessageConverter.boardLength + MessageConverter.sizeLength + MessageConverter.nameLength;
    }

    public StartGameMessage(ByteBuffer buffer, int length)
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
        System.out.println("StartGameMessage is executed");
        gamemaster.startGame(board, rules, player);
    }
}
