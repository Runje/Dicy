package games.runje.dicymodel;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.04.2015.
 */
public interface ClientGamemaster
{
    void receiveMessage(ByteBuffer buffer, int length);

    void startGame(Board board, Rules rules, LocalGame game);

    void switchElementsFromHost(Coords first, Coords second);

    void nextFromHost();

    void changeGravityFromHost(Gravity gravity);

    void startRecreateAnimation(ArrayList<BoardElement> elements);

    void recreateBoard(Board board);

    void executeSkillFromHost(Skill s);
}
