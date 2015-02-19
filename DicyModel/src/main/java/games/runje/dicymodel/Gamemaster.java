package games.runje.dicymodel;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.boardChecker.BoardChecker;
import games.runje.dicymodel.communication.Message;
import games.runje.dicymodel.communication.MessageParser;
import games.runje.dicymodel.communication.RecreateElementsMessage;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Move;
import games.runje.dicymodel.data.PointElement;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 16.02.2015.
 */
public class Gamemaster
{
    private Rules rules;
    private Board board;
    private Socket clientSocket;
    private Game game;

    public Gamemaster()
    {

    }

    public Gamemaster(Board b, Rules r, String[] player, Socket socket)
    {
        this.clientSocket = socket;
        this.board = b;
        this.rules = r;
        List<Strategy> s = new ArrayList<>();
        s.add(null);
        s.add(null);
        this.game = new LocalGame(20, 100, Arrays.asList(player), s);
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
        System.out.println("ReceiveMessage in Gamemaster");
        Message msg = MessageParser.parse(buffer, length);
        msg.execute(this);
    }

    public void startGame(Board board, Rules rules, String[] player)
    {
        // TODO: move to animatedGamemaster?
        System.out.println("Starting Game from Gamemaster");
    }


    public void switchElements(Coords first, Coords second)
    {
        board.switchElements(first, second);
        updateAfterSwitch();
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
        sendMessageToClient(msg);
        System.out.println("Send recreating elements: " + msg.toString());
        System.out.println("Board after recreating elements: " + board.toString());

        if (elements.size() == 0)
        {
            System.out.println("End Switch");
            // end move
            game.endSwitch();

            // check if moves are possible
            ArrayList<Move> moves = BoardChecker.getPossiblePointMoves(board, rules);

            if (moves.size() == 0)
            {
                // TODO: recreate board
            }

            // skills could have disabled it
            game.setStrikePossible(true);
        }
        else
        {
            updateAfterSwitch();
        }
    }

    public void updateAfterFall()
    {
    }

    public void sendMessageToClient(Message message)
    {
        try
        {
            clientSocket.getOutputStream().write(message.toByte());
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

}
