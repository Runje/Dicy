package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 23.06.2015.
 */
public class PlayerTable
{
    private static final String TABLE_PLAYERS = "players";

    private static final String KEY_NAME = "name";
    private static final String KEY_GAMES_0 = "games0";
    private static final String KEY_GAMES_1 = "games1";
    private static final String KEY_GAMES_2 = "games2";
    private static final String KEY_GAMES_3 = "games3";
    private static final String KEY_GAMES_4 = "games4";
    private static final String KEY_GAMES_5 = "games5";
    private static final String KEY_WINS_0 = "wins0";
    private static final String KEY_WINS_1 = "wins1";
    private static final String KEY_WINS_2 = "wins2";
    private static final String KEY_WINS_3 = "wins3";
    private static final String KEY_WINS_4 = "wins4";
    private static final String KEY_WINS_5 = "wins5";
    private static final String KEY_ID = "id";
    private static final String KEY_STRATEGY = "strategy";
    private static final String CREATE_PLAYERS_TABLE = "CREATE TABLE " + TABLE_PLAYERS + "("
            + KEY_ID + " LONG,"
            + KEY_NAME + " TEXT,"
            + KEY_GAMES_0 + " LONG,"
            + KEY_GAMES_1 + " LONG,"
            + KEY_GAMES_2 + " LONG,"
            + KEY_GAMES_3 + " LONG,"
            + KEY_GAMES_4 + " LONG,"
            + KEY_GAMES_5 + " LONG,"
            + KEY_WINS_0 + " LONG,"
            + KEY_WINS_1 + " LONG,"
            + KEY_WINS_2 + " LONG,"
            + KEY_WINS_3 + " LONG,"
            + KEY_WINS_4 + " LONG,"
            + KEY_WINS_5 + " LONG,"
            + KEY_STRATEGY + " TEXT"
            + ");";


    private static final String[] Player = new String[]{
            KEY_ID, KEY_NAME, KEY_GAMES_0, KEY_GAMES_1, KEY_GAMES_2, KEY_GAMES_3, KEY_GAMES_4, KEY_GAMES_5, KEY_WINS_0, KEY_WINS_1, KEY_WINS_2, KEY_WINS_3, KEY_WINS_4, KEY_WINS_5, KEY_STRATEGY};
    public static String LogKey = "PlayerTable";

    public static PlayerStatistic createPlayer(String name, Strategy strategy, SQLiteDatabase db)
    {
        String s = strategy == null ? Strategy.Human : strategy.toString();
        PlayerStatistic player = new PlayerStatistic(getNextId(db), name, new long[6], new long[6], s);
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
        values.put(KEY_GAMES_0, player.getGames(RuleVariant.values()[0]));
        values.put(KEY_GAMES_1, player.getGames(RuleVariant.values()[1]));
        values.put(KEY_GAMES_2, player.getGames(RuleVariant.values()[2]));
        values.put(KEY_GAMES_3, player.getGames(RuleVariant.values()[3]));
        values.put(KEY_GAMES_4, player.getGames(RuleVariant.values()[4]));
        values.put(KEY_GAMES_5, player.getGames(RuleVariant.values()[5]));
        values.put(KEY_WINS_0, player.getWins(RuleVariant.values()[0]));
        values.put(KEY_WINS_1, player.getWins(RuleVariant.values()[1]));
        values.put(KEY_WINS_2, player.getWins(RuleVariant.values()[2]));
        values.put(KEY_WINS_3, player.getWins(RuleVariant.values()[3]));
        values.put(KEY_WINS_4, player.getWins(RuleVariant.values()[4]));
        values.put(KEY_WINS_5, player.getWins(RuleVariant.values()[5]));
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

        player1.increaseGames(game.getRuleVariant());
        player2.increaseGames(game.getRuleVariant());

        if (game.hasP1Won())
        {
            player1.increaseWins(game.getRuleVariant());
        }
        else
        {
            player2.increaseWins(game.getRuleVariant());
        }

        updatePlayer(player1, db);
        updatePlayer(player2, db);
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL(CREATE_PLAYERS_TABLE);
        createPlayer("Thomas", Strategy.getStrategy(Strategy.Human), db);
        createPlayer("Milena", Strategy.getStrategy(Strategy.Human), db);
        createPlayer("Horst", new Strategy(0, 0, 0, 0, 0, 1), db);
        createPlayer("Emil", new Strategy(0.1, 1, 0.1, 0.1, 0.1, 0.9), db);
        createPlayer("Victor", new Strategy(1, 0, 1, 1, 1, 0), db);
        createPlayer("Franz", new Strategy(0.2, 0.2, 0.2, 0.2, 0.2, 0.2), db);
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

        long[] games = new long[]{cursor.getLong(2), cursor.getLong(3), cursor.getLong(4), cursor.getLong(5), cursor.getLong(6), cursor.getLong(7)};
        long[] wins = new long[]{cursor.getLong(8), cursor.getLong(9), cursor.getLong(10), cursor.getLong(11), cursor.getLong(12), cursor.getLong(13)};
        return new PlayerStatistic(cursor.getLong(0), cursor.getString(1), games, wins, cursor.getString(14));
    }
}
