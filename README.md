# Android Studio之高德地图实现定位和3D地图显示 
  <p>在应用开发中，地图开发是经常需要使用的“组件”，国内比较出名的是就是百度地图和高德地图。</p> 
<p>此博客讲的是高德地图实现定位和3D地图显示，并标注相应位置，话不多说，先看看效果，在上代码。</p> 
<p>&nbsp;效果如图：</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" src="https://static.oschina.net/uploads/space/2017/0225/092641_yr8v_2945455.gif"></p> 
<p>首先注册高德成为开发者（打开高德地图，点击底部的开发者平台），创建应用，按照要求填写相应信息，如下图：</p> 
<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <img alt="" height="537" src="https://static.oschina.net/uploads/space/2017/0225/092612_InnT_2945455.png" width="628"></p> 
<p>途中包含了发布版的SHA1安全码和测试版SHA1安全码，两者的值可以看&nbsp; 博客 ： <a href="https://my.oschina.net/zhangqie/blog/845684" rel="nofollow">Android Studio apk打包，keystore.jks文件生成，根据keystore密钥获取SHA1安全码&nbsp; </a></p> 
<p>讲的很详细，照做就一定会成功获取的。</p> 
<p>首先导入高德的jar包</p> 
<p><img alt="" height="134" src="https://static.oschina.net/uploads/space/2017/0225/093747_Noxl_2945455.png" width="336"></p> 
<p>选中jar包右键点击&nbsp; Add As Library， 在build.fradle中看到如下代码 表示导包成功</p> 
<pre><code class="language-java">    compile files('libs/AMap_Location_V3.0.0_20160922.jar')
    compile files('libs/AMap_Search_V3.5.0_20160927.jar')
    compile files('libs/AMap_3DMap_V4.1.1_20161019.jar')</code></pre> 
<p>代码：(注：项目代码目录中要添加一个接口文件&nbsp; JniLibs 的os文件&nbsp; 才能绘制地图，具体下载源码查看，就不详细描述了)</p> 
<pre><code class="language-java">/****
 * 讲诉了高德地图定位和3D地图显示
 *
 * 打包和未打包的情况是不一样的，高德配置是可以配置调试版和发布版
 *
 */
public class MainActivity extends AppCompatActivity implements AMapLocationListener,GeocodeSearch.OnGeocodeSearchListener {

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView textView;
    private String[] strMsg;
    private com.amap.api.maps.AMap aMap;
    private MapView mapView;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private static LatLonPoint latLonPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_map);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        Location();
    }



    private void initMap(){

        if (aMap == null) {
            aMap = mapView.getMap();

             //用高德默认图标
             //geoMarker= aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            //自定义图标
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                   .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.punch_dw))));
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        getAddress(latLonPoint);
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
                        textView.setText("地址：" + strMsg[0] + "\n" + "经    度：" + strMsg[1] + "\n" + "纬    度：" + strMsg[2]);
                        latLonPoint= new LatLonPoint(Double.valueOf(strMsg[2]), Double.valueOf(strMsg[1]));
                        initMap();
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


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null &amp;&amp; result.getRegeocodeAddress() != null
                    &amp;&amp; result.getRegeocodeAddress().getFormatAddress() != null) {

                Toast.makeText(MainActivity.this,result.getRegeocodeAddress().getFormatAddress()
                        + "附近",Toast.LENGTH_LONG).show();
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                geoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
            } else {

            }
        } else {
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}</code></pre> 
<p>&nbsp;</p> 
<p>AndroidManifest.xml中加入权限</p> 
<p>&nbsp;</p> 
<pre><code class="language-html"> &lt;!--允许程序打开网络套接字--&gt;
    &lt;uses-permission android:name="android.permission.INTERNET" /&gt;
    &lt;!-- 定位 --&gt;
    &lt;!-- 用于访问GPS定位 --&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"&gt;&lt;/uses-permission&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"&gt;&lt;/uses-permission&gt;
    &lt;!--允许程序设置内置sd卡的写权限--&gt;
    &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /&gt;
    &lt;!--允许程序获取网络状态--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;
    &lt;!--允许程序访问WiFi网络信息--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /&gt;
    &lt;!--允许程序读写手机状态和身份--&gt;
    &lt;uses-permission android:name="android.permission.READ_PHONE_STATE" /&gt;
    &lt;!--允许程序访问CellID或WiFi热点来获取粗略的位置--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /&gt;</code></pre> 
<p>&lt;application&gt;标签中添加Key值信息</p> 
<pre><code class="language-html">&lt;meta-data
  android:name="com.amap.api.v2.apikey"
  android:value="32c4bc659a111616d9b1196522223206"&gt;&lt;/meta-data&gt;
   &lt;!-- 定位需要的服务 --&gt;
&lt;service android:name="com.amap.api.location.APSService"&gt;&lt;/service&gt;</code></pre> 
<p>&nbsp;</p> 
<p>如果只实现高德定位可查看： <a href="https://github.com/DickyQie/android-map" rel="nofollow">Android Studio 中实现高德定位并获取相应信息&nbsp; </a></p> 
<p>讲的很详细。</p> 
