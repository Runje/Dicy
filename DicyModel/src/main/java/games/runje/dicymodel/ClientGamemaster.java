package games.runje.dicymodel;

import java.util.ArrayList;

import games.runje.dicymodel.communication.messages.Message;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.data.BoardElement;
import games.runje.dicymodel.data.Coords;
import games.runje.dicymodel.data.Gravity;
import games.runje.dicymodel.game.Game;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 09.04.2015.
 */
public interface ClientGamemaster
{
    void startGame(Board board, Rules rules, LocalGame game);

    void switchElementsFromHost(Coords first, Coords second);

    void nextFromHost();

    void changeGravityFromHost(Gravity gravity);

    void startRecreateAnimation(ArrayList<BoardElement> elements);

    void recreateBoard(Board board);

    void executeFirstSkillMessageFromHost(Skill s);

    void executeSecondSkillMessageFromHost(Skill s);

    void receiveMessage(Message msg);

    Game getGame();
}
