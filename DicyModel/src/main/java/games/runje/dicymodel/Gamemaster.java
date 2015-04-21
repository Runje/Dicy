package games.runje.dicymodel;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.communication.MessageParser;
import games.runje.dicymodel.communication.messages.GravityMessage;
import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.communication.messages.NextMessage;
import games.runje.dicymodel.communication.messages.RecreateBoardMessage;
import games.runje.dicymodel.communication.messages.RecreateElementsMessage;
import games.runje.dicymodel.communication.messages.SkillMessage;
import games.runje.dicymodel.communication.messages.SwitchMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 16.02.2015.
 */
public class Gamemaster
{
    protected long fromId;
    protected long toFirstId;
    protected Board board;
    protected Game game;
    private Socket secondSocket;
    private Rules rules;
    private Socket firstSocket;
    private long toSecondId;
    private String LogKey = "Gamemaster";

    public Gamemaster()
    {

    }

    public Gamemaster(Board b, Rules r, LocalGame game, Socket firstSocket, long firstId, Socket secondSocket, long secondId)
    {
        this.firstSocket = firstSocket;
        this.secondSocket = secondSocket;
        this.toFirstId = firstId;
        this.toSecondId = secondId;
        this.board = b;
        this.rules = r;
        List<Strategy> s = new ArrayList<>();
        s.add(null);
        s.add(null);
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }

    public Rules getRules()
    {
        return rules;
    }

    public Board getBoard()
    {
        return board;
    }

    public void receiveMessage(final ByteBuffer buffer, final int length)
    {
        Logger.logInfo(LogKey, "ReceiveMessage in Gamemaster");
        Message msg = MessageParser.parse(buffer, length);
        //msg.execute(this);
    }

    public void startGame(Board board, Rules rules, LocalGame game)
    {
        // TODO: move to animatedGamemaster?
        Logger.logInfo(LogKey, "Starting Game from Gamemaster");
    }


    public void switchElements(Coords first, Coords second, long fromId)
    {
        board.switchElements(first, second, true, rules);
        sendMessageToOther(new SwitchMessage(first, second), fromId);
        updateAfterSwitch();
    }

    public void sendMessageToOther(Message message, long id)
    {
        if (id == Message.ServerId)
        {
            return;
        }

        long toId = 0;
        Socket socket = null;
        if (id == toFirstId)
        {
            toId = toSecondId;
            socket = secondSocket;
        }
        else
        {
            toId = toFirstId;
            socket = firstSocket;
        }

        sendMessage(message, socket, toId);
    }

    public void updateAfterSwitch()
    {
        ArrayList<PointElement> elements = BoardChecker.getAll(board, rules);

        game.addPointElements(elements, board);

        board.deleteElements(elements);

        updaterAfterPoints();
    }

    public void updaterAfterPoints()
    {
        ArrayList<BoardElement> fallingElements = board.moveElementsFromGravity();

        ArrayList<BoardElement> elements = board.recreateElements();
        RecreateElementsMessage msg = new RecreateElementsMessage(elements);
        sendMessageToBoth(msg);
        Logger.logInfo(LogKey, "Send recreating elements: " + msg.toString());
        Logger.logInfo(LogKey, "Board after recreating elements: " + board.toString());

        if (elements.size() == 0)
        {
            Logger.logInfo(LogKey, "End Switch");
            // end move
            game.endSwitch();

            // check if moves are possible
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

            if (moves.size() == 0)
            {
                // TODO: recreate board
                Logger.logInfo(LogKey, "Recreating board");
                board = Board.createBoardNoPoints(board.getNumberOfRows(), board.getNumberOfColumns(), rules);
                sendMessageToBoth(new RecreateBoardMessage(board));

            }

            // skills could have disabled it
            game.setStrikePossible(true);
        }
        else
        {
            updateAfterSwitch();
        }
    }

    private void sendMessageToBoth(Message msg)
    {
        sendMessageToFirst(msg);
        sendMessageToSecond(msg);
    }

    public void executeSkill(Skill s, long fromId)
    {
        SkillMessage msg = new SkillMessage(s.getName());
        msg.setPos(s.getPos());
        sendMessageToOther(msg, fromId);
        // TODO
        switch (s.getName())
        {
            case Skill.Help:
                if (s.isExecutable())
                {
                    s.pay();
                }
                else
                {
                    s.pay();
                    game.getPlayingPlayer().setPoints(game.getPlayingPlayer().getPoints() - game.getPointsLimit());
                }
                break;

            case Skill.Change:
                if (s.isExecutable())
                {
                    s.pay();
                }
                else
                {
                    s.pay();
                    game.getPlayingPlayer().setPoints(game.getPlayingPlayer().getPoints() - game.getPointsLimit());
                }

                // TODO: use own class for each skill
                Coords pos = s.getPos();
                board.getElement(pos).setValue(6);
                game.setStrikePossible(false);
                updateAfterSwitch();
                break;

        }

    }

    public void updateAfterFall()
    {
    }

    public void sendMessageToFirst(Message message)
    {
        sendMessage(message, firstSocket, toFirstId);
    }

    public void sendMessageToSecond(Message message)
    {
        sendMessage(message, secondSocket, toSecondId);
    }

    public void sendMessage(Message message, Socket s, long id)
    {
        try
        {
            message.setFromId(Message.ServerId);
            message.setToId(id);
            Logger.logInfo(LogKey, "Sending " + message.getName() + " from " + message.getFromId() + " to " + message.getToId());
            s.getOutputStream().write(message.toByte());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateAfterFall(ArrayList<BoardElement> elements)
    {
        // check board if there have to be created new elements
        board.recreateElements(elements);
        if (elements.size() == 0)
        {
            updateAfterSwitch();
        }
    }

    public void recreateBoard(Board board)
    {
        Logger.logInfo(LogKey, "Recreating Board");
        this.board = board;
    }

    public void next(long id)
    {
        Logger.logInfo(LogKey, "Next");
        sendMessageToOther(new NextMessage(), id);
        game.moveEnds();
    }

    public void changeGravity(Gravity gravity, long fromId)
    {
        board.setGravity(gravity);
        sendMessageToOther(new GravityMessage(gravity), fromId);
    }

    public void findOpponent(long id)
    {
        // TODO: What if two opponents have different version of game?
        //GameMatcher.findOpponent(id);
    }

    public long getFromId()
    {
        return fromId;
    }
}
