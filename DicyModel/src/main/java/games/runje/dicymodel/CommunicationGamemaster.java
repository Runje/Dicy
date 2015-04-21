package games.runje.dicymodel;

import games.runje.dicymodel.data.Coords;

/**
 * Created by Thomas on 10.04.2015.
 */
public interface CommunicationGamemaster
{
    public void switchElements(Coords first, Coords second, long fromId);
}
