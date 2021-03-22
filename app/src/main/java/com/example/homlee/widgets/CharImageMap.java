package com.example.homlee.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hlyu
 */
public class CharImageMap {

    private Map<Character, Integer> mMap;
    private volatile static CharImageMap mInstance;

    private CharImageMap() {
        mMap = new HashMap<>(64);
        initImageMap(mMap);
    }

    private void initImageMap(Map<Character, Integer> map) {
        map.clear();
        /*
        map.put('-', R.drawable.icon_eye);
        map.put('0', R.drawable.icon_eye);
        map.put('1', R.drawable.icon_eye);
        map.put('2', R.drawable.icon_eye);
        map.put('3', R.drawable.icon_eye);
        map.put('4', R.drawable.icon_eye);
        map.put('5', R.drawable.icon_eye);
        map.put('6', R.drawable.icon_eye);
        map.put('7', R.drawable.icon_eye);
        map.put('8', R.drawable.icon_eye);
        map.put('9', R.drawable.icon_eye);

        map.put('A', R.drawable.icon_eye);
        map.put('B', R.drawable.icon_eye);
        map.put('C', R.drawable.icon_eye);
        map.put('D', R.drawable.icon_eye);
        map.put('E', R.drawable.icon_eye);
        map.put('F', R.drawable.icon_eye);
        map.put('G', R.drawable.icon_eye);
        map.put('H', R.drawable.icon_eye);
        map.put('I', R.drawable.icon_eye);
        map.put('J', R.drawable.icon_eye);
        map.put('K', R.drawable.icon_eye);
        map.put('L', R.drawable.icon_eye);
        map.put('M', R.drawable.icon_eye);
        map.put('N', R.drawable.icon_eye);
        map.put('O', R.drawable.icon_eye);
        map.put('P', R.drawable.icon_eye);
        map.put('Q', R.drawable.icon_eye);
        map.put('R', R.drawable.icon_eye);
        map.put('S', R.drawable.icon_eye);
        map.put('T', R.drawable.icon_eye);
        map.put('U', R.drawable.icon_eye);
        map.put('V', R.drawable.icon_eye);
        map.put('W', R.drawable.icon_eye);
        map.put('X', R.drawable.icon_eye);
        map.put('Y', R.drawable.icon_eye);
        map.put('Z', R.drawable.icon_eye);
         */
    }

    public static CharImageMap getInstance() {
        if (mInstance == null) {
            synchronized (CharImageMap.class) {
                if (mInstance == null) {
                    mInstance = new CharImageMap();
                }
            }
        }

        return mInstance;
    }

    public int getImageRes(char ch) {
        if (hasImageRes(ch)) {
            return mMap.get(ch);
        } else {
            return 0;
        }
    }

    public boolean hasImageRes(char ch) {
        return mMap.containsKey(ch);
    }
}
