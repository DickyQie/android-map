package com.zhangqie.drawmultiplepoints.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by zhangqie on 2017/8/15.
 */

public class Util {

    public static final String URL = "http://dtjg.liaidicn.com/app.php?";
    public static final Map<String, String> LOCATION() {
        Map<String, String> map = new Hashtable<String, String>();
        map.put("c", "map");
        map.put("a", "batchget_location");
        map.put("use_state", "1");
        map.put("offset", "20");
        map.put("count", "100");
        return map;
    }

}
