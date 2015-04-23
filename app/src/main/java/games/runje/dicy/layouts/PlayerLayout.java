package games.runje.dicy.layouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.animatedSkills.AnimatedHelpSkill;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controller.GamemasterAnimated;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.LocalGame;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 23.03.2015.
 */
public class PlayerLayout extends RelativeLayout
{
    public static String HtmlGreen = "#33CA15";
    public static String HtmlWhite = "#ffffff";
    public static String HtmlBlue = "#155DCA";
    private final View container;
    ImageView image;
    TextView name;
    TextView strikes;
    TextView points;
    View skillView;
    List<SkillLayout> skills = new ArrayList<>();
    GamemasterAnimated gm;
    private Player player;
    private String LogKey = "PlayerLayout";

    public PlayerLayout(Context context, Player player, int imageId, GamemasterAnimated gm, int containerId)
    {
        super(context);
        this.gm = gm;
        this.player = player;
        this.container = gm.getActivity().findViewById(containerId);
        //makeBorder();
        image = (ImageView) container.findViewById(R.id.player_icon);
        image.setImageResource(imageId);

        name = (TextView) container.findViewById(R.id.player_name);
        name.setText(player.getName());

        points = (TextView) container.findViewById(R.id.player_points);
        points.setText(Integer.toString(player.getPoints()));

        strikes = (TextView) container.findViewById(R.id.player_strike);

        this.skillView = skills();

    }

    public Player getPlayer()
    {
        return player;
    }

    public void startTurnAnimation()
    {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        image.startAnimation(animation);
    }

    public void stopTurnAnimation()
    {
        image.clearAnimation();
    }

    private void makeBorder()
    {
        ShapeDrawable rectShapeDrawable = new ShapeDrawable(); // pre defined class

        // get paint
        Paint paint = rectShapeDrawable.getPaint();

        // set border color, stroke and stroke width
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5); // you can change the value of 5
        setBackground(rectShapeDrawable);
    }

    public void updatePlayer(Player player, boolean isPlaying, LocalGame game)
    {

        String pointColor = "";
        if (game.areMostPoints(player.getPoints()))
        {
            pointColor = HtmlBlue;
            if (player.getPoints() > game.getGameEndPoints())
            {
                pointColor = HtmlGreen;
            }
        }
        else
        {
            pointColor = HtmlWhite;
        }

        String pointString = "<font color=" + pointColor + ">" + Integer.toString(player.getPoints()) + "</font>";

        String playPoints = "";
        if (isPlaying)
        {
            startTurnAnimation();
            name.setTextColor(Color.RED);
            String color = "";
            int sumPoints = game.getMovePoints() + player.getPoints() + game.getSwitchPoints();
            if (game.areMostPoints(sumPoints))
            {
                color = HtmlBlue;
                if (sumPoints > game.getGameEndPoints())
                {
                    color = HtmlGreen;
                }
            }
            else
            {
                color = HtmlWhite;
            }

            playPoints = " <font color=" + color + ">(" + sumPoints + ")</font>";
        }
        else
        {
            name.setTextColor(Color.GRAY);
            stopTurnAnimation();
        }
        points.setText(Html.fromHtml(pointString + playPoints));

        strikes.setText(strikesToString(player.getStrikes()));
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        for (SkillLayout skill : skills)
        {
            skill.setEnabled(enabled);
        }

    }

    private String strikesToString(int strikes)
    {
        if (strikes == 1)
        {
            return "X";
        }

        if (strikes == 2)
        {
            return "X X";
        }

        if (strikes == 3)
        {
            return "X X X";
        }

        return "";
    }

    public void updateSkills()
    {
        for (Skill s : this.player.getSkills())
        {
            for (SkillLayout sl : skills)
            {
                if (s.getName().equals(sl.getName()))
                {
                    sl.update();
                }
            }
        }
    }

    private View skills()
    {
        LinearLayout layout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setLayoutDirection(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);
        View[] containers = {container.findViewById(R.id.skill1), container.findViewById(R.id.skill2), container.findViewById(R.id.skill3)};
        for (int i = 0; i < player.getSkills().size(); i++)
        {
            Skill skill = player.getSkills().get(i);
            AnimatedLogger.logInfo(LogKey, "Adding skill: " + skill.getName() + ", animatedHelp: " + (skill instanceof AnimatedHelpSkill));


            SkillLayout sl = new SkillLayout(getContext(), skill, gm, containers[i]);
            skills.add(sl);
            // TODO: make 2 classes for each skill (normal skill and animated skill) with waitMethod and pay method
        }

        containers[2].setVisibility(View.GONE);
        return layout;
    }
}
