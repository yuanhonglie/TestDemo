package com.example.homlee.Utils;

import java.util.List;

/**
 * Created by homlee on 2018/11/24.
 */

public class ListUtils {

    private ListUtils(){};


    public static <T> int size(List<T> list) {
        return isEmpty(list) ? 0 : list.size();
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || (list.size() == 0);
    }

}
