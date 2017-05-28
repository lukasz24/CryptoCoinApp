package com.example.radek.finalproduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by Admin on 2017-05-24.
 */

public class KryptoDAO {
    private DatabaseHandler dbHelper;

    public KryptoDAO(Context context){
        dbHelper = new DatabaseHandler(context);
    }

    public void insertKrypto(final Krypto krypto){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Kryptos.Columns.KEY_ID, krypto.getId());
        contentValues.put(Kryptos.Columns.KEY_SHORT, krypto.getShortName());
        contentValues.put(Kryptos.Columns.KEY_NAME, krypto.getLongName());
        contentValues.put(Kryptos.Columns.KEY_WATCH, krypto.getObserwowane());
        contentValues.put(Kryptos.Columns.KEY_CURRENT, krypto.getAnkualnaCena());
        contentValues.put(Kryptos.Columns.KEY_CHANGE, krypto.getZmiana());
        contentValues.put(Kryptos.Columns.KEY_IMAGE, krypto.getObrazek());

        dbHelper.getWritableDatabase().insert(Kryptos.TABLE_CRYPTO, null, contentValues);
    }



    public void updateKrypto(final Krypto krypto){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Kryptos.Columns.KEY_WATCH, krypto.getObserwowane());
        contentValues.put(Kryptos.Columns.KEY_CURRENT, krypto.getAnkualnaCena());
        contentValues.put(Kryptos.Columns.KEY_CHANGE, krypto.getZmiana());

        String quest = "id=?";
        String[] questArgs = new String[] {String.valueOf(krypto.getId())};

        //dbHelper.getWritableDatabase().update(Kryptos.TABLE_CRYPTO, contentValues, "" + Kryptos.Columns.KEY_ID + " = ? ", new String[]{krypto.});
        dbHelper.getWritableDatabase().update(Kryptos.TABLE_CRYPTO, contentValues,  quest, questArgs);

    }
    public boolean isTableEmpty(){
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Kryptos.TABLE_CRYPTO, null);
        if(cursor.getCount() == 0){
            return true;
        }
        return false;
    }

    public Krypto getKryptoById(final int id){

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Kryptos.TABLE_CRYPTO + " WHERE " + Kryptos.Columns.KEY_ID + " = " + id, null);

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            return mapCursorToKrypto(cursor);
        }
        return null;

    }

    // COS NIE DZIAŁA
    public Krypto getKryptoByShort(final String sho){
         Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Kryptos.TABLE_CRYPTO + " WHERE " + Kryptos.Columns.KEY_SHORT + " = '" + sho + "'", null);

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            return mapCursorToKrypto(cursor);
        }
        return null;

    }
    public Krypto getKryptoByIMG(final int img){
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Kryptos.TABLE_CRYPTO + " WHERE " + Kryptos.Columns.KEY_IMAGE + " = '" + img + "'", null);

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            return mapCursorToKrypto(cursor);
        }
        return null;

    }
    public Krypto getKryptoByName(final String name){
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + Kryptos.TABLE_CRYPTO + " WHERE " + Kryptos.Columns.KEY_NAME + " = '" + name + "'", null);

        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            return mapCursorToKrypto(cursor);
        }
        return null;

    }

    private Krypto mapCursorToKrypto(final Cursor cursor){
        int idColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_ID);
        int shortColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_SHORT);
        int nameColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_NAME);
        int watchColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_WATCH);
        int currentColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_CURRENT);
        int changeColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_CHANGE);
        int imageColumnId = cursor.getColumnIndex(Kryptos.Columns.KEY_IMAGE);

        Krypto krypto = new Krypto();
        krypto.setId(cursor.getInt(idColumnId));
        krypto.setShortName(cursor.getString(shortColumnId));
        krypto.setLongName(cursor.getString(nameColumnId));
        krypto.setObserwowane(cursor.getString(watchColumnId));
        krypto.setAnkualnaCena(cursor.getDouble(currentColumnId));
        krypto.setZmiana(cursor.getDouble(changeColumnId));
        krypto.setObrazek(cursor.getInt(imageColumnId));


        return krypto;
    }

}
