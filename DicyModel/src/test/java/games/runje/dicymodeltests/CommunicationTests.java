package games.runje.dicymodeltests;

import org.junit.Test;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.communication.messages.StartGameMessage;
import games.runje.dicymodel.data.Board;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thomas on 15.02.2015.
 */
public class CommunicationTests
{
    @Test
    public void boardToByteTest()
    {
        Board expectedBoard = new Board(2, 3);
        byte[] boardBytes = MessageConverter.boardToByte(expectedBoard);

        Board board = MessageConverter.byteToBoard(ByteBuffer.wrap(boardBytes));

        assertEquals(expectedBoard.getNumberOfColumns(), board.getNumberOfColumns());
        assertEquals(expectedBoard.getNumberOfRows(), board.getNumberOfRows());
        assertEquals(expectedBoard.toString(), board.toString());
        assertEquals(expectedBoard.getGravity(), board.getGravity());
    }

    @Test
    public void startGameMessageTest()
    {
        Board b = new Board(2, 3);
        Rules r = new Rules();
        StartGameMessage expMessage = new StartGameMessage(b, r, null);
        byte[] bytes = expMessage.toByte();

        // buffer as received
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.position(buffer.position() + 4 + MessageConverter.nameLength);
        StartGameMessage message = new StartGameMessage(buffer, bytes.length);

        assertEquals(expMessage.getTotalLength(), message.getTotalLength());
        assertEquals(expMessage.getName(), message.getName());
        assertEquals(expMessage.getBoard().toString(), message.getBoard().toString());

    }
}
