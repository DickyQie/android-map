package com.zhangqie.drawmultiplepoints.imap;

import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * Created by zhangqie on 2017/8/15.
 */

public interface InfoWindowAdapter extends AMap.InfoWindowAdapter{

    View getInfoWindow(Marker marker);
    View getInfoContents(Marker marker);
}
