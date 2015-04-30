package games.runje.dicymodel;

/**
 * Created by Thomas on 16.03.2015.
 */
public interface GameControls
{
    void setEnabledControls(boolean enabled);

    boolean areControlsEnabled();

    void update();
}
