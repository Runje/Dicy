package games.runje.dicy.controls;

import android.app.Activity;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.layouts.BoardLayout;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;

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
    private String LogKey = "GameControls";

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
    public void setEnabledControls(boolean enabled)
    {
        if (game.isGameOver() && enabled)
        {
            AnimatedLogger.logInfo(LogKey, "Game Over");
            return;
        }

        if (game.hasAIPlayerTurn() && enabled)
        {
            handler.setEnabledBoard(false);
            setEnabledControls(false);
            AnimatedLogger.logInfo(LogKey, "Disabling controls for " + game.getPlayingPlayer().getName() + ":" + game.getPlayingPlayer().getId());
            return;
        }

        this.enabled = enabled;
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
            AnimatedLogger.logInfo(LogKey, "Before show");
            d.show(activity.getFragmentManager(), "Game is finished");
            AnimatedLogger.logInfo(LogKey, "After show");
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
