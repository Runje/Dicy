package games.runje.dicymodel.communication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 17.02.2015.
 */
public class MessageReader
{

    public static String LogKey = "MessageReader";

    public static int readMessage(InputStream is, ByteBuffer buffer) throws IOException
    {
        read(is, buffer, 0, 4);

        int length = buffer.getInt();
        Logger.logDebug(LogKey, "totalLength: " + length);

        read(is, buffer, 4, length - 4);

        buffer.position(4);
        return length;
    }

    private static void read(InputStream is, ByteBuffer buffer, int offset, int length) throws IOException
    {
        int readSoFar = 0;
        while (readSoFar < length)
        {
            int bytesRead = is.read(buffer.array(), offset + readSoFar, length - readSoFar);
            if (bytesRead <= 0)
            {
                Logger.logInfo(LogKey, "No more bytes could be read");
                throw new IOException();
            }

            readSoFar += bytesRead;
        }
    }
}
