package games.runje.dicy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import games.runje.dicy.layouts.statisticsView.StatisticsFragmentPagerAdapter;
import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 18.08.2015.
 */
public class StatisticsActivity extends FragmentActivity
{
    StatisticsFragmentPagerAdapter adapter;
    ViewPager pager;
    private String LogKey = "StatisticsActivity";

    @Override
    protected void onResume()
    {
        super.onResume();
        //adapter.onResume();
        Logger.logInfo(LogKey, "On Resume");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.statistics_page_viewer);


        adapter = new StatisticsFragmentPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.statistics_pager);
        pager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
