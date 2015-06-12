package games.runje.dicy.layouts;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.controller.AnimatedLogger;
import games.runje.dicy.controls.ControlHandler;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 03.04.2015.
 */
public class SkillLayout extends LinearLayout
{
    private TextView loadText;
    private FrameLayout clickArea;
    private ImageView image;
    private Skill skill;
    public static String LogKey = "SkillLayout";
    private boolean saveEnabled;

    public SkillLayout(Context context, final Skill skill, final ControlHandler handler, View container)
    {
        super(context);
        this.skill = skill;
        container.setVisibility(View.VISIBLE);
        this.clickArea = (FrameLayout) container.findViewById(R.id.skill_clickarea);
        this.image = (ImageView) container.findViewById(R.id.skill_image);
        image.setImageResource(skill.getImageId());

        ImageView loadImage = (ImageView) container.findViewById(R.id.skill_value);
        loadImage.setImageResource(AnimatedBoardElement.valueToImageResource(skill.getLoadValue()));

        TextView text = (TextView) container.findViewById(R.id.skill_name);
        text.setText(skill.getName());

        this.loadText = (TextView) container.findViewById(R.id.skill_load);
        loadText.setText(skill.getCurrentLoad() + "/" + skill.getMaxLoad());
        clickArea.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                handler.executeSkillFromUser(skill);
            }
        });

    }


    @Override
    public void setEnabled(boolean enable)
    {
        super.setEnabled(enable);

        enable = enable && skill.isExecutable();
        clickArea.setEnabled(enable);

        float alpha = 0;
        if (enable)
        {
            alpha = 1;
        }
        else
        {
            alpha = 0.5f;
        }

        image.setAlpha(alpha);
    }

    public void update()
    {
        AnimatedLogger.logDebug(LogKey, "Updating skill: " + skill.getName());
        loadText.setText(skill.getCurrentLoad() + "/" + skill.getMaxLoad());
    }


    public String getName()
    {
        return skill.getName();
    }

    public void save()
    {
        saveEnabled = isEnabled();
    }

    public void restore()
    {
        setEnabled(saveEnabled);
    }
}
