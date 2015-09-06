package games.runje.dicy.layouts.statisticsView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 20.08.2015.
 */
public class StatisticsFragmentPagerAdapter extends FragmentStatePagerAdapter
{
    List<Fragment> fragments;
    private RuleVariant ruleVariant;

    public StatisticsFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);

        fragments = new ArrayList<>();

        fragments.add(new GamesStatisticFragment());

        Fragment f1 = new MovePointStatisticFragment();
        fragments.add(f1);

        Fragment f2 = new SwitchPointStatisticFragment();
        fragments.add(f2);

        Fragment f3 = new ShortPointStatisticFragment();
        fragments.add(f3);

        Fragment f4 = new NormalPointStatisticFragment();
        fragments.add(f4);

        Fragment f5 = new LongPointStatisticFragment();
        fragments.add(f5);
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)
        {
            case 0:
                return "GAMES";

            case 1:
                return "MOVE POINTS";

            case 2:
                return "SWITCH POINTS";

            case 3:
                return "SHORT GAME POINTS";

            case 4:
                return "NORMAL GAME POINTS";

            case 5:
                return "LONG GAME POINTS";

        }

        return super.getPageTitle(position);
    }

}
