package games.runje.dicy.controller;

/**
 * Created by thomas on 08.02.15.
 */
public class HelpAction extends Action
{
    @Override
    public void execute()
    {
        Gamemaster.getInstance().help();
    }

    @Override
    public boolean isPossible()
    {
        // TODO: Only if loaded or it costs
        return true;
    }
}
