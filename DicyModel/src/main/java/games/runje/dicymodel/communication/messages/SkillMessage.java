package games.runje.dicymodel.communication.messages;

import java.nio.ByteBuffer;

import games.runje.dicymodel.ClientGamemaster;
import games.runje.dicymodel.HostGamemaster;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.communication.MessageConverter;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by thomas on 22.02.15.
 */
public class SkillMessage extends Message
{
    public static final String Name = "SkillMessage";
    private final String LogKey = Name;
    private String skill;
    private Coords pos;

    public SkillMessage(String skillName)
    {
        this(skillName, new Coords(0, 0));
    }

    public SkillMessage(ByteBuffer buffer, int length)
    {
        this.contentLength = length - headerLength;
        this.skill = MessageConverter.byteToString(buffer, MessageConverter.skillNameLength);
        this.pos = MessageConverter.byteToCoords(buffer);
        Logger.logDebug(LogKey, "Pos in Skill: " + pos);
    }

    public SkillMessage(String skillName, Coords position)
    {
        this.contentLength = MessageConverter.coordsLength + MessageConverter.skillNameLength;
        this.skill = skillName;
        this.pos = position;
    }

    public SkillMessage(Skill skill)
    {
        this(skill.getName(), skill.getPos());
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
        Skill s = gamemaster.getGame().getPlayingPlayer().getSkill(skill);
        s.setPos(pos);
        Logger.logDebug(LogKey, "Pos: " + pos);
        gamemaster.executeSkill(s, fromId);
    }

    @Override
    public void executeAtClient(ClientGamemaster gamemaster)
    {
        // TODO
        Skill s = new Skill(1, 1, skill, 0);
        s.setPos(pos);
        Logger.logDebug(LogKey, "Pos: " + pos);
        gamemaster.executeSkillFromHost(s);
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
        return buffer.array();
    }
}
