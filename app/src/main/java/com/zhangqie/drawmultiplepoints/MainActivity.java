package com.zhangqie.drawmultiplepoints;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.zhangqie.drawmultiplepoints.entity.MapDataInfo;
import com.zhangqie.drawmultiplepoints.imap.InfoWindowAdapter;
import com.zhangqie.drawmultiplepoints.util.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<MapDataInfo.Data>  list;
    com.amap.api.maps.AMap aMap;
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        aMap = mapView.getMap();
        aMap.setTrafficEnabled(true);// 显示实时交通状况
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        showRequest();
    }

    private void showRequest() {
        OkHttpUtils.post().url(Util.URL).params(Util.LOCATION()).build().execute(new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(String response, int id) {
                MapDataInfo info = JSON.parseObject(response, MapDataInfo.class);
                list=info.getData();
                    mapView.onResume();
                      moveToForbiddenCity();
                      addmarker();
                }
        });
    }
    private void moveToForbiddenCity() {
        if (list.size() > 0) {
            double v = Double.parseDouble(list.get(0).getPosition().get(1));
            double v1 = Double.parseDouble(list.get(0).getPosition().get(0));
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(v, v1), 13.0f);
            aMap.moveCamera(cu);
        }
    }

    private void addmarker() {
        for (MapDataInfo.Data info : list) {
            MarkerOptions mo = new MarkerOptions();
            double v = Double.parseDouble(info.getPosition().get(1));
            double v1 = Double.parseDouble(info.getPosition().get(0));
            mo.position(new LatLng(v, v1));
            mo.title(info.getLift_code()).snippet(info.getUse_company_name());
            aMap.setOnMarkerClickListener(markerClickListener);
            mo.draggable(true);
            mo.icon(BitmapDescriptorFactory.defaultMarker());
            mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            aMap.addMarker(mo);
        }
    }

    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            final MarkerOptions moo = marker.getOptions();
            showMapView(marker.getTitle(), marker.getSnippet(), moo);
            return false;
        }
    };


    private void showMapView(final String coed, final String name, MarkerOptions mo) {
        final Marker markern = aMap.addMarker(mo);
        aMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            View infoWindow = null;
            @Override
            public View getInfoWindow(Marker marker) {
                if (infoWindow == null) {
                    infoWindow = LayoutInflater.from(MainActivity.this).inflate(R.layout.map_wallrk_layout, null);
                }
                render(markern, infoWindow);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }

            public void render(Marker marker, View view) {
                TextView textView = (TextView) view.findViewById(R.id.text_map_code);
                textView.setText(coed);
                TextView address = (TextView) view.findViewById(R.id.text_map_title_name);
                address.setText(name);
                view.findViewById(R.id.text_map_content).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, name, Toast.LENGTH_LONG).show();
                    }
                });
                marker.showInfoWindow();
                marker.setFlat(false);
            }
        });
    }


}
