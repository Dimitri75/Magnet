package kei.magnet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by .Sylvain on 03/12/2015.
 * Class used to create the database, and update it.
 * Define all the tables and their columns.
 */
public class MagnetSQLiteHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MagnetDB";

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_KEY = "id";
    public static final String USER_TOKEN = "token";
    public static final String USER_COLUMNS[] = {USER_KEY, USER_TOKEN};
    public static final String USER_TABLE_CREATE =
            "CREATE_TABLE" + USER_TABLE_NAME + " (" +
                USER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_TOKEN + " VARCHAR(64) NOT NULL);";
    public static final String USER_TABLE_DROP = "DROP TABLE IF EXISTS " + USER_TABLE_NAME + ";";

    public MagnetSQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(USER_TABLE_DROP);
        onCreate(db);
    }
}
