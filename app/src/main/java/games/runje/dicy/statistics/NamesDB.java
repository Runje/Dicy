package games.runje.dicy.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 15.11.2015.
 */
public class NamesDB
{
    private static final String DB_PATH = "/data/data/games.runje.dicy/databases/";
    private static final String DB_NAME = "names";
    private static final String TABLE_NAMES = "names";
    private static String LogKey = "namesDB";
    private static Random random = new Random();

    public static void createDatabaseIfNotExists(Context context) throws IOException
    {
        boolean createDb = false;

        File dbDir = new File(DB_PATH);
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbDir.exists()) {
            dbDir.mkdir();
            createDb = true;
        }
        else if (!dbFile.exists()) {
            createDb = true;
        }
        else {
            // Check that we have the latest version of the db
            boolean doUpgrade = false;

            // Insert your own logic here on whether to upgrade the db; I personally
            // just store the db version # in a text file, but you can do whatever
            // you want.  I've tried MD5 hashing the db before, but that takes a while.

            // If we are doing an upgrade, basically we just delete the db then
            // flip the switch to create a new one
            if (doUpgrade) {
                dbFile.delete();
                createDb = true;
            }
        }

        if (createDb) {
            // Open your local db as the input stream
            InputStream myInput = context.getAssets().open(DB_NAME);

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(dbFile);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    public static SQLiteDatabase getStaticDb() {
        return SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }

    public static List<String> getNames()
    {
        SQLiteDatabase db = getStaticDb();
        ArrayList<String> names = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAMES;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                String name = cursor.getString(1);
                names.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return names;
    }

    public static String getRandomName()
    {
        SQLiteDatabase db = getStaticDb();
        int number = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAMES);
        int r = random.nextInt(number);
        String selectQuery = "SELECT * FROM " + TABLE_NAMES + " WHERE _id = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{Integer.toString(r)});

        String name = "";
        if (cursor.moveToFirst())
        {
            do
            {
                name = cursor.getString(1);
                Logger.logDebug(LogKey, name.toString());
            } while (cursor.moveToNext());
        }

        cursor.close();
        return name;
    }
}
