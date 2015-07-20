package games.runje.dicy.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 15.06.2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper implements StatisticManager
{
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "dicy.db";

    private String LogKey = "DB";
    public SQLiteHandler(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public static long getNextId(SQLiteDatabase db, String keyId, String table)
    {
        String query = "SELECT MAX(" + keyId + ") from " + table;
        Cursor c = db.rawQuery(query, null);
        long id = -1;
        if (c.moveToFirst())
        {
            id = c.getLong(0) + 1;
        }

        c.close();

        return id;
    }

    public PlayerStatistic createPlayer(String name, String strategy, SQLiteDatabase db)
    {
        return PlayerTable.createPlayer(name, strategy, db);
    }

    @Override
    public PlayerStatistic createPlayer(String name, String strategy)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        PlayerStatistic p = createPlayer(name, strategy, db);
        db.close(); // Closing database connection
        return p;
    }

    @Override
    public PlayerStatistic getPlayer(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return PlayerTable.getPlayer(name, db);
    }

    public void recreate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        PlayerTable.drop(db);

        onCreate(db);
    }

    @Override
    public List<GameStatistic> getAllGames()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return GameTable.getAll(db);
    }

    @Override
    public List<GameStatistic> getGames(String player1, String player2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return GameTable.getGames(db, player1, player2);
    }

    @Override
    public void update(GameStatistic game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        PlayerTable.update(game, db);
        GameTable.add(game, db);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Logger.logInfo(LogKey, "On Create DB");
        PlayerTable.create(db);
        GameTable.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        Logger.logInfo(LogKey, "On Upgrade from " + oldVersion + " to " + newVersion);
        PlayerTable.upgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    public ArrayList<PlayerStatistic> getAllPlayers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return PlayerTable.getAll(db);
    }
}
