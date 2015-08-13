package games.runje.dicy.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import games.runje.dicy.R;
import games.runje.dicy.layouts.DicyProgress;
import games.runje.dicy.layouts.PlayerLayout;
import games.runje.dicy.layouts.PointList;
import games.runje.dicymodel.Rules;
import games.runje.dicymodel.game.LocalGame;

/**
 * Created by Thomas on 27.04.2015.
 */
public class GameInfo
{
    private final ImageView nextButton;
    private final TextView movePointsText;
    private final Activity activity;
    private final ControlHandler handler;
    private final LocalGame game;
    private final DicyProgress progress;
    private final TextView countdown;
    private boolean enabled;
    private String LogKey = "GameInfo";

    public GameInfo(Activity activity, final ControlHandler handler, LocalGame game)
    {
        this.game = game;
        this.activity = activity;
        this.handler = handler;
        TextView limit = (TextView) activity.findViewById(R.id.goal_text);
        limit.setText("" + game.getGameEndPoints());

        movePointsText = (TextView) activity.findViewById(R.id.switchPointsText);
        movePointsText.setText("0\\" + game.getPointsLimit());

        nextButton = (ImageView) activity.findViewById(R.id.image_next);
        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                handler.nextFromUser();
            }
        });
        //nextButton.setColorFilter(R.color.dicy_yellow);

        initPointListButton();

        progress = (DicyProgress) activity.findViewById(R.id.dicy_progress);
        progress.setMaxProgress(game.getPointsLimit());

        countdown = (TextView) activity.findViewById(R.id.text_countdown);
        Rules rules = handler.getRules();
        if (rules.isTimeLimit())
        {
            countdown.setText(Integer.toString(rules.getTimeLimitInS()));
            updateCountdown();
        } else
        {
            countdown.setVisibility(View.GONE);
        }
    }

    public static Dialog createPointListDialog(Context context, Rules rules, final Runnable runnable)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Points")
                .setTitle("List").setView(PointList.createPointList(context, rules));
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if (runnable != null)
                {
                    runnable.run();
                }
            }
        });

        return builder.create();
    }

    public void updateCountdown()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!game.isFinishedOrCancelled())
                {
                    final long time = (new Date().getTime() - game.getPlayerIsPlayingSince().getTime()) / 1000;
                    long timeLimit = handler.getRules().getTimeLimitInS();
                    final long cdown = timeLimit - time;
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (cdown >= 0)
                            {
                                countdown.setText(Integer.toString((int) cdown));

                                if (cdown <= 10)
                                {
                                    Animation animation = AnimationUtils.loadAnimation(activity, R.anim.blink);
                                    countdown.startAnimation(animation);
                                    countdown.setTextColor(Color.RED);
                                } else
                                {
                                    countdown.clearAnimation();
                                    countdown.setTextColor(Color.YELLOW);
                                }

                                countdown.invalidate();
                            }
                        }
                    });
                    if (time > handler.getRules().getTimeLimitInS())
                    {
                        handler.timeOut();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void initPointListButton()
    {
        ImageView b = (ImageView) activity.findViewById(R.id.pointList);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                createPointListDialog(activity, handler.getRules(), null).show();
            }
        });
    }

    public void update()
    {

        int pointsLimit = game.getPointsLimit();
        String sLimit = Integer.toString(pointsLimit);

        this.movePointsText.setText("" + Integer.toString(game.getSwitchPoints()) + "\\" + sLimit);

        progress.setProgress(game.getSwitchPoints());

        if (game.getSwitchPoints() >= game.getPointsLimit())
        {
            movePointsText.setTextColor(Color.parseColor(PlayerLayout.HtmlBlue));
        }
        else
        {
            movePointsText.setTextColor(Color.parseColor("#610B0B"));
        }
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        // only allow next if its not the first move
        this.nextButton.setEnabled(enabled && game.getMovePoints() > 0);
    }

    public void setPointLimit(int i)
    {
        progress.setMaxProgress(i);
        progress.postInvalidate();
    }
}
