package kei.magnet.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by .Sylvain on 03/12/2015.
 */
public abstract class DAOBase {
    protected SQLiteOpenHelper mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new MagnetSQLiteHelper(pContext);
    }
}
