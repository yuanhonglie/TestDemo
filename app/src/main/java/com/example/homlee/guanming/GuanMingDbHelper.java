package com.example.homlee.guanming;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GuanMingDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "GuanMingDbHelper";
    private static final String DB_NAME = "booking.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_BOOKING = "booking_table";
    private static final String TABLE_CANDIDATE = "candidate_table";

    public GuanMingDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public interface BookingField {
        String CANDIDATEID = "candidateId";
        String ROOMID = "roomId";
    }

    public interface CandidateField {
        String ID = "_id";
        String SEQ = "seq";
        String NAME = "name";
        String EXTRA = "extra";
        String IDENTITY = "identity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKING
                +"("
                +BookingField.ROOMID + " TEXT PRIMARY KEY, "
                +BookingField.CANDIDATEID + " TEXT"
                +");";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CANDIDATE
                +"("
                +CandidateField.ID + " TEXT PRIMARY KEY, "
                +CandidateField.SEQ + " TEXT, "
                +CandidateField.NAME + " TEXT, "
                +CandidateField.EXTRA + " TEXT, "
                +CandidateField.IDENTITY + " TEXT"
                +");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_BOOKING;
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_CANDIDATE;
        db.execSQL(sql);
        onCreate(db);
    }

    private boolean doesRoomIdExist(SQLiteDatabase db, String roomId) {
        boolean exist = false;
        try {
            String selection = BookingField.ROOMID + "=?";
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
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = BookingField.ROOMID + "=?";
            String[] args = new String[]{""+roomId};
            long row = db.delete(TABLE_BOOKING, whereClause, args);
            succ = row > 0;
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public synchronized boolean reserveRoom(SelectedRoom room) {
        boolean succ = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (doesRoomIdExist(db, room.getRoomId())) {
                succ = true;
            } else {
                ContentValues cvs = new ContentValues();
                cvs.put(BookingField.ROOMID, room.getRoomId());
                cvs.put(BookingField.CANDIDATEID, room.getCandidateId());
                long row = db.insert(TABLE_BOOKING, null, cvs);
                succ = row > 0;
            }
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public synchronized Collection<SelectedRoom> loadAllSeletedRooms() {
        List<SelectedRoom> roomList = new ArrayList<>(1024);
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor c = db.rawQuery("select * from " + TABLE_BOOKING, new String[0]);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String roomId = c.getString(c.getColumnIndex(BookingField.ROOMID));
                        String candidate = c.getString(c.getColumnIndex(BookingField.CANDIDATEID));
                        roomList.add(new SelectedRoom(roomId, candidate));
                    } while (c.moveToNext());
                }
                c.close();
            }
        } catch (Exception e) {
            Log.i(TAG, "loadAllSeletedRooms: error = " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return roomList;
    }

    public boolean cancelRoom(String roomId) {
        return deleteRoomId(roomId);
    }

    public boolean deleteAllReservedRooms() {
        boolean succ = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from " + TABLE_BOOKING);
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public boolean deleteAllCandidates() {
        boolean succ = false;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("delete from " + TABLE_CANDIDATE);
            db.close();
        } catch (Exception e) {}
        return succ;
    }

    public void saveCandidates(List<Candidate> candidates) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                for (Candidate candidate : candidates) {
                    ContentValues cvs = new ContentValues();
                    cvs.put(CandidateField.SEQ, candidate.getId());
                    cvs.put(CandidateField.NAME, candidate.getName());
                    cvs.put(CandidateField.EXTRA, candidate.getExtra());
                    cvs.put(CandidateField.IDENTITY, candidate.getIdentity());
                    db.insert(TABLE_CANDIDATE, null, cvs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            db.close();
        } catch (Exception e) {}
    }

    public List<Candidate> getCandidatesById(String id) {
        List<Candidate> candidates = new ArrayList<>(16);
        try {
            SQLiteDatabase db = getReadableDatabase();
            String selection = CandidateField.SEQ + "=?";
            String[] args = new String[]{""+id};
            Cursor c = db.query(TABLE_CANDIDATE, null, selection, args, null, null, null);
            if (c != null) {
                if (c.getCount() > 0 && c.moveToFirst()) {
                    do {
                        String cid = c.getString(c.getColumnIndex(CandidateField.SEQ));
                        String name = c.getString(c.getColumnIndex(CandidateField.NAME));
                        String extra = c.getString(c.getColumnIndex(CandidateField.EXTRA));
                        String identity = c.getString(c.getColumnIndex(CandidateField.IDENTITY));
                        candidates.add(new Candidate(cid, name, extra, identity));
                    } while (c.moveToNext());
                }
                c.close();
            }
            db.close();
        } catch (Exception e) {}

        return candidates;
    }
}
