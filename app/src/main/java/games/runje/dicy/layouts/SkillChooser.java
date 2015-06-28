package games.runje.dicy.layouts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicy.R;
import games.runje.dicy.animatedData.AnimatedBoardElement;
import games.runje.dicy.animatedData.animatedSkills.AnimatedSkill;
import games.runje.dicymodel.skills.Skill;

/**
 * Created by Thomas on 21.06.2015.
 */
public class SkillChooser
{
    private SimpleObserver handler;
    private String[] skills;
    private int lastSkillValue;
    private int[] skillValues;

    public SkillChooser(SimpleObserver handler, String[] skills, int[] skillValues)
    {
        this.handler = handler;
        // copy
        this.skills = new String[skills.length];
        for (int i = 0; i < skills.length; i++)
        {
            this.skills[i] = skills[i];
        }

        this.skillValues = skillValues;
    }

    public SkillChooser()
    {
        skills = new String[3];
        skillValues = new int[3];
    }

    public void showDialog(Activity activity, final int skillNumber)
    {
        lastSkillValue = skillValues[skillNumber];
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = activity.getLayoutInflater().inflate(R.layout.pick_skill_dialog, null);

        View skill = view.findViewById(R.id.skill);
        final ImageView skill_image = (ImageView) skill.findViewById(R.id.skill_image);
        final ImageView skill_value = (ImageView) skill.findViewById(R.id.skill_value);
        final TextView skill_name = (TextView) skill.findViewById(R.id.skill_name);

        skill_name.setText(skills[skillNumber]);
        skill_value.setImageResource(AnimatedBoardElement.valueToImageResource(skillValues[skillNumber]));
        skill_image.setImageResource(AnimatedSkill.getImageId(skills[skillNumber]));

        View help = view.findViewById(R.id.help);
        TextView text = (TextView) help.findViewById(R.id.skill_name);
        text.setText(Skill.Help);
        help.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                skill_name.setText(Skill.Help);
                skill_image.setImageResource(AnimatedSkill.getImageId(Skill.Help));

            }
        });

        View shuffle = view.findViewById(R.id.shuffle);
        text = (TextView) shuffle.findViewById(R.id.skill_name);
        text.setText(Skill.Shuffle);
        shuffle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                skill_name.setText(Skill.Shuffle);
                skill_image.setImageResource(AnimatedSkill.getImageId(Skill.Shuffle));
            }
        });

        View change = view.findViewById(R.id.change);
        text = (TextView) change.findViewById(R.id.skill_name);
        text.setText(Skill.Change);
        change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                skill_name.setText(Skill.Change);
                skill_image.setImageResource(AnimatedSkill.getImageId(Skill.Change));
            }
        });

        int[] diceIds = new int[] { R.id.image_one, R.id.image_two, R.id.image_three, R.id.image_four, R.id.image_five, R.id.image_six};
        for (int i = 0; i < 6; i++)
        {
            View dice = view.findViewById(diceIds[i]);
            final int finalI = i;
            dice.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    skill_value.setImageResource(AnimatedBoardElement.valueToImageResource(finalI + 1));
                    skillValues[skillNumber] = finalI + 1;
                }
            });

        }

        //final View view = getLayoutInflater().inflate(R.layout.pick_skill_dialog, null);
        builder.setView(view);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // User clicked OK button
                skills[skillNumber] = skill_name.getText().toString();
                handler.update();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // reset skill_game value
                skillValues[skillNumber] = lastSkillValue;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String getName(int i)
    {
        return skills[i];
    }

    public int getImageId(int i)
    {
        return AnimatedSkill.getImageId(skills[i]);
    }

    public int getLoadId(int i)
    {
        return AnimatedBoardElement.valueToImageResource(skillValues[i]);
    }

    public int getValue(int i)
    {
        return skillValues[i];
    }

    public void setName(String string, int i)
    {
        skills[i] = string;
    }

    public void setValue(int value, int i)
    {
        skillValues[i] = value;
    }

    public List<Skill> getSkills()
    {
        List<Skill> result = new ArrayList<>();
        for (int i = 0; i < skills.length; i++)
        {
            result.add(Skill.createSkill(skills[i], skillValues[i], 8));
        }

        return result;
    }
}
