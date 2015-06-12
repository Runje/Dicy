package games.runje.dicy.controls;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicy.layouts.SkillLayout;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 27.04.2015.
 */
public class Controls implements GameControls
{
    private final Activity activity;
    private final ControlHandler handler;
    GameInfo gameInfo;
    List<PlayerLayout> playerLayouts;
    BoardLayout boardLayout;
    LocalGame game;

    boolean enabled;
    public static String LogKey = "GameControls";
    private boolean saveBoardEnabled;
    private boolean saveGameInfoEnabled;
    private boolean[] savePlayersEnabled = new boolean[2];

    @Override
    public void setSkillEnabled(Skill skill)
    {
        long id = game.getPlayingPlayer().getId();

        for (PlayerLayout layout : playerLayouts)
        {
            if (layout.getPlayer().getId() == id)
            {
                for (SkillLayout skillLayout : layout.getSkills())
                {
                    if (skillLayout.getName().equals(skill.getName()))
                    {
                        skillLayout.setEnabled(true);
                        Logger.logInfo(LogKey, "Setting Skill " + skill.getName() + " enabled");
                        return;
                    }
                }
            }
        }
    }

    public Controls(Activity activity, ControlHandler handler, LocalGame game)
    {
        this.activity = activity;
        this.handler = handler;
        this.game = game;
        this.playerLayouts = new ArrayList<>();
        RelativeLayout l = new RelativeLayout(activity);
        List<Player> players = game.getPlayers();
        PlayerLayout pl = new PlayerLayout(activity, players.get(0), R.drawable.blueyellowchip, R.id.player1, handler);
        this.playerLayouts.add(pl);

        PlayerLayout pl2 = new PlayerLayout(activity, players.get(1), R.drawable.bluewhitechip, R.id.player2, handler);
        this.playerLayouts.add(pl2);
        this.boardLayout = handler.getBoardLayout();
        gameInfo = new GameInfo(activity, handler, game);
        setEnabledControls(true);
    }

    @Override
    public void setPointLimit(int i)
    {
        gameInfo.setPointLimit(i);
    }

    @Override
    public void restore()
    {
        handler.setEnabledBoard(saveBoardEnabled);
        boardLayout.setEnabledGravity(saveBoardEnabled);

        gameInfo.setEnabled(saveGameInfoEnabled);

        for (PlayerLayout playerLayout : playerLayouts)
        {
            playerLayout.restore();
        }
    }

    @Override
    public void save()
    {
        // Board
        saveBoardEnabled = handler.isEnabledBoard();
        Logger.logInfo(LogKey, "Board is enabled: " + saveBoardEnabled);

        // GameInfo
        saveGameInfoEnabled = gameInfo.isEnabled();

        // Players
        for (PlayerLayout playerLayout : playerLayouts)
        {
            playerLayout.save();
        }

    }

    @Override
    public void setEnabledControls(boolean enabled)
    {
        if (game.isGameOver() && enabled)
        {
            AnimatedLogger.logInfo(LogKey, "Game Over");
            enabled = false;
            return;
        }

        boolean aiEnabled = game.hasAIPlayerTurn() && enabled;

        this.enabled = enabled;
        if (aiEnabled)
        {
            enabled = false;
        }

        handler.setEnabledBoard(enabled);
        boardLayout.setEnabledGravity(enabled);
        gameInfo.setEnabled(enabled);
        for (PlayerLayout playerLayout : playerLayouts)
        {
            playerLayout.setEnabled(enabled && game.hasTurn(playerLayout.getPlayer()));
        }
    }

    private void gameOver()
    {
        if (game.isGameOver())
        {
            setEnabledControls(false);
            FinishedDialog d = new FinishedDialog();
            d.setContext(activity);
            d.setName(game.getWinner());
            AnimatedLogger.logDebug(LogKey, "Before show");
            d.show(activity.getFragmentManager(), "Game is finished");
            AnimatedLogger.logDebug(LogKey, "After show");
        }
    }

    @Override
    public boolean areControlsEnabled()
    {
        return enabled;
    }

    @Override
    public void update()
    {
        gameInfo.update();
        boardLayout.updateGravity();
        for (PlayerLayout playerLayout : playerLayouts)
        {
            playerLayout.updatePlayer(game);
            playerLayout.updateSkills();
        }

        gameOver();
    }
}
