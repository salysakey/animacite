package org.anima.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.anima.entities.Food;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiam on 08/02/2016.
 */
public class CacheActuDB {


    public static final String TABLE_CACHE = "CACHE";

    public static final String FIELD_ID = "_id";
    public static final String FIELD_CACHE_CONTENT = "cache_content";
    public static final String FIELD_LAST_CACHE = "last_cache";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CACHE + " (" +
            FIELD_ID + " INTEGER PRIMARY KEY, " +
            FIELD_CACHE_CONTENT + " TEXT, " +
            FIELD_LAST_CACHE + " INTEGER" + ")";

    public static final String DROP_TABLE = "DROP TABLE " + TABLE_CACHE;
    public static final Object lock = new Object();
    private Context context;

    private SQLiteDatabase _db = null;

    public CacheActuDB(SQLiteDatabase _db, Context context) {
        this._db = _db;
        this.context = context;
    }

    public List<Food> createFoodsFromCache(Cursor cursor) {
        List<Food> foods = null;
        Type type = new TypeToken<List<Food>>(){}.getType();
        final int COL_ID = cursor.getColumnIndexOrThrow(FIELD_ID);
        final int COL_CACHE_CONTENT = cursor.getColumnIndexOrThrow(FIELD_CACHE_CONTENT);
        final int COL_LAST_CACHE = cursor.getColumnIndexOrThrow(FIELD_LAST_CACHE);

        if (!cursor.isAfterLast()) {

            String cacheContent = cursor.getString(COL_CACHE_CONTENT);
            long lastCache = cursor.getLong(COL_LAST_CACHE);
            long id = cursor.getLong(COL_ID);
            long currentTimeInMillis = System.currentTimeMillis();
            if (currentTimeInMillis - lastCache <= DateUtils.DAY_IN_MILLIS) {
                foods = deserializeCache(new Gson().fromJson(cacheContent, JsonArray.class));
            } else {
                deleteCacheById(id);
            }
        }
        return foods;
    }

    public boolean deleteCacheById(long cacheId) {
        if (cacheId < 0) return false;

        int nbRows;
        synchronized (lock) {
            nbRows = _db.delete(
                    TABLE_CACHE,
                    FIELD_ID + " = ?",
                    new String[] { String.valueOf(cacheId) });
        }
        return nbRows > 0;
    }

    public boolean clearCache() {
        int nbRows;
        synchronized (lock) {
            nbRows = _db.delete(
                    TABLE_CACHE,
                    null,
                    null);
        }
        return nbRows > 0;
    }

    public List<Food> getFoodsFromCache() {
        List<Food> foods = null;

        synchronized (lock) {
            Cursor cursor = _db.query(TABLE_CACHE, null, null, null, null, null, null);

            if (cursor != null && !cursor.isAfterLast()) {
                cursor.moveToFirst();
                foods = createFoodsFromCache(cursor);
                cursor.close();
            }

        }

        return foods;
    }

    public boolean saveCache(List<Food> foods) {
        synchronized (lock) {
            clearCache();
            long id;
            id = _db.insert(TABLE_CACHE, null, getContentValues(foods));
            return id > 0;
        }
    }

    private ContentValues getContentValues(List<Food> foods) {
        Type type = new TypeToken<List<Food>>(){}.getType();
        String content = new Gson().toJson(foods, type);

        ContentValues initialValues = new ContentValues();
        initialValues.put(CacheActuDB.FIELD_CACHE_CONTENT, content);
        initialValues.put(CacheActuDB.FIELD_LAST_CACHE, System.currentTimeMillis());
        return initialValues;
    }

    private List<Food> deserializeCache(JsonArray array) {
        List<Food> foods = null;
        if (array != null) {
            foods = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject object = element.getAsJsonObject();
                Food feeling = new Food();
                feeling.setId(object.get("id").getAsInt());
                feeling.setName(object.get("name").getAsString());
                feeling.setPictureUrl(object.get("pictureUrl").getAsString());
                feeling.setPrice(object.get("price").getAsString());
                feeling.setDescriptif(object.get("descriptif").getAsString());
                feeling.setType(object.get("type").getAsInt());
                feeling.setIdent(object.get("ident").getAsInt());
                foods.add(feeling);
            }
        }
        return foods;
    }

}
