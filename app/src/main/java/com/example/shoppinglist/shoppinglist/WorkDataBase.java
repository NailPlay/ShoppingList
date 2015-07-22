package com.example.shoppinglist.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Наиль on 05.07.2015.
 */
interface Workings {
    public long addRecord(String entry_id, String title);

    public ArrayList<String> readRecord();

    public int deteleIdRecord(int id);

    public int updateIdDataBase(int id, String entry_id, String title);

    public Cursor fetchAllList();

    public Cursor fetchIdList(int id);

    public void deleteAll();
}

public class WorkDataBase implements Workings {
    ListShoppingDbHelper mDbHelper;
    Context context;

    public WorkDataBase(Context context) {
        mDbHelper = new ListShoppingDbHelper(context);
        this.context = context;
    }

    // Добавление записи в базу данных.
    @Override
    public long addRecord(String entry_id, String title){
        try {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_ENTRY_ID, entry_id);
            values.put(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_TITLE, title);
            return db.insert(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Toast.makeText(context, "Произошла какая-то ошибка при добавление записи в базу данных: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    @Override
    public ArrayList<String> readRecord() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor c = db.query(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                do {
                    list.add(c.getString(c.getColumnIndexOrThrow(ListShoppingDbHelper.ShoppingEntry._ID)));
                } while (c.moveToNext());
            } else {
                Log.d("parse", "Нет записей в БД!");
            }
            return list;
        } catch (Exception e) {
            Toast.makeText(context, "Произошла какая-то ошибка при чтение записей: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return list;
        }
    }

    @Override
    public int deteleIdRecord(int id) {
        try {
            String selection = ListShoppingDbHelper.ShoppingEntry._ID + " LIKE " + id;
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            return db.delete(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, selection, null);
        } catch (Exception e) {
            Toast.makeText(context, "Произошла какая-то ошибка при удаление записи: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    @Override
    public int updateIdDataBase(int id, String entry_id, String title) {
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_ENTRY_ID, entry_id);
            values.put(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_TITLE, title);
            String selection = ListShoppingDbHelper.ShoppingEntry._ID + " LIKE " + id;
            return db.update(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, values, selection, null);
        } catch (Exception e) {
            Toast.makeText(context, "Произошла какая-то ошибка при изменение данных: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    @Override
    public Cursor fetchAllList() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public Cursor fetchIdList(int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection = {ListShoppingDbHelper.ShoppingEntry._ID, ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_ENTRY_ID, ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_TITLE};
        String selection = ListShoppingDbHelper.ShoppingEntry._ID + " LIKE " + id;
        Cursor cursor = db.query(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME, projection, selection, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(ListShoppingDbHelper.ShoppingEntry.TABLE_NAME,null,null);
    }

}
