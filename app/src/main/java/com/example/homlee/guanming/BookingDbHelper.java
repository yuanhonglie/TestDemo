package com.example.homlee.guanming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by homlee on 2018/11/24.
 */

public class BookingDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "BookingDbHelper";
    private static final String DB_NAME = "booking.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_BOOKING = "booking_table";

    public BookingDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public interface BookingField {
        String id 		= "id";
        String roomId 	= "roomId";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKING
                +"("
                +BookingField.roomId 	+ " TEXT PRIMARY KEY"
                +");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_BOOKING;
        db.execSQL(sql);
        onCreate(db);
    }

    private boolean doesRoomIdExist(SQLiteDatabase db, String roomId) {
        boolean exist = false;
        try {
            String selection = BookingField.roomId + "=?";
            String[] args = new String[]{""+roomId};
            Cursor c = db.query(TABLE_BOOKING, null, selection, args, null, null, null);
            if (c != null) {
                if (c.getCount() > 0 && c.moveToFirst()) {
                    exist = true;
                }
                c.close();
            }
        } catch (Exception e) {}
        return exist;
    }

    private boolean deleteRoomId(String roomId) {
        boolean succ = false;
        try {
            SQLiteDatabase db = getReadableDatabase();
            String whereClause = BookingField.roomId + "=?";
            String[] args = new String[]{""+roomId};
            long row = db.delete(TABLE_BOOKING, whereClause, args);
            succ = row > 0;
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public boolean reserve(String roomId) {
        boolean succ = false;
        try {
            SQLiteDatabase db = getReadableDatabase();
            if (doesRoomIdExist(db, roomId)) {
                succ = true;
            } else {
                ContentValues cvs = new ContentValues();
                cvs.put(BookingField.roomId, roomId);
                long row = db.insert(TABLE_BOOKING, null, cvs);
                succ = row > 0;
            }
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public boolean reserve(Collection<String> roomIdList) {
        boolean succ = true;
        try {
            SQLiteDatabase db = getReadableDatabase();
            for (String roomId : roomIdList) {
                if (!doesRoomIdExist(db, roomId)) {
                    ContentValues cvs = new ContentValues();
                    cvs.put(BookingField.roomId, roomId);
                    long row = db.insert(TABLE_BOOKING, null, cvs);
                    if (row <= 0) {
                        succ = false;
                        break;
                    }
                }
            }
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public Collection<String> loadAllData() {
        List<String> roomList = new ArrayList<>(1024);
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.rawQuery("select * from " + TABLE_BOOKING, new String[0]);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String roomId = c.getString(c.getColumnIndex(BookingField.roomId));
                        roomList.add(roomId);
                    } while (c.moveToNext());
                }
                c.close();
            }
        } catch (Exception e) {
//			e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return roomList;
    }

    public boolean cancel(String roomId) {
        return deleteRoomId(roomId);
    }

    public boolean deleteAll() {
        boolean succ = false;
        try {
            SQLiteDatabase db = getReadableDatabase();
            db.execSQL("delete from " + TABLE_BOOKING);
            db.close();
        } catch (Exception e) {}
        return succ;
    }
}
