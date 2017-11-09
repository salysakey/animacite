package org.anima.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by thiam on 08/02/2016.
 */
public class AnimaDatabase extends SQLiteOpenHelper {

    private static final String TAG = "OTMDatabase";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = " AnimaDatabase";
    private final Context context;

    public AnimaDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        try {
            getWritableDatabase().close();
        } catch (SQLiteException e) {
            Log.w(TAG, e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CacheActuDB.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= DB_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + CacheActuDB.TABLE_CACHE);
            onCreate(db);
        }
    }

    public CacheActuDB getCacheDB() throws SQLiteException {
        return new CacheActuDB(this.getWritableDatabase(), context);
    }
}
