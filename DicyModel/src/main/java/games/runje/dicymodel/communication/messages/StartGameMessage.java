package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.OnlineStrategy;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.Player;
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

    public StartGameMessage(Board board, Rules rules, LocalGame game)
    {
        this.board = board;
        this.rules = rules;
        this.game = game;
        this.contentLength = MessageConverter.boardLength + MessageConverter.gameLength;
    }

    public StartGameMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        board = MessageConverter.byteToBoard(buffer);
        game = MessageConverter.byteToGame(buffer);

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
        for (Player p : game.getPlayers())
        {
            if (p.getId() != toId)
            {
                p.setStrategy(new OnlineStrategy());
                System.out.println(p.getId() + " is online player");
            }
        }

        gamemaster.startGame(board, rules, game);
    }
}
