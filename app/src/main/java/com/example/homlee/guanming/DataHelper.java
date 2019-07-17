package com.example.homlee.guanming;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by homlee on 2018/11/24.
 */

public class DataHelper {
    private static final String TAG = "DataHelper";
    private volatile static DataHelper mInstance;
    private GuanMingDbHelper mDbHelper;
    private Map<String, SelectedRoom> mBookedMap;
    //"SUBJECT:(\\d+)=\\[(.+)\\]"
    //TODO 根据数据修改匹配模板
    private static final String GUANMING_ROOM_PATTERN1 = "\\d*\\s*([A-Z]+[0-9]+).*冠铭花园\\s+(7栋[ABD]座)(\\d+)(0\\d).*";
    //1	BHJ024338	史玮	主申请人	42010619801111****
    private static final String CANDIDATE_MAIN_PATTERN = "\\d+\\s+([A-Z]+[0-9]+)\\s+(\\w+)\\s+(\\w+)\\s+(\\d+).*";
    //		李浩瑜	共同申请人	44030520110404****
    private static final String CANDIDATE_COMPANION_PATTERN = "\\s+(\\w+)\\s+(\\w+)\\s+(\\d+).*";

    private DataHelper(Context context) {
        mDbHelper = new GuanMingDbHelper(context.getApplicationContext());
        mBookedMap = new HashMap<>(1024);
        initBuildingMap();
    }

    public static DataHelper getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("You have to call initialize() first!!!");
        }

        return mInstance;
    }

    public static DataHelper initialize(Context context) {
        if (mInstance == null) {
            synchronized (DataHelper.class) {
                if (mInstance == null) {
                    mInstance = new DataHelper(context);
                }
            }
        }
        return mInstance;
    }

    public void reserveRooms(Collection<SelectedRoom> rooms) {
        for (SelectedRoom room : rooms) {
            reserve(room);
        }
    }

    /**
     * 判断房间是否已被预订
     * @param roomId
     * @return
     */
    public boolean isReserved(String roomId) {
        return mBookedMap.containsKey(roomId);
    }

    /**
     * 预订房间
     * @param roomId
     * @return
     */
    public boolean reserve(String roomId) {
        return reserve(new SelectedRoom(roomId));
    }

    /**
     * 预订房间
     * @param room
     * @return
     */
    public boolean reserve(SelectedRoom room) {
        boolean reserved = true;
        if (!mBookedMap.containsKey(room.getRoomId())) {
            mDbHelper.reserveRoom(room);
            mBookedMap.put(room.getRoomId(), room);
        }

        return reserved;
    }



    /**
     * 取消预订
     * @param roomId
     * @return
     */
    public boolean cancel(String roomId) {
        boolean canceled = true;
        if (mBookedMap.containsKey(roomId)) {
            mDbHelper.cancelRoom(roomId);
            mBookedMap.remove(roomId);
        }

        return canceled;
    }

    public void loadAllData() {
        Collection<SelectedRoom> roomList = mDbHelper.loadAllSeletedRooms();
        for (SelectedRoom room : roomList) {
            mBookedMap.put(room.getRoomId(), room);
        }
    }

    /**
     * 移除所有订房信息
     */
    public void clearAllData() {
        mDbHelper.deleteAllReservedRooms();
        mBookedMap.clear();
    }


    public Collection<SelectedRoom> loadDataFromAsset(Context context) {
        List<SelectedRoom> rooms = new ArrayList<>(1024);
        AssetManager manager = context.getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = manager.open("reserved-rooms.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                SelectedRoom room = parseRoomId(GUANMING_ROOM_PATTERN1, line);
                if (room != null) {
                    Log.i(TAG, "loadDataFromAsset: room = " + room);
                    rooms.add(room);
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

    private SelectedRoom parseRoomId(String regex, String line) {
        Log.i(TAG, "parseRoomId: line = " + line);
        if (!TextUtils.isEmpty(line)) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String candidateId = matcher.group(1);
                String buildingName = matcher.group(2);
                if (mBuildingMap.containsKey(buildingName)) {
                    int buildingType = mBuildingMap.get(buildingName);
                    String level = matcher.group(3);
                    String roomNum = matcher.group(4);
                    //roomId = buildingType + "-" + level + roomNum;
                    String roomId = String.format("%02d-%s%s", buildingType, level, roomNum);
                    return new SelectedRoom(roomId, candidateId);
                }

            }
        }
        return null;
    }

    public boolean deleteAllCandidates() {
        return mDbHelper.deleteAllCandidates();
    }

    public boolean loadCandidatesFromAsset(Context context) {
        int size = 0;
        List<Candidate> candidates = new ArrayList<>(1024);
        AssetManager manager = context.getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = manager.open("candidates.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String seqNum = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!TextUtils.isEmpty(line)) {
                    Pattern pattern1 = Pattern.compile(CANDIDATE_MAIN_PATTERN);
                    Matcher matcher1 = pattern1.matcher(line);
                    if (matcher1.matches()) {
                        seqNum = matcher1.group(1);
                        String name = matcher1.group(2);
                        String extra = matcher1.group(3);
                        String identity = matcher1.group(4);
                        candidates.add(new Candidate(seqNum, name, extra, identity));
                    } else {
                        Pattern pattern2 = Pattern.compile(CANDIDATE_COMPANION_PATTERN);
                        Matcher matcher2 = pattern2.matcher(line);
                        if (matcher2.matches()) {
                            String name = matcher2.group(1);
                            String extra = matcher2.group(2);
                            String identity = matcher2.group(3);
                            candidates.add(new Candidate(seqNum, name, extra, identity));
                        }
                    }
                }

                if (candidates.size() == 500) {
                    size += candidates.size();
                    mDbHelper.saveCandidates(candidates);
                    candidates.clear();
                }
            }

            if (candidates.size() > 0) {
                size += candidates.size();
                mDbHelper.saveCandidates(candidates);
                candidates.clear();
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
        return size > 0;
    }

    public List<Candidate> getCandidatesBySeqNum(String seqNum) {
        return mDbHelper.getCandidatesById(seqNum);
    }

    private Map<String, Integer> mBuildingMap = new HashMap<>(16);
    private void initBuildingMap() {
        mBuildingMap.clear();
        //TODO 根据数据修改映射表
        mBuildingMap.put("7栋A座", 1);
        mBuildingMap.put("7栋B座", 2);
        mBuildingMap.put("7栋D座", 3);
    }

    public Collection<SelectedRoom> getSelectedRooms() {
        return mBookedMap.values();
    }

    public SelectedRoom getSelectedRoom(String roomId) {
        return mBookedMap.get(roomId);
    }

    public static void destory() {
        mInstance = null;
    }

}
