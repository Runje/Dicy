package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.LocalGameActivity;
import games.runje.dicy.OptionActivity;
import games.runje.dicy.R;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;
import games.runje.dicy.util.ActivityUtilities;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.GameMode;
import games.runje.dicymodel.game.RuleVariant;
import games.runje.dicymodel.game.Tourney;
import games.runje.dicymodel.game.TourneyGame;
import games.runje.dicymodel.skills.ChangeSkill;
import games.runje.dicymodel.skills.HelpSkill;
import games.runje.dicymodel.skills.ShuffleSkill;
import games.runje.dicymodel.skills.Skill;

import static games.runje.dicy.util.ActivityUtilities.playerStatisticsToPlayer;

/**
 * Created by Thomas on 16.11.2015.
 */
public class TourneyActivity extends Activity
{
    private Tourney tourney;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.tourney_plan);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        List<Player> players = new ArrayList<>();
        players.add(getPlayer("Franz"));
        players.add(getPlayer("Thomas"));
        players.add(getPlayer("Victor"));
        players.add(getPlayer("Emil"));
        players.add(getPlayer("Max"));
        players.add(getPlayer("Horst"));
        players.add(getPlayer("Cassandra"));
        players.add(getPlayer("Maryam"));
        tourney = new Tourney(players);

        updateTourneyPlan();
    }

    private Player getPlayer(String name)
    {
        List<Skill> skills1 = new ArrayList<Skill>();
        skills1.add(new ChangeSkill(1, 6));
        skills1.add(new HelpSkill(6, 6));
        skills1.add(new ShuffleSkill(5, 6));
        return ActivityUtilities.playerStatisticsToPlayer(new SQLiteHandler(this).getPlayer(name), skills1);
    }

    private void updateTourneyPlan()
    {
        Integer[] ids = { R.id.game1, R.id.game2, R.id.game3, R.id.game4};

        for (int i = 0; i < 4; i++)
        {
            View gameLayout = findViewById(ids[i]);
            updateGame(gameLayout, tourney.getRoundPlan().get(i));
        }

    }

    private void updateGame(View gameLayout, TourneyGame tourneyGame)
    {
        TextView player1 = (TextView) gameLayout.findViewById(R.id.player1).findViewById(R.id.text_name);
        player1.setText(tourneyGame.getPlayer1().getName());

        TextView player2 = (TextView) gameLayout.findViewById(R.id.player2).findViewById(R.id.text_name);
        player2.setText(tourneyGame.getPlayer2().getName());
    }

    public void clickPlay(View v)
    {
        Rules rules = Rules.makeRules(RuleVariant.Atlantic_City);
        ActivityUtilities.startGame(this, getPlayingPlayers(), rules, GameMode.Tourney);
    }

    private List<Player> getPlayingPlayers()
    {
        Player[] players = new Player[] {tourney.getNextHumanGame().getPlayer1(), tourney.getNextHumanGame().getPlayer2() };
        List<Player> playerList = new ArrayList<>();
        StatisticManager manager = new SQLiteHandler(this);

        PlayerStatistic player = manager.getPlayer(players[0].getName());

        List<Skill> skills1 = new ArrayList<Skill>();
        skills1.add(new ChangeSkill(1, 6));
        skills1.add(new HelpSkill(6, 6));
        skills1.add(new ShuffleSkill(5, 6));
        playerList.add(playerStatisticsToPlayer(player, skills1));

        PlayerStatistic player2 = manager.getPlayer(players[1].getName());

        List<Skill> skills2 = new ArrayList<Skill>();
        skills2.add(new ChangeSkill(1, 6));
        skills2.add(new HelpSkill(6, 6));
        skills2.add(new ShuffleSkill(5, 6));
        playerList.add(playerStatisticsToPlayer(player2, skills2));
        return playerList;
    }
}