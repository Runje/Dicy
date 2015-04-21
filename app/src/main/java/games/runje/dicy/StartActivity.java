package games.runje.dicy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
    }

    public void clickTestarena(View v)
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void clickOnlineGame(View v)
    {
        Intent intent = new Intent(this, OnlineGameActivity.class);
        startActivity(intent);
        //Toast.makeText(this, "Playing online is not implemented yet", Toast.LENGTH_LONG).show();
    }

    public void clickHighscore(View v)
    {
        Intent intent = new Intent(this, NewOptionActivity.class);
        startActivity(intent);
    }

    public void clickChip(View v)
    {
        /*Animation animation = null;
        switch (v.getId())
        {
            case R.id.dicyChip:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                break;
            case R.id.dicychip2:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                break;
            case R.id.dicychip3:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                break;
            case R.id.dicychip4:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                break;
            case R.id.dicychip5:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
                break;
            case R.id.dicychip6:
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                break;


        }


        v.startAnimation(animation);*/

    }
}
