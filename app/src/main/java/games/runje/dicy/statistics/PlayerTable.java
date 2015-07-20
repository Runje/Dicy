package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.ai.Strategy;

/**
 * Created by Thomas on 23.06.2015.
 */
public class PlayerTable
{
    private static final String TABLE_PLAYERS = "players";

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


    private static final String[] Player = new String[]{
            KEY_ID, KEY_NAME, KEY_GAMES, KEY_WINS, KEY_STRATEGY};
    public static String LogKey = "PlayerTable";

    public static PlayerStatistic createPlayer(String name, String strategy, SQLiteDatabase db)
    {
        PlayerStatistic player = new PlayerStatistic(getNextId(db), name, 0, 0, strategy);
        String query = "select count(*) from " + TABLE_PLAYERS + " where " + KEY_NAME + " = ?";
        Cursor c = db.rawQuery(query, new String[]{name});
        if (c.moveToFirst())
        {
            if (c.getInt(0) != 0)
            {
                Log.d("DBHandler", player.getName() + " is already in DB: " + c.getInt(0));
                c.close();
                return getPlayer(name, db);
            }
        }

        c.close();

        ContentValues values = PlayerToValues(player);
        // Inserting Row
        db.insert(TABLE_PLAYERS, null, values);


        return player;
    }

    private static long getNextId(SQLiteDatabase db)
    {
        return SQLiteHandler.getNextId(db, KEY_ID, TABLE_PLAYERS);
    }

    public static PlayerStatistic getPlayer(String name, SQLiteDatabase db)
    {
        Cursor cursor = db.query(TABLE_PLAYERS, Player, KEY_NAME + "= ? ",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
            PlayerStatistic playerStatistic = createPlayerFromCursor(cursor);
            cursor.close();
            return playerStatistic;
        }
        else
        {
            Log.d("DBHandler", name + " is not in DB.");
            return null;
        }


    }

    public static void drop(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
    }

    private static ContentValues PlayerToValues(PlayerStatistic player)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, player.getId());
        values.put(KEY_NAME, player.getName());
        values.put(KEY_GAMES, player.getGames());
        values.put(KEY_WINS, player.getWins());
        values.put(KEY_STRATEGY, player.getStrategy());
        return values;
    }

    private static void updatePlayer(PlayerStatistic player, SQLiteDatabase db)
    {
        ContentValues values = PlayerToValues(player);
        int i = db.update(TABLE_PLAYERS, values, KEY_ID + " = " + player.getId(), null);
        Log.d("DBHandler", "There are " + i + " " + player.getName());
    }

    public static void update(GameStatistic game, SQLiteDatabase db)
    {
        Logger.logInfo(LogKey, "Update");
        PlayerStatistic player1 = game.getPlayer1();
        PlayerStatistic player2 = game.getPlayer2();

        player1.increaseGames();
        player2.increaseGames();

        if (game.hasP1Won())
        {
            player1.increaseWins();
        }
        else
        {
            player2.increaseWins();
        }

        updatePlayer(player1, db);
        updatePlayer(player2, db);
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL(CREATE_PLAYERS_TABLE);
        createPlayer("Thomas", Strategy.Human, db);
        createPlayer("Milena", Strategy.Human, db);
        createPlayer("Max", Strategy.Simple, db);
    }

    public static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        drop(db);
        create(db);
    }

    public static ArrayList<PlayerStatistic> getAll(SQLiteDatabase db)
    {
        ArrayList<PlayerStatistic> players = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYERS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                PlayerStatistic player = createPlayerFromCursor(cursor);
                players.add(player);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return players;
    }

    private static PlayerStatistic createPlayerFromCursor(Cursor cursor)
    {
        return new PlayerStatistic(cursor.getLong(0), cursor.getString(1), cursor.getLong(2), cursor.getLong(3), cursor.getString(4));
    }
}
