package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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

    // Contacts table name
    private static final String TABLE_PLAYERS = "players";

    // Contacts Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_GAMES = "games";
    private static final String KEY_WINS = "wins";
    private static final String KEY_ID = "id";
    private static final String KEY_STRATEGY = "strategy";
    private static final String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "("
            + KEY_ID + " LONG,"
            + KEY_NAME + " TEXT,"
            + KEY_GAMES + " LONG,"
            + KEY_WINS + " LONG,"
            + KEY_STRATEGY + " TEXT"
            + ");";
    private String LogKey = "DB";

    private static final String[] Player = new String[] {
            KEY_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_STRATEGY};

    public SQLiteHandler(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }



    @Override
    public PlayerStatistic createPlayer(String name, String strategy)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        PlayerStatistic player = new PlayerStatistic(getNextId(), name, 0,0, strategy);
        String query = "select count(*) from " + TABLE_PLAYERS + " where " + KEY_NAME + " = ?";
        Cursor c = db.rawQuery(query, new String[] { name });
        if (c.moveToFirst())
        {
            if (c.getInt(0) != 0)
            {
                Log.d("DBHandler", player.getName() + " is already in DB: " + c.getInt(0));
                c.close();
                return getPlayer(name);
            }
        }

        c.close();

        ContentValues values = PlayerToValues(player);
        // Inserting Row
        db.insert(TABLE_PLAYERS, null, values);
        db.close(); // Closing database connection

        return player;
    }

    private long getNextId()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(" + KEY_ID + ") from " + TABLE_PLAYERS;
        Cursor c = db.rawQuery(query, null);
        long id = -1;
        if (c.moveToFirst())
        {
           id = c.getLong(0) + 1;
        }

        c.close();

        return id;
    }

    @Override
    public PlayerStatistic getPlayer(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYERS, Player, KEY_NAME + "= ? ",
                new String[] { name }, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            return createPlayerFromCursor(cursor);
        }
        else
        {
            Log.d("DBHandler", name + " is not in DB.");
            return null;
        }
    }

    public void recreate()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);

        onCreate(db);
    }

    private ContentValues PlayerToValues(PlayerStatistic player)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, player.getId());
        values.put(KEY_NAME, player.getName());
        values.put(KEY_GAMES, player.getGames());
        values.put(KEY_WINS, player.getWins());
        values.put(KEY_STRATEGY, player.getStrategy());
        return values;
    }

    @Override
    public void update(GameStatistic game)
    {
        PlayerStatistic player1 = game.getPlayer1();
        PlayerStatistic player2 = game.getPlayer2();

        player1.increaseGames();
        player2.increaseGames();

        if (game.getWonIndex() == 0)
        {
            player1.increaseWins();
        }
        else
        {
            player2.increaseWins();
        }

        updatePlayer(player1);
        updatePlayer(player2);
    }

    private void updatePlayer(PlayerStatistic player)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = PlayerToValues(player);
        int i = db.update(TABLE_PLAYERS, values, KEY_ID + " = " + player.getId(), null);
        Log.d("DBHandler", "There are " + i + " " + player.getName());
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        Logger.logInfo(LogKey, "On Create DB");
        sqLiteDatabase.execSQL(CREATE_PLAYERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        Logger.logInfo(LogKey, "On Upgrade from " + oldVersion + " to " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<PlayerStatistic> getAllPlayers()
    {
        ArrayList<PlayerStatistic> players = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                PlayerStatistic player = createPlayerFromCursor(cursor);
                players.add(player);
            } while (cursor.moveToNext());
        }

        return players;
    }

    private PlayerStatistic createPlayerFromCursor(Cursor cursor)
    {
        return new PlayerStatistic(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3), cursor.getString(4));
    }
}
