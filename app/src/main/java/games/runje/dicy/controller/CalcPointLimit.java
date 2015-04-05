package games.runje.dicy.controller;

import android.os.AsyncTask;

import games.runje.dicymodel.GameControls;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;
import games.runje.dicymodel.data.Board;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 26.03.2015.
 */
public class CalcPointLimit extends AsyncTask<Void, Void, Void>
{
    private Board board;
    private Rules rules;
    private GameControls gameControls;
    private LocalGame game;

    public CalcPointLimit(Board b, Rules r, GameControls controls, LocalGame game)
    {
        this.board = b;
        this.rules = r;
        this.gameControls = controls;
        this.game = game;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        rules.setPointLimit(Simulator.getLimit(rules, board));
        AnimatedLogger.logDebug("CalcPointLimit", "Calculated Limit: " + rules.getPointLimit());

        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        game.setPointsLimit(rules.getPointLimit());
        rules.setPointLimitSetManually(true);
        gameControls.update();
        gameControls.startGame();
    }

}
