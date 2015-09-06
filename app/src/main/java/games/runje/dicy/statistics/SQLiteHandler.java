package games.runje.dicy.statistics;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.ai.Strategy;
import games.runje.dicymodel.data.Player;
import games.runje.dicymodel.game.GameLength;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 15.06.2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper implements StatisticManager
{
    // Database Version
    private static final int DATABASE_VERSION = 6;

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

    @Override
    public boolean addMovePoints(int movePoints, Player playingPlayer, RuleVariant ruleVariant)
    {
        SQLiteDatabase db = getWritableDatabase();
        MovePointsTable table = new MovePointsTable();

        List<PointStatistic> points = table.getAll(db);

        if (points.size() >= 10)
        {
            Collections.sort(points, new Comparator<PointStatistic>()
            {
                @Override
                public int compare(PointStatistic p1, PointStatistic p2)
                {
                    return ((Integer) p1.getPoints()).compareTo(p2.getPoints());
                }
            });

            Collections.reverse(points);

            if (points.get(9).getPoints() > movePoints)
            {
                db.close();
                return false;
            }
        }

        table.add(movePoints, playingPlayer.getName(), ruleVariant, db);
        db.close();
        Logger.logInfo(LogKey, "Added " + movePoints + " Points");
        return true;
    }

    @Override
    public boolean addSwitchPoints(int switchPoints, Player playingPlayer, RuleVariant ruleVariant)
    {
        SQLiteDatabase db = getWritableDatabase();
        SwitchPointsTable table = new SwitchPointsTable();
        List<PointStatistic> points = table.getAll(db);

        if (points.size() >= 10)
        {
            Collections.sort(points, new Comparator<PointStatistic>()
            {
                @Override
                public int compare(PointStatistic p1, PointStatistic p2)
                {
                    return ((Integer) p1.getPoints()).compareTo(p2.getPoints());
                }
            });

            Collections.reverse(points);

            if (points.get(9).getPoints() > switchPoints)
            {
                db.close();
                return false;
            }
        }

        table.add(switchPoints, playingPlayer.getName(), ruleVariant, db);
        db.close();
        Logger.logInfo(LogKey, "Added " + switchPoints + " Points");

        return true;
    }

    @Override
    public List<PointStatistic> getAllMovePoints()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PointStatistic> points = new MovePointsTable().getAll(db);
        db.close();
        return points;
    }

    @Override
    public List<PointStatistic> getMovePoints(RuleVariant ruleVariant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PointStatistic> points = new MovePointsTable().getPoints(db, ruleVariant);
        db.close();
        return points;
    }

    @Override
    public void deleteMovePoint(PointStatistic point)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        new MovePointsTable().delete(db, point);
        db.close();
    }

    @Override
    public void deleteSwitchPoint(PointStatistic point)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        new SwitchPointsTable().delete(db, point);
        db.close();
    }

    @Override
    public List<PointStatistic> getAllSwitchPoints()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PointStatistic> points = new SwitchPointsTable().getAll(db);
        db.close();
        return points;
    }

    @Override
    public List<PointStatistic> getSwitchPoints(RuleVariant ruleVariant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PointStatistic> points = new SwitchPointsTable().getPoints(db, ruleVariant);
        db.close();
        return points;
    }

    public PlayerStatistic createPlayer(String name, Strategy strategy, SQLiteDatabase db)
    {
        return PlayerTable.createPlayer(name, strategy, db);
    }

    @Override
    public PlayerStatistic createPlayer(String name, Strategy strategy)
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

        PlayerStatistic player = PlayerTable.getPlayer(name, db);

        db.close();

        return player;
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
        List<GameStatistic> games = GameTable.getAll(db);
        db.close();
        return games;
    }

    @Override
    public List<GameStatistic> getGames(String player1, String player2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<GameStatistic> games = GameTable.getGames(db, player1, player2);
        db.close();
        return games;
    }

    @Override
    public List<GameStatistic> getGames(GameLength length)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<GameStatistic> games = GameTable.getGames(db, length);
        db.close();
        return games;
    }

    @Override
    public List<GameStatistic> getGames(GameLength length, RuleVariant ruleVariant)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        List<GameStatistic> games = GameTable.getGames(db, length, ruleVariant);
        db.close();
        return games;
    }

    @Override
    public void update(GameStatistic game)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        PlayerTable.update(game, db);
        GameTable.add(game, db);
        db.close();
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
        GameTable.upgrade(sqLiteDatabase, oldVersion, newVersion);

        // TODO: make singleton
        new SwitchPointsTable().upgrade(sqLiteDatabase, oldVersion, newVersion);
        new MovePointsTable().upgrade(sqLiteDatabase, oldVersion, newVersion);


    }

    public ArrayList<PlayerStatistic> getAllPlayers()
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<PlayerStatistic> players = PlayerTable.getAll(db);
        db.close();
        return players;
    }
}
