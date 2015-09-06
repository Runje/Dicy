package games.runje.dicy.layouts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.OptionActivity;
import games.runje.dicy.R;
import games.runje.dicy.statistics.PlayerStatistic;
import games.runje.dicymodel.Logger;
import games.runje.dicymodel.Utilities;
import games.runje.dicymodel.ai.Strategy;

/**
 * Created by Thomas on 18.06.2015.
 */
public class NamesArrayAdapter extends ArrayAdapter<PlayerStatistic>
{
    private final List<PlayerStatistic> players;
    private int selectedOther;

    public NamesArrayAdapter(OptionActivity context, List<PlayerStatistic> players, int selectedOther)
    {
        super(context, R.layout.player_spinner_item, players);
        this.players = new ArrayList<>();
        for (PlayerStatistic p:players)
        {
            this.players.add(p);
        }

        this.selectedOther = selectedOther;
    }

    public int getSelectedOther()
    {
        return selectedOther;
    }

    public void setSelectedOther(int other)
    {
        Logger.logInfo("Names", "Set selcted other " + other);
        selectedOther = other;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }
    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    @Override
    public boolean isEnabled(int position)
    {
        return position != selectedOther;
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        View mySpinner = inflater.inflate(R.layout.player_spinner_item, parent,
                false);
        TextView main_text = (TextView) mySpinner
                .findViewById(R.id.textView);
        PlayerStatistic player = players.get(position);


        if (players.get(position).getStrategy().equals(Strategy.Human))
        {
            ImageView left_icon = (ImageView) mySpinner
                    .findViewById(R.id.imageView);

            left_icon.setVisibility(View.GONE);
            main_text.setText(players.get(position).getName());
        } else
        {
            Strategy strategy = Strategy.getStrategy(player.getStrategy());
            main_text.setText(players.get(position).getName() + " (" + Utilities.doubleToString(strategy.getValue(), 2) + ")");
        }


        return mySpinner;
    }

    public void addPlayer(PlayerStatistic player)
    {
        players.add(player);
        add(player);
        notifyDataSetChanged();
    }
}
