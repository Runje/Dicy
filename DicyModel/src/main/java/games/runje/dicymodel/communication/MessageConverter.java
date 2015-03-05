package games.runje.dicymodel.communication;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 14.02.2015.
 */
public class MessageConverter
{
    public static final int sizeLength = 4;
    public static final int messageSizeLength = 5;
    public static final int rowSizeLength = 4;
    public static final int allBoardElementsLength = Rules.maxLengthOfRow * rowSizeLength * Rules.maxLengthOfRow * rowSizeLength;
    public static final int coordsLength = 8;
    public static final int boardElementLength = coordsLength + 4;
    public static final String encoding = "UTF8";
    public static final int gravityLength = 5;
    public static final int boardLength = 2 * rowSizeLength + allBoardElementsLength + gravityLength;
    public static final int nameLength = 20;
    public static final int skillLength = 15;
    public static final int idLength = 8;
    public static final int playerNameLength = 20;
    public static final int playerLength = playerNameLength + idLength;
    public static final int gameLength = 4 + 4 + 4 + 4 + 4 * playerLength;


    public static byte[] playerToByte(Player player)
    {
        ByteBuffer buffer = ByteBuffer.allocate(playerLength);
        buffer.put(stringToByte(player.getName()));
        fillBufferWithZero(buffer, playerNameLength - player.getName().length());
        buffer.putLong(player.getId());
        return buffer.array();
    }

    public static Player byteToPlayer(ByteBuffer buffer)
    {
        String name = byteToString(buffer, playerNameLength);
        long id = buffer.getLong();
        return new Player(name, null, id);
    }

    public static byte[] gameToByte(LocalGame game)
    {
        ByteBuffer buffer = ByteBuffer.allocate(gameLength);
        buffer.putInt(game.getPointsLimit());
        buffer.putInt(game.getGameEndPoints());
        buffer.putInt(game.getLastLeadingPlayer());
        List<Player> players = game.getPlayers();
        buffer.putInt(players.size());
        for (Player p : players)
        {
            buffer.put(playerToByte(p));
        }

        fillBufferWithZero(buffer, (4 - players.size()) * playerLength);

        return buffer.array();
    }

    public static LocalGame byteToGame(ByteBuffer buffer)
    {
        int pointsLimit = buffer.getInt();
        int gameEndPoints = buffer.getInt();
        int startingPlayer = buffer.getInt();
        int numberPlayer = buffer.getInt();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numberPlayer; i++)
        {
            players.add(byteToPlayer(buffer));
        }

        LocalGame game = new LocalGame(pointsLimit, gameEndPoints, players, startingPlayer);
        return game;
    }


    public static byte[] boardToByte(Board board)
    {
        ByteBuffer buffer = ByteBuffer.allocate(boardLength);
        int rows = board.getNumberOfRows();
        int columns = board.getNumberOfColumns();

        buffer.putInt(rows);
        buffer.putInt(columns);

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                buffer.putInt(board.getElement(i, j).getValue());
            }
        }

        // TODO: kann ich das auch weglassen?
        fillBufferWithZero(buffer, allBoardElementsLength - (rows * columns * 4));
        byte[] bytes = stringToByte(board.getGravity().toString());
        buffer.put(bytes);
        fillBufferWithZero(buffer, gravityLength - bytes.length);

        return buffer.array();
    }


    public static void fillBufferWithZero(ByteBuffer buffer, int numberOfFills)
    {
        assert numberOfFills >= 0;
        buffer.put(new byte[numberOfFills]);
    }


    public static Board byteToBoard(ByteBuffer buffer)
    {
        int rows = buffer.getInt();
        int columns = buffer.getInt();
        Board result = new Board(rows, columns);
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                BoardElement element = BoardElement.createFromValue(buffer.getInt(), i, j);
                result.setElement(i, j, element);
            }
        }

        int oldPosition = buffer.position();
        buffer.position(oldPosition + allBoardElementsLength - (rows * columns * 4));
        byte[] gravityBytes = new byte[gravityLength];
        Gravity gravity = Gravity.valueOf(byteToString(buffer, gravityLength));
        result.setGravity(gravity);
        return result;
    }

    public static byte[] boardElementToByte(BoardElement element)
    {
        ByteBuffer buffer = ByteBuffer.allocate(boardElementLength);

        // pos
        buffer.put(coordsToByte(element.getPosition()));

        // value
        buffer.putInt(element.getValue());

        return buffer.array();
    }

    public static BoardElement byteToBoardElement(ByteBuffer buffer)
    {
        Coords pos = byteToCoords(buffer);
        int value = buffer.getInt();

        return BoardElement.createFromValue(value, pos);
    }

    public static byte[] coordsToByte(Coords position)
    {
        ByteBuffer buffer = ByteBuffer.allocate(coordsLength);
        buffer.putInt(position.row);
        buffer.putInt(position.column);
        return buffer.array();
    }

    public static byte[] stringToByte(String string)
    {
        try
        {
            return string.getBytes(encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String byteToString(ByteBuffer buffer, int length)
    {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, Charset.forName(encoding)).trim();
    }

    public static Coords byteToCoords(ByteBuffer buffer)
    {
        return new Coords(buffer.getInt(), buffer.getInt());
    }


}
