package games.runje.dicymodel.communication;

import games.runje.dicymodel.communication.messages.Message;

/**
 * Created by Thomas on 30.04.2015.
 */
public interface MessageHandler
{
    void handleMessage(Message msg);
}
