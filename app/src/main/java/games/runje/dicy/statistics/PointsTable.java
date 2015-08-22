package games.runje.dicy.statistics;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import games.runje.dicymodel.Logger;

/**
 * Created by Thomas on 19.08.2015.
 */
public abstract class PointsTable
{
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_POINTS = "points";

    private static final String[] Points = new String[]{
            KEY_NAME, KEY_DATE, KEY_POINTS};

    protected String getCreateTableString()
    {
        return "CREATE TABLE " + getTableName() + "("
                + KEY_NAME + " TEXT,"
                + KEY_DATE + " LONG,"
                + KEY_POINTS + " INT"
                + ");";
    }

    public void create(SQLiteDatabase db)
    {
        db.execSQL(getCreateTableString());
    }

    public void add(int points, String name, SQLiteDatabase db)
    {
        ContentValues values = PointToValues(new PointStatistic(name, new Date(), points));
        // Inserting Row
        db.insert(getTableName(), null, values);
    }

    private ContentValues PointToValues(PointStatistic pointStatistic)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pointStatistic.getName());
        values.put(KEY_DATE, pointStatistic.getDate().getTime());
        values.put(KEY_POINTS, pointStatistic.getPoints());
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

    private PointStatistic createPointFromCursor(Cursor cursor)
    {
        return new PointStatistic(cursor.getString(0), new Date(cursor.getLong(1)), cursor.getInt(2));
    }

    abstract String getTableName();

    public void delete(SQLiteDatabase db, PointStatistic point)
    {
        db.delete(getTableName(), KEY_NAME + "=? AND " + KEY_DATE + " =? AND " + KEY_POINTS + "=?", new String[]{point.getName(), Long.toString(point.getDate().getTime()), Integer.toString(point.getPoints())});
        Logger.logInfo("POINTS", "Deleted: " + point.toString());
    }
}
