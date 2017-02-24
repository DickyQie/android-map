package com.mapdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/****
 * AS 实现定位操作
 */
public class MainActivity extends AppCompatActivity implements AMapLocationListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_map);
        Location();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //定位完成
                case Utils.MSG_LOCATION_FINISH:
                    String result = "";
                    try {
                        AMapLocation loc = (AMapLocation) msg.obj;
                        result = Utils.getLocationStr(loc, 1);
                        strMsg = result.split(",");
                        Toast.makeText(MainActivity.this, "定位成功", Toast.LENGTH_LONG).show();
                        textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[1]);
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
        };

    };

    public void Location() {
        // TODO Auto-generated method stub
        try {
            locationClient = new AMapLocationClient(this);
            locationOption = new AMapLocationClientOption();
            // 设置定位模式为低功耗模式
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            // 设置定位监听
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);//设置为单次定位
            locationClient.setLocationOption(locationOption);// 设置定位参数
            // 启动定位
            locationClient.startLocation();
            mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_LONG).show();
        }
    }
}
