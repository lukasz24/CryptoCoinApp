package com.example.radek.finalproduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "bazaKryptowalut";

    private static final String TABLE_CRYPTO = "kryptowaluty";

    private static final String KEY_ID = "id";
    private static final String KEY_SHORT = "short";
    private static final String KEY_NAME = "kryptowaluty";
    private static final String KEY_WATCH = "obserwowane";
    private static final String KEY_CURRENT = "obecnaWartosc";
    private static final String KEY_CHANGE = "zmiana";
    private static final String KEY_IMAGE = "obrazek";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       // db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CRYPTO_TABLE = "CREATE TABLE " + TABLE_CRYPTO + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_SHORT + " TEXT, " +
                KEY_NAME + " TEXT, " + KEY_WATCH + " TEXT, " + KEY_CURRENT + " REAL, " + KEY_CHANGE + " REAL, " + KEY_IMAGE + " INTEGER " + ")";
        db.execSQL(CREATE_CRYPTO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRYPTO);
        onCreate(db);
    }
    /*
    public void onOpen(SQLiteDatabase d){
        super.onOpen(d);
        db = getWritableDatabase();
    }
    public void insertData(String data){
        String stmt = "INSERT INTO " + TABLE_CRYPTO + " (" + KEY_SHORT + ") VALUES ('" + data + "')";
        db.execSQL(stmt);
    }
    public void insertPodstawe(String sho, String fullName, int image){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(KEY_SHORT, sho);
        wartosci.put(KEY_NAME, fullName);
        wartosci.put(KEY_IMAGE, image);
        wartosci.put(KEY_WATCH, "false");
        wartosci.put(KEY_CURRENT, 0.0);
        wartosci.put(KEY_CHANGE, 0.0);
        db.insertOrThrow(TABLE_CRYPTO, null, wartosci);

    }
    public void updateWatch(String sho, String obs){
        String stmt = "UPDATE " + TABLE_CRYPTO + " SET " + KEY_WATCH + " = '" + obs + "' WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }
    public void updateValue(String sho, float value){
        String stmt = "UPDATE " + TABLE_CRYPTO + " SET " + KEY_CURRENT + " = '" + value + "' WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }
    public void updateChange(String sho, float chan){
        String stmt = "UPDATE " + TABLE_CRYPTO + " SET " + KEY_CHANGE + " = '" + chan + "' WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }
    public void updateImage(String sho, int imag){
        String stmt = "UPDATE " + TABLE_CRYPTO + " SET " + KEY_IMAGE + " = '" + imag + "' WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }

    public void updateAll(String sho, String obs, float value, float chan){
        String stmt = "UPDATE " + TABLE_CRYPTO + " SET " + KEY_WATCH + " = '" + obs + "', " + KEY_CURRENT + " = '" + value + "', " + KEY_CHANGE + " = '" + chan + "' WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }

    public void deleteData(String sho){
        String stmt = "DELETE FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        db.execSQL(stmt);
    }

    public Cursor selectWatch(String sho){
        String stmt = "SELECT " + KEY_WATCH + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        return db.rawQuery(stmt, null);
    }

    public Cursor selectValue(String sho){
        String stmt = "SELECT " + KEY_CURRENT + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        return db.rawQuery(stmt, null);
    }

    public Cursor selectChange(String sho){
        String stmt = "SELECT " + KEY_CHANGE + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        return db.rawQuery(stmt, null);
    }
    public Cursor selectName(String sho){
        String stmt = "SELECT " + KEY_NAME + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        return db.rawQuery(stmt, null);
    }
    public Cursor selectWatchShort(){
        String stmt = "SELECT " + KEY_SHORT + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_WATCH + " = 'true'" + ")";
        return db.rawQuery(stmt, null);
    }

    public Cursor selectImage(String sho){
        String stmt = "SELECT " + KEY_IMAGE + " FROM " + TABLE_CRYPTO + " WHERE " + KEY_SHORT + " = '" + sho + "')";
        return db.rawQuery(stmt, null);
    }
    */
}
