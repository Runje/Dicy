package games.runje.dicy.controls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import games.runje.dicy.OptionActivity;
import games.runje.dicy.StartActivity;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicy.statistics.SQLiteHandler;
import games.runje.dicy.statistics.StatisticManager;

/**
 * Created by Thomas on 18.01.2015.
 */
public class FinishedDialog extends DialogFragment
{

    private String name = "Unknown";

    private Context context;

    public FinishedDialog()
    {

    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AnimatedLogger.logInfo("Finished", "Creating dialog");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        StatisticManager manager = new SQLiteHandler(context);
        List<PlayerStatistic> players = manager.getAllPlayers();
        String statistics = "";
        for (PlayerStatistic playerStatistic: players)
        {
            statistics += playerStatistic + "\n";
        }

        builder.setMessage(statistics + name + " wins the game.")
                .setPositiveButton("Again", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent = new Intent(((Dialog) dialog).getContext(), OptionActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Back to Menu", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent = new Intent(((Dialog) dialog).getContext(), StartActivity.class);
                        startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}