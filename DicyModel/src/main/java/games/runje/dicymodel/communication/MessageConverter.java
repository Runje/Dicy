package games.runje.dicymodel.communication;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.SavedGame;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.GameState;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.game.RuleVariant;
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
    public static final int ruleVariantLength = 15;
    public static final int playerNameLength = 20;
    public static final int gameLengthLength = 6;
    public static final int rulesLength = 4 * 4 + 1 + 1 + 4 + gameLengthLength + ruleVariantLength + 4 + 8;
    public static final int skillLength = coordsLength + 1 + 3 * 4 + skillNameLength;
    public static final int strategyLength = 100;
    public static final int playerLength = playerNameLength + idLength + 4 + strategyLength + 4 + 2 + 3 * skillLength;
    public static final int gameLength = 2 * 4 + 4 * playerLength + 3 * 4 + 4 + rulesLength;
    public static final int moveLength = 2 * coordsLength;
    public static final int gameStateLength = 14;
    public static final int savedGameLength = boardLength + gameLength + rulesLength + gameStateLength + moveLength + 4;


    public static byte[] playerToByte(Player player)
    {
        ByteBuffer buffer = ByteBuffer.allocate(playerLength);
        buffer.put(stringToByte(player.getName(), playerNameLength));
        buffer.putLong(player.getId());
        buffer.putInt(player.getPoints());
        buffer.put(strategyToByte(player.getStrategy()));
        buffer.putInt(player.getStrikes());
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
        int strikes = buffer.getInt();
        short numberSkills = buffer.getShort();
        List<Skill> skills = new ArrayList<>();
        for (int i = 0; i < numberSkills; i++)
        {
            Skill skill = byteToSkill(buffer);
            skills.add(skill);
        }

        Player player = new Player(name, Strategy.getStrategy(strategy), id, skills);
        player.setPoints(points);
        player.setStrikes(strikes);
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

    public static byte booleanToByte(boolean b)
    {
        return (byte) (b ? 1 : 0);
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
            name = strategy.toString();
        }

        return stringToByte(name, strategyLength);
    }

    public static byte[] gameToByte(LocalGame game)
    {
        ByteBuffer buffer = ByteBuffer.allocate(gameLength);
        buffer.putInt(game.getLastLeadingPlayer());
        List<Player> players = game.getPlayers();
        buffer.putInt(players.size());
        for (Player p : players)
        {
            buffer.put(playerToByte(p));
        }

        fillBufferWithZero(buffer, (4 - players.size()) * playerLength);

        buffer.putInt(game.getMovePoints());
        buffer.putInt(game.getSwitchPoints());
        buffer.putInt(game.getTurn());
        buffer.putInt(game.getPlayerIsPlayingSince());
        buffer.put(rulesToByte(game.getRules()));
        return buffer.array();
    }

    public static LocalGame byteToGame(ByteBuffer buffer)
    {
        int lastLeadingPlayer = buffer.getInt();
        int numberPlayer = buffer.getInt();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numberPlayer; i++)
        {
            players.add(byteToPlayer(buffer));
        }

        int oldPosition = buffer.position();
        buffer.position(oldPosition + (4 - players.size()) * playerLength);
        int movePoints = buffer.getInt();
        int switchPoints = buffer.getInt();
        int turn = buffer.getInt();
        int time = buffer.getInt();
        Rules rules = byteToRules(buffer);
        LocalGame game = new LocalGame(players, lastLeadingPlayer, rules);
        game.setMovePoints(movePoints);
        game.setSwitchPoints(switchPoints);
        game.setTurn(turn);
        game.setPlayerIsPlayingSince(time);

        return game;
    }

    public static void fillBufferWithZero(ByteBuffer buffer, int numberOfFills)
    {
        assert numberOfFills >= 0;
        buffer.put(new byte[numberOfFills]);
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

    public static Rules byteToRules(ByteBuffer buffer)
    {
        int pointLimit = buffer.getInt();
        int gameEndPoints = buffer.getInt();
        int minStraight = buffer.getInt();
        int minXOfAkInd = buffer.getInt();
        boolean diagonal = byteToBoolean(buffer.get());
        boolean time = byteToBoolean(buffer.get());
        int timeLimit = buffer.getInt();
        GameLength gameLength = GameLength.valueOf(byteToString(buffer, gameLengthLength));
        RuleVariant ruleVariant = RuleVariant.getEnum(byteToString(buffer, ruleVariantLength));

        int allowedStrikes = buffer.getInt();
        double skillFactor = buffer.getDouble();
        Rules rules = new Rules();
        rules.setPointLimit(pointLimit);
        rules.setGameEndPoints(gameEndPoints);
        rules.setMinStraight(minStraight);
        rules.setMinXOfAKind(minXOfAkInd);
        rules.setDiagonalActive(diagonal);
        rules.setTimeLimit(time);
        rules.setTimeLimitInS(timeLimit);
        rules.setGameLength(gameLength);
        rules.setRuleVariant(ruleVariant);
        rules.setAllowedStrikes(allowedStrikes);
        rules.setSkillLoadFactor(skillFactor);
        return rules;
    }

    public static byte[] rulesToByte(Rules rules)
    {
        ByteBuffer buffer = ByteBuffer.allocate(rulesLength);
        buffer.putInt(rules.getPointLimit());
        buffer.putInt(rules.getGameEndPoints());
        buffer.putInt(rules.getMinStraight());
        buffer.putInt(rules.getMinXOfAKind());
        buffer.put(booleanToByte(rules.isDiagonalActive()));
        buffer.put(booleanToByte(rules.isTimeLimit()));
        buffer.putInt(rules.getTimeLimitInS());
        buffer.put(stringToByte(rules.getGameLength().toString(), gameLengthLength));
        buffer.put(stringToByte(rules.getRuleVariant().toString(), ruleVariantLength));
        buffer.putInt(rules.getAllowedStrikes());
        buffer.putDouble(rules.getSkillLoadFactor());
        return buffer.array();
    }

    public static byte[] savedGameToByte(SavedGame game)
    {
        ByteBuffer buffer = ByteBuffer.allocate(savedGameLength);
        buffer.put(boardToByte(game.getBoard()));
        buffer.put(gameToByte(game.getGame()));
        buffer.put(rulesToByte(game.getRules()));
        buffer.put(gameStateToByte(game.getNextGameState()));
        buffer.put(moveToByte(game.getLastMove()));
        buffer.putInt(game.getActiveSkillIndex());
        return buffer.array();
    }

    public static SavedGame byteToSavedGame(ByteBuffer buffer)
    {
        Board b = byteToBoard(buffer);
        LocalGame game = byteToGame(buffer);
        Rules rules = byteToRules(buffer);
        GameState state = byteToGameState(buffer);
        Move move = byteToMove(buffer);
        int i = buffer.getInt();
        return new SavedGame(rules, game, b, state, move, i);
    }

    private static byte[] gameStateToByte(GameState state)
    {
        ByteBuffer buffer = ByteBuffer.allocate(gameStateLength);
        buffer.put(stringToByte(state.toString(), gameStateLength));
        return buffer.array();
    }

    private static GameState byteToGameState(ByteBuffer buffer)
    {
        String string = byteToString(buffer, gameStateLength);
        return GameState.valueOf(string);
    }

    private static byte[] moveToByte(Move lastMove)
    {
        ByteBuffer buffer = ByteBuffer.allocate(moveLength);
        if (lastMove == null)
        {
            lastMove = new Move(new Coords(0, 0), new Coords(0, 0));
        }
        buffer.put(coordsToByte(lastMove.getFirst()));
        buffer.put(coordsToByte(lastMove.getSecond()));
        return buffer.array();
    }

    private static Move byteToMove(ByteBuffer buffer)
    {
        Coords first = byteToCoords(buffer);
        Coords second = byteToCoords(buffer);
        return new Move(first, second);
    }
}
