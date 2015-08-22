package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.game.GameLength;

/**
 * Created by Thomas on 23.06.2015.
 */
public class GameTable
{
    private static final String TABLE_GAMES = "games";

    private static final String KEY_ID = "id";
    private static final String KEY_P1 = "p1";
    private static final String KEY_P2 = "p2";
    private static final String KEY_P1Start = "p1start";
    private static final String KEY_P1Won = "p1won";
    private static final String KEY_P1Points = "p1points";
    private static final String KEY_P2Points = "p2points";
    private static final String KEY_DATE = "date";
    private static final String KEY_LENGTH = "length";
    private static final String CREATE_GAMES_TABLE = "CREATE TABLE " + TABLE_GAMES + "("
            + KEY_ID + " LONG,"
            + KEY_P1 + " TEXT,"
            + KEY_P2 + " TEXT,"
            + KEY_P1Start + " INT,"
            + KEY_P1Won + " INT,"
            + KEY_P1Points + " INT,"
            + KEY_P2Points + " INT,"
            + KEY_DATE + " LONG,"
            + KEY_LENGTH + " TEXT"
            + ");";


    private static final String[] Player = new String[]{
            KEY_ID, KEY_P1, KEY_P2, KEY_P1Start, KEY_P1Won, KEY_P1Points, KEY_P2Points, KEY_DATE, KEY_LENGTH};
    public static String LogKey = "PlayerTable";

    public static void add(GameStatistic game, SQLiteDatabase db)
    {
        game.setId(getNextId(db));
        String query = "select count(*) from " + TABLE_GAMES + " where " + KEY_ID + " = ?";
        Cursor c = db.rawQuery(query, new String[]{Long.toString(game.getId())});
        if (c.moveToFirst())
        {
            if (c.getInt(0) != 0)
            {
                Log.d(LogKey, game.getId() + " is already in DB: " + c.getInt(0));
                c.close();
                return;
            }
        }

        c.close();

        ContentValues values = gameToValues(game);
        // Inserting Row
        db.insert(TABLE_GAMES, null, values);

    }

    private static ContentValues gameToValues(GameStatistic game)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, game.getId());
        values.put(KEY_P1, game.getPlayer1().getName());
        values.put(KEY_P2, game.getPlayer2().getName());
        values.put(KEY_P1Start, game.hasP1Started() ? 1 : 0);
        values.put(KEY_P1Won, game.hasP1Won() ? 1 : 0);
        values.put(KEY_P1Points, game.getP1Points());
        values.put(KEY_P2Points, game.getP2Points());
        values.put(KEY_DATE, game.getDate().getTime());
        values.put(KEY_LENGTH, game.getLength().toString());
        return values;
    }

    private static long getNextId(SQLiteDatabase db)
    {
        return SQLiteHandler.getNextId(db, KEY_ID, TABLE_GAMES);
    }

    public static ArrayList<GameStatistic> getAll(SQLiteDatabase db)
    {
        ArrayList<GameStatistic> games = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GAMES;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                GameStatistic game = createGameFromCursor(cursor);
                Logger.logDebug(LogKey, game.toString());
                games.add(game);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return games;
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL(CREATE_GAMES_TABLE);
    }

    private static GameStatistic createGameFromCursor(Cursor cursor)
    {
        return new GameStatistic(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1, cursor.getInt(4) == 1, cursor.getInt(5), cursor.getInt(6), new Date(cursor.getLong(7)), GameLength.valueOf(cursor.getString(8)));
    }

    public static List<GameStatistic> getGames(SQLiteDatabase db, String player1, String player2)
    {
        ArrayList<GameStatistic> games = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GAMES + " WHERE (" + KEY_P1 + " = ? AND " + KEY_P2 + " = ?) OR (" + KEY_P1 + " = ? " + "AND " + KEY_P2 + " = ?)";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{player1, player2, player2, player1});

        if (cursor.moveToFirst())
        {
            do
            {
                GameStatistic game = createGameFromCursor(cursor);
                Logger.logDebug(LogKey, game.toString());
                games.add(game);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return games;
    }

    public static List<GameStatistic> getGames(SQLiteDatabase db, GameLength length)
    {
        ArrayList<GameStatistic> games = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_GAMES + " WHERE (" + KEY_LENGTH + " = ?)";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{length.toString()});

        if (cursor.moveToFirst())
        {
            do
            {
                GameStatistic game = createGameFromCursor(cursor);
                Logger.logDebug(LogKey, game.toString());
                games.add(game);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return games;
    }

    public static void upgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        drop(sqLiteDatabase);
        create(sqLiteDatabase);
    }

    public static void drop(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
    }
}
