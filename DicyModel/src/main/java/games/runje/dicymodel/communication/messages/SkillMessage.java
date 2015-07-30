package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.HostGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by thomas on 22.02.15.
 */
public class SkillMessage extends Message
{
    public static final String Name = "SkillMessage";
    private final String LogKey = Name;
    private ArrayList<ArrayList<BoardElement>> newBoard;
    private String skill;
    private int index;
    private Coords pos;
    private boolean firstMessage;

    public SkillMessage(String skillName, int index, boolean firstMessage)
    {
        this(skillName, new Coords(0, 0), index, firstMessage);
    }

    public SkillMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        this.skill = MessageConverter.byteToString(buffer, MessageConverter.skillNameLength);
        this.pos = MessageConverter.byteToCoords(buffer);
        this.index = buffer.getInt();
        this.firstMessage = MessageConverter.byteToBoolean(buffer.get());
        Board b = MessageConverter.byteToBoard(buffer);
        this.newBoard = b.getBoard();
    }

    public SkillMessage(String skillName, Coords position, int index, boolean firstMessage)
    {
        this.contentLength = MessageConverter.coordsLength + MessageConverter.skillNameLength + 4 + 1 + MessageConverter.boardLength;
        this.skill = skillName;
        this.pos = position;
        this.index = index;
        this.firstMessage = firstMessage;
    }

    public SkillMessage(Skill skill, int index, boolean firstMessage)
    {
        this(skill.getName(), skill.getPos(), index, firstMessage);
    }

    public SkillMessage(Skill s, int index, boolean firstMessage, ArrayList<ArrayList<BoardElement>> board)
    {
        this(s, index, firstMessage);
        this.newBoard = board;
    }

    public void setPos(Coords pos)
    {
        this.pos = pos;
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void executeAtHost(HostGamemaster gamemaster)
    {
        Skill s = gamemaster.getGame().getPlayingPlayer().getSkills().get(index);
        s.setPos(pos);
        if (s.getName().equals(Skill.Shuffle))
        {
            ShuffleSkill shuffleSkill = (ShuffleSkill) s;
            shuffleSkill.setNewBoard(newBoard);
        }

        if ( firstMessage)
        {
            gamemaster.executeFirstSkillMessage(s, fromId);
        }
        else
        {
            gamemaster.executeSecondSkillMessage(s, fromId);
        }

    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        Skill s = gamemaster.getGame().getPlayingPlayer().getSkills().get(index);
        s.setPos(pos);

        if (s.getName().equals(Skill.Shuffle))
        {
            ShuffleSkill shuffleSkill = (ShuffleSkill) s;
            shuffleSkill.setNewBoard(newBoard);
        }

        if ( firstMessage)
        {
            gamemaster.executeFirstSkillMessageFromHost(s);
        }
        else
        {
            gamemaster.executeSecondSkillMessageFromHost(s);
        }

    }

    public byte[] contentToByte()
    {
        ByteBuffer buffer = ByteBuffer.allocate(contentLength);
        buffer.put(MessageConverter.stringToByte(skill, MessageConverter.skillNameLength));
        if (pos == null)
        {
            pos = new Coords(-1, -1);
        }
        buffer.put(MessageConverter.coordsToByte(pos));
        buffer.putInt(index);
        buffer.put(MessageConverter.booleanToByte(firstMessage));
        Board b = new Board(5,5);
        if (newBoard != null)
        {
            b = new Board(newBoard.size(), newBoard.get(0).size());
            b.setBoard(newBoard);

        }
        buffer.put(MessageConverter.boardToByte(b));
        return buffer.array();
    }
}
