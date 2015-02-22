package games.runje.dicymodel.communication;

import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by thomas on 22.02.15.
 */
public class SkillMessage extends Message
{
    public static final String Name = "SkillMessage";
    private String skill;
    private Coords pos;

    public SkillMessage(String skillName)
    {
        this(skillName, new Coords(0,0));
    }

    public SkillMessage(ByteBuffer buffer, int length)
    {
        this.length = length;
        this.skill = MessageConverter.byteToString(buffer, MessageConverter.skillLength);
        this.pos = MessageConverter.byteToCoords(buffer);
    }

    public SkillMessage(String skillName, Coords position)
    {
        this.length = MessageConverter.coordsLength + MessageConverter.skillLength + MessageConverter.sizeLength + MessageConverter.nameLength;
        this.skill = skillName;
        this.pos = position;
    }

    @Override
    public String getName()
    {
        return Name;
    }

    @Override
    public void execute(Gamemaster gamemaster)
    {
        Skill s = gamemaster.getGame().getPlayingPlayer().getSkill(skill);
        s.setPos(pos);
        System.out.println("Pos: " + pos);
        gamemaster.executeSkill(s);
    }

    @Override
    public byte[] toByte()
    {
        // TODO: make method in base class because first two lines are uses in each sub class
        ByteBuffer buffer = ByteBuffer.allocate(length);
        buffer.put(lengthAndNameToByte());
        buffer.put(MessageConverter.stringToByte(skill));
        // TODO: fill with zeros in new stringToByte(skill, length)
        MessageConverter.fillBufferWithZero(buffer, MessageConverter.skillLength - skill.length());
        buffer.put(MessageConverter.coordsToByte(pos));
        System.out.println("Writing pos: " + pos);
        return buffer.array();
    }
}
