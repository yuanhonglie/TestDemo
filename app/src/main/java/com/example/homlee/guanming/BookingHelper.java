package com.example.homlee.guanming;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by homlee on 2018/11/24.
 */

public class BookingHelper {
    private static final String TAG = "BookingHelper";
    private volatile static BookingHelper mInstance;
    private BookingDbHelper mDbHelper;
    private Set<String> mBookedRooms;
    //"SUBJECT:(\\d+)=\\[(.+)\\]"
    //TODO 根据数据修改匹配模板
    private static final String GUANMING_ROOM_PATTERN1 = ".*(冠[銘铭]7栋[ABD]座)\\s+(\\d+)(0\\d).*";

    private BookingHelper(Context context) {
        mDbHelper = new BookingDbHelper(context.getApplicationContext());
        mBookedRooms = new HashSet<>(1024);
        initBuildingMap();
    }

    public static BookingHelper getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("You have to call initialize() first!!!");
        }

        return mInstance;
    }

    public static BookingHelper initialize(Context context) {
        if (mInstance == null) {
            synchronized (BookingHelper.class) {
                if (mInstance == null) {
                    mInstance = new BookingHelper(context);
                }
            }
        }
        return mInstance;
    }

    public void reserveRooms(Collection<String> rooms) {
        for (String id : rooms) {
            reserve(id);
        }
    }

    /**
     * 判断房间是否已被预订
     * @param room
     * @return
     */
    public boolean isReserved(Room room) {
        return mBookedRooms.contains(room.getRoomId());
    }

    /**
     * 预订房间
     * @param room
     * @return
     */
    public boolean reserve(Room room) {
        boolean reserved = reserve(room.getRoomId());
        room.setSelected(reserved);
        return reserved;
    }

    /**
     * 预订房间
     * @param roomId
     * @return
     */
    public boolean reserve(String roomId) {
        boolean reserved = true;
        if (!mBookedRooms.contains(roomId)) {
            mDbHelper.reserve(roomId);
            mBookedRooms.add(roomId);
        }

        return reserved;
    }

    /**
     * 取消预订
     * @param room
     * @return
     */
    public boolean cancel(Room room) {
        boolean canceled = true;
        if (mBookedRooms.contains(room.getRoomId())) {
            mDbHelper.cancel(room.getRoomId());
            mBookedRooms.remove(room.getRoomId());
        }

        room.setSelected(!canceled);
        return canceled;
    }

    public void loadAllData() {
        Collection<String> roomList = mDbHelper.loadAllData();
        for (String room : roomList) {
            mBookedRooms.add(room);
        }
    }

    /**
     * 移除所有订房信息
     */
    public void clearAllData() {
        mDbHelper.deleteAll();
        mBookedRooms.clear();
    }


    public Set<String> loadDataFromAsset(Context context) {
        Set<String> rooms = new HashSet<>(1024);
        AssetManager manager = context.getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = manager.open("reserved-rooms.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String roomId = parseRoomId(GUANMING_ROOM_PATTERN1, line);
                if (!TextUtils.isEmpty(roomId)) {
                    rooms.add(roomId);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "loadDataFromAsset: error1 = " + e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "loadDataFromAsset: error2 = " + e.getMessage());
            }
        }

        return rooms;
    }

    private String parseRoomId(String regex, String line) {
        Log.i(TAG, "parseRoomId: line = " + line);
        String roomId = "";

        if (!TextUtils.isEmpty(line)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String buildingName = matcher.group(1);
                if (mBuildingMap.containsKey(buildingName)) {
                    int buildingType = mBuildingMap.get(buildingName);
                    String level = matcher.group(2);
                    String roomNum = matcher.group(3);
                    //roomId = buildingType + "-" + level + roomNum;
                    roomId = String.format("%02d-%s%s", buildingType, level, roomNum);
                }

            }
        }
        return roomId;
    }

    private Map<String, Integer> mBuildingMap = new HashMap<>(16);
    private void initBuildingMap() {
        mBuildingMap.clear();
        //TODO 根据数据修改映射表
        mBuildingMap.put("冠銘花园7栋A座", 1);
        mBuildingMap.put("冠銘花园7栋B座", 2);
        mBuildingMap.put("冠銘花园7栋D座", 3);
        mBuildingMap.put("冠銘7栋A座", 1);
        mBuildingMap.put("冠銘7栋B座", 2);
        mBuildingMap.put("冠銘7栋D座", 3);
        mBuildingMap.put("冠铭7栋A座", 1);
        mBuildingMap.put("冠铭7栋B座", 2);
        mBuildingMap.put("冠铭7栋D座", 3);
        mBuildingMap.put("7栋A座", 1);
        mBuildingMap.put("7栋B座", 2);
        mBuildingMap.put("7栋D座", 3);
        mBuildingMap.put("7A", 1);
        mBuildingMap.put("7B", 2);
        mBuildingMap.put("7D", 3);
    }

    public void destory() {
        mInstance = null;
    }

}
