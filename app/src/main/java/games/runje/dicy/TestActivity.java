package games.runje.dicy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import games.runje.dicy.layouts.DicyProgress;

/**
 * Created by Thomas on 08.06.2015.
 */
public class TestActivity extends Activity
{
    private EditText editText;
    private DicyProgress dicyProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity);
        editText = (EditText) findViewById(R.id.editText);
        dicyProgress = (DicyProgress) findViewById(R.id.view);

    }

    public void clickButton(View v)
    {
        int progress = Integer.parseInt(editText.getText().toString());
        dicyProgress.setProgress(progress);
        dicyProgress.invalidate();
    }
}
