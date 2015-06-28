package games.runje.dicymodel.communication;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

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
    public static final int skillNameLength = 15;
    public static final int idLength = 8;
    public static final int playerNameLength = 20;
    public static final int skillLength = coordsLength + 1 + 4 + 4 + 4 + skillNameLength;
    public static final int strategyLength = 15;
    public static final int playerLength = playerNameLength + idLength + 4 + strategyLength + 2 + 3 * skillLength;
    public static final int gameLength = 4 + 4 + 4 + 4 + 4 * playerLength;


    public static byte[] playerToByte(Player player)
    {
        ByteBuffer buffer = ByteBuffer.allocate(playerLength);
        buffer.put(stringToByte(player.getName(), playerNameLength));
        buffer.putLong(player.getId());
        buffer.putInt(player.getPoints());
        buffer.put(strategyToByte(player.getStrategy()));
        short numberSkills = (short) player.getSkills().size();
        buffer.putShort(numberSkills);
        for (Skill skill : player.getSkills())
        {
            buffer.put(skillToByte(skill));
        }

        fillBufferWithZero(buffer, (3 - numberSkills) * skillLength);
        return buffer.array();
    }

    public static Player byteToPlayer(ByteBuffer buffer)
    {
        String name = byteToString(buffer, playerNameLength);
        long id = buffer.getLong();
        int points = buffer.getInt();
        String strategy = byteToSrategy(buffer);
        short numberSkills = buffer.getShort();
        List<Skill> skills = new ArrayList<>();
        for (int i = 0; i < numberSkills; i++)
        {
            Skill skill = byteToSkill(buffer);
            skills.add(skill);
        }

        Player player = new Player(name, Strategy.makeStrategy(strategy), id, skills);
        player.setPoints(points);
        return player;
    }

    private static String byteToSrategy(ByteBuffer buffer)
    {
        return byteToString(buffer, strategyLength);
    }

    private static byte[] skillToByte(Skill skill)
    {
        ByteBuffer buffer = ByteBuffer.allocate(skillLength);
        buffer.put(coordsToByte(skill.getPos()));
        buffer.put(booleanToByte(skill.isWaiting()));
        buffer.putInt(skill.getLoadValue());
        buffer.putInt(skill.getMaxLoad());
        buffer.putInt(skill.getCurrentLoad());
        buffer.put(stringToByte(skill.getName(), skillNameLength));
        return buffer.array();
    }

    public static Skill byteToSkill(ByteBuffer buffer)
    {
        Coords pos = byteToCoords(buffer);
        boolean waiting = byteToBoolean(buffer.get());
        int loadValue = buffer.getInt();
        int maxLoad = buffer.getInt();
        int currentLoad = buffer.getInt();
        String name = byteToString(buffer, skillNameLength);

        Skill skill = Skill.createSkill(name, loadValue, maxLoad);
        skill.setPos(pos);
        skill.setWaiting(waiting);
        skill.setCurrentLoad(currentLoad);
        return skill;
    }

    private static byte booleanToByte(boolean waiting)
    {
        return (byte) (waiting ? 1 : 0);
    }

    public static boolean byteToBoolean(Byte b)
    {
        return b == (byte) 1;
    }

    private static byte[] strategyToByte(Strategy strategy)
    {
        String name;
        if (strategy == null)
        {
            name = Strategy.Human;
        }
        else
        {
            name = Strategy.Simple;
        }

        return stringToByte(name, strategyLength);
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
        byte[] bytes = stringToByte(board.getGravity().toString(), gravityLength);
        buffer.put(bytes);
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

    public static byte[] stringToByte(String string, int maxLength)
    {
        try
        {
            ByteBuffer buffer = ByteBuffer.allocate(maxLength);
            buffer.put(string.getBytes(encoding));
            buffer.put(new byte[maxLength - string.length()]);
            return buffer.array();
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
