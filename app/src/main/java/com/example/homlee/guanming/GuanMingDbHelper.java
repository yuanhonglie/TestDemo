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
        String candidateId = "candidateId";
        String roomId 	= "roomId";
    }

    public interface CandidateField {
        String id = "_id";
        String seq = "seq";
        String name = "name";
        String extra = "extra";
        String identity = "identity";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKING
                +"("
                +BookingField.roomId 	+ " TEXT PRIMARY KEY, "
                +BookingField.candidateId + " TEXT"
                +");";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CANDIDATE
                +"("
                +CandidateField.id + " TEXT PRIMARY KEY, "
                +CandidateField.seq + " TEXT, "
                +CandidateField.name + " TEXT, "
                +CandidateField.extra + " TEXT, "
                +CandidateField.identity + " TEXT"
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
            SQLiteDatabase db = getWritableDatabase();
            String whereClause = BookingField.roomId + "=?";
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
                cvs.put(BookingField.roomId, room.getRoomId());
                cvs.put(BookingField.candidateId, room.getCandidateId());
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
                        String roomId = c.getString(c.getColumnIndex(BookingField.roomId));
                        String candidate = c.getString(c.getColumnIndex(BookingField.candidateId));
                        roomList.add(new SelectedRoom(roomId, candidate));
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
                    cvs.put(CandidateField.seq, candidate.getId());
                    cvs.put(CandidateField.name, candidate.getName());
                    cvs.put(CandidateField.extra, candidate.getExtra());
                    cvs.put(CandidateField.identity, candidate.getIdentity());
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
            String selection = CandidateField.seq + "=?";
            String[] args = new String[]{""+id};
            Cursor c = db.query(TABLE_CANDIDATE, null, selection, args, null, null, null);
            if (c != null) {
                if (c.getCount() > 0 && c.moveToFirst()) {
                    do {
                        String cid = c.getString(c.getColumnIndex(CandidateField.seq));
                        String name = c.getString(c.getColumnIndex(CandidateField.name));
                        String extra = c.getString(c.getColumnIndex(CandidateField.extra));
                        String identity = c.getString(c.getColumnIndex(CandidateField.identity));
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
