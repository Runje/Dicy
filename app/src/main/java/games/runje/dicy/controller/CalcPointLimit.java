package games.runje.dicy.controller;

import android.os.AsyncTask;

import games.runje.dicymodel.Rules;
import games.runje.dicymodel.StopWatch;
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        rules.setPointLimit(Simulator.getLimit(rules));
        stopWatch.stop();
        AnimatedLogger.logDebug("CalcPointLimit", "Calculated Limit: " + rules.getPointLimit() + ", Time: " + stopWatch.getElapsedTimeSecs());

        return null;
    }

    @Override
    protected void onPostExecute(Void voids)
    {
        runnable.run();
    }

}
