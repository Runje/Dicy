package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 14.02.2015.
 */
public class StartGameMessage extends Message
{
    public static final String Name = "StartGameMessage";
    Board board;
    Rules rules;
    LocalGame game;
    public static String LogKey = Name;

    public StartGameMessage(Board board, Rules rules, LocalGame game)
    {
        this.board = board;
        this.rules = rules;
        this.game = game;
        this.contentLength = MessageConverter.boardLength + MessageConverter.gameLength + MessageConverter.rulesLength;
    }

    public StartGameMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        board = MessageConverter.byteToBoard(buffer);
        game = MessageConverter.byteToGame(buffer);
        rules = MessageConverter.byteToRules(buffer);
    }

    public String getName()
    {
        return Name;
    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        buffer.put(MessageConverter.boardToByte(board));
        buffer.put(MessageConverter.gameToByte(game));
        buffer.put(MessageConverter.rulesToByte(rules));
        return buffer.array();
    }

    public Board getBoard()
    {
        return board;
    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        Logger.logDebug(LogKey, "StartGameMessage is executed");
        gamemaster.startGame(board, rules, game);
    }

    public LocalGame getGame()
    {
        return game;
    }

    public Rules getRules()
    {
        return rules;
    }
}
