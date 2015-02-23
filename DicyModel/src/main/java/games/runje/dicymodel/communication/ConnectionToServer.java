package games.runje.dicymodel.communication;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import games.runje.dicymodel.Gamemaster;

/**
 * Created by Thomas on 11.02.2015.
 */
public class ConnectionToServer
{

    public static final int port = 4400;
    //public static final String ipaddress = "runje.ddns.net";
    //public static final String ipaddress = "192.168.2.240";
    public static final String ipaddress = "192.168.1.4";
    private static boolean firstTime = true;
    private static Socket s;
    private static boolean connected = false;
    private static Gamemaster gamemaster;

    public static boolean isConnected()
    {
        return connected;
    }

    public static void connect(Gamemaster gm)
    {
        gamemaster = gm;
        if (connected)
        {
            return;
        }

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                connectAndListen();
            }
        }).start();
    }


    private static void connectAndListen()
    {
        try
        {
            s = new Socket(ipaddress, port);
            connected = true;
            System.out.println("Connected");
        }
        catch (IOException e)
        {
            connected = false;
            e.printStackTrace();
        }

        try
        {
            InputStream is = s.getInputStream();
            //Get message from the server
            InputStreamReader isr = new InputStreamReader(is);

            while (true)
            {
                ByteBuffer buffer = ByteBuffer.allocate(2000);
                int length = MessageReader.readMessage(is, buffer);
                gamemaster.receiveMessage(buffer, length);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            System.out.println("Stop listening");
            connected = false;
        }

        connected = false;

    }

    public static void sendMessage(Message message)
    {
        byte[] bytes = message.toByte();
        try
        {
            s.getOutputStream().write(bytes);
            System.out.println("Sent messsage: " + message.getName() + ", length: " + bytes.length);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(final String msg)
    {
       /* try {
            //Send the message to the server
            OutputStream os = s.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            String sendMessageToClient = msg + "\n";
            bw.write(sendMessageToClient);
            bw.flush();
            System.out.println("Message sent to the server : " + sendMessageToClient);
        } catch (Exception exception) {
            exception.printStackTrace();
        }*/
    }
}
