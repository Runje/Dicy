package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import games.runje.dicymodel.Logger;
import games.runje.dicymodel.game.RuleVariant;

/**
 * Created by Thomas on 19.08.2015.
 */
public abstract class PointsTable
{
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_POINTS = "points";
    private static final String KEY_RULEVARIANT = "rulevariant";

    private static final String[] Points = new String[]{
            KEY_NAME, KEY_DATE, KEY_POINTS, KEY_RULEVARIANT};

    protected String getCreateTableString()
    {
        return "CREATE TABLE " + getTableName() + "("
                + KEY_NAME + " TEXT,"
                + KEY_DATE + " LONG,"
                + KEY_POINTS + " INT,"
                + KEY_RULEVARIANT + " TEXT"
                + ");";
    }

    public void create(SQLiteDatabase db)
    {
        db.execSQL(getCreateTableString());
    }

    public void add(int points, String name, RuleVariant ruleVariant, SQLiteDatabase db)
    {
        ContentValues values = PointToValues(new PointStatistic(name, new Date(), points, ruleVariant));
        // Inserting Row
        db.insert(getTableName(), null, values);
    }

    private ContentValues PointToValues(PointStatistic pointStatistic)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pointStatistic.getName());
        values.put(KEY_DATE, pointStatistic.getDate().getTime());
        values.put(KEY_POINTS, pointStatistic.getPoints());
        values.put(KEY_RULEVARIANT, pointStatistic.getRuleVariant().toString());
        return values;
    }

    public ArrayList<PointStatistic> getAll(SQLiteDatabase db)
    {
        ArrayList<PointStatistic> points = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + getTableName();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                PointStatistic point = createPointFromCursor(cursor);
                points.add(point);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return points;
    }

    public ArrayList<PointStatistic> getPoints(SQLiteDatabase db, RuleVariant ruleVariant)
    {
        ArrayList<PointStatistic> points = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + getTableName() + " WHERE " + KEY_RULEVARIANT + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{ruleVariant.toString()});

        if (cursor.moveToFirst())
        {
            do
            {
                PointStatistic point = createPointFromCursor(cursor);
                points.add(point);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return points;
    }

    private PointStatistic createPointFromCursor(Cursor cursor)
    {
        return new PointStatistic(cursor.getString(0), new Date(cursor.getLong(1)), cursor.getInt(2), RuleVariant.getEnum(cursor.getString(3)));
    }

    abstract String getTableName();

    public void delete(SQLiteDatabase db, PointStatistic point)
    {
        db.delete(getTableName(), KEY_NAME + "=? AND " + KEY_DATE + " =? AND " + KEY_POINTS + "=? AND " + KEY_RULEVARIANT + "=?", new String[]{point.getName(), Long.toString(point.getDate().getTime()), Integer.toString(point.getPoints()), point.getRuleVariant().toString()});
        Logger.logInfo("POINTS", "Deleted: " + point.toString());
    }

    public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        drop(db);
        create(db);
    }

    private void drop(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }
}
