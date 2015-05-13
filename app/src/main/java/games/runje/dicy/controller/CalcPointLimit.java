package games.runje.dicy.controller;

import android.os.AsyncTask;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.ai.Simulator;

/**
 * Created by Thomas on 26.03.2015.
 */
public class CalcPointLimit extends AsyncTask<Void, Void, Void>
{
    private Runnable runnable;
    private Rules rules;

    public CalcPointLimit(Rules r, Runnable runnable)
    {
        this.runnable = runnable;
        this.rules = r;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        rules.setPointLimit(Simulator.getLimit(rules));
        AnimatedLogger.logDebug("CalcPointLimit", "Calculated Limit: " + rules.getPointLimit());

        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        runnable.run();
    }

}
