package games.runje.dicymodel.communication;

import games.runje.dicymodel.communication.messages.Message;

/**
 * Created by Thomas on 26.02.2015.
 */
public interface IServer
{
    public void sendMessage(Message msg, long toId);
}
