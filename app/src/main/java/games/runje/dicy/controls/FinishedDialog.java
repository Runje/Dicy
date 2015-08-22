package games.runje.dicy.controls;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import games.runje.dicy.StartActivity;
import games.runje.dicy.StatisticsActivity;
import games.runje.dicy.controller.AnimatedLogger;

/**
 * Created by Thomas on 18.01.2015.
 */
public class FinishedDialog extends DialogFragment
{

    // TODO: back to menu on stop/pause
    private String name = "Unknown";

    public FinishedDialog()
    {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(name + " wins the game.")
                .setPositiveButton("Show Statistics", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent intent = new Intent(((Dialog) dialog).getContext(), StatisticsActivity.class);
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