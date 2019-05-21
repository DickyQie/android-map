### android ------ 实现高德定位并获取相应信息 ( 最新版高德SDK 和 Android SDK版本) 
 <p>Android开发项目时常常会遇到定位这个功能， 很久以前写过一篇了，官方也更新了一些东西，我也更新下</p> 
<p>以前使用的是jar包 导入来实现高德定位</p> 
<p>老版本 链接：<a href="https://blog.csdn.net/DickyQie/article/details/56840100" rel="nofollow">https://blog.csdn.net/DickyQie/article/details/56840100</a></p> 
<p>&nbsp;</p> 
<p>现在通过Gradle集成SDK（也可以使用jar包）</p> 
<pre><code class="language-java"> //定位依赖
    implementation 'com.amap.api:location:latest.integration'</code></pre> 
<p>之后到高德申请key值，登录打开控制台 点击创建应用，如图：</p> 
<p><img alt="" height="545" src="https://static.oschina.net/uploads/space/2017/0224/133407_6iRd_2945455.png" width="629"></p> 
<p>填写相应信息，</p> 
<p>名称，PackageName：项目的包名，至于SHA1安全码可以这样获取，可以 Window+R 打开控制台&nbsp; 输入cmd点击确定</p> 
<p><img alt="" height="261" src="https://static.oschina.net/uploads/space/2017/0224/134141_13c6_2945455.png" width="427"></p> 
<p>之后在弹框中完成以下操作就可以看到SHA1安全码了</p> 
<p><img alt="" height="708" src="https://static.oschina.net/uploads/space/2017/0224/134745_x9ub_2945455.png" width="673"></p> 
<p>上图的密钥库口令默认的是：android &nbsp; &nbsp; &nbsp;&nbsp;&nbsp;(注：输入口令是看不见的，输入完成Enter即可)</p> 
<p>以上是测试版获取SHA1，发布版SHA1获取请看博客&nbsp;&nbsp;&nbsp;<a href="http://www.cnblogs.com/zhangqie/p/6439052.html" rel="nofollow">Android Studio apk打包，keystore.jks文件生成，根据keystore密钥获取SHA1安全码</a></p> 
<p>输入完信息确定就可以看到key值了</p> 
<p><img alt="" height="172" src="https://static.oschina.net/uploads/space/2017/0224/134918_siW4_2945455.png" width="1164"></p> 
<p>AndroidManifest.xml中加入权限</p> 
<pre><code class="language-html">复制代码

  &lt;!--用于进行网络定位--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"&gt;&lt;/uses-permission&gt;
    &lt;!--用于访问GPS定位--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"&gt;&lt;/uses-permission&gt;
    &lt;!--用于获取运营商信息，用于支持提供运营商信息相关的接口--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"&gt;&lt;/uses-permission&gt;
    &lt;!--用于访问wifi网络信息，wifi信息会用于进行网络定位--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_WIFI_STATE"&gt;&lt;/uses-permission&gt;
    &lt;!--用于获取wifi的获取权限，wifi信息会用来进行网络定位--&gt;
    &lt;uses-permission android:name="android.permission.CHANGE_WIFI_STATE"&gt;&lt;/uses-permission&gt;
    &lt;!--用于访问网络，网络定位需要上网--&gt;
    &lt;uses-permission android:name="android.permission.INTERNET"&gt;&lt;/uses-permission&gt;
    &lt;!--用于读取手机当前的状态--&gt;
    &lt;uses-permission android:name="android.permission.READ_PHONE_STATE"&gt;&lt;/uses-permission&gt;
    &lt;!--用于写入缓存数据到扩展存储卡--&gt;
    &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"&gt;&lt;/uses-permission&gt;
    &lt;!--用于申请调用A-GPS模块--&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"&gt;&lt;/uses-permission&gt;
    &lt;!--用于申请获取蓝牙信息进行室内定位--&gt;
    &lt;uses-permission android:name="android.permission.BLUETOOTH"&gt;&lt;/uses-permission&gt;
    &lt;uses-permission android:name="android.permission.BLUETOOTH_ADMIN"&gt;&lt;/uses-permission&gt;</code></pre> 
<p>&lt;application&gt;标签中添加Key值信息</p> 
<pre><code class="language-html">&lt;service android:name="com.amap.api.location.APSService"&gt;&lt;/service&gt;
 &lt;meta-data android:name="com.amap.api.v2.apikey" android:value="2ca77496c8ae6b92cef9e15dfd68b87b"&gt;
 &lt;/meta-data&gt;</code></pre> 
<p>Activity代码：（和以前的相比少了很多不必要的代码）</p> 
<pre><code class="language-java"> /***
     * 定位
     */
    private void showLocation() {
        try {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {

        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息

                    //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Log.i("定位类型", amapLocation.getLocationType() + "");
                    Log.i("获取纬度", amapLocation.getLatitude() + "");
                    Log.i("获取经度", amapLocation.getLongitude() + "");
                    Log.i("获取精度信息", amapLocation.getAccuracy() + "");

                    //如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i("地址", amapLocation.getAddress());
                    Log.i("国家信息", amapLocation.getCountry());
                    Log.i("省信息", amapLocation.getProvince());
                    Log.i("城市信息", amapLocation.getCity());
                    Log.i("城区信息", amapLocation.getDistrict());
                    Log.i("街道信息", amapLocation.getStreet());
                    Log.i("街道门牌号信息", amapLocation.getStreetNum());
                    Log.i("城市编码", amapLocation.getCityCode());
                    Log.i("地区编码", amapLocation.getAdCode());
                    Log.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
                    Log.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
                    Log.i("获取当前室内定位的楼层", amapLocation.getFloor());
                    Log.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");

                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());

                    Log.i("获取定位时间", df.format(date));


                    // 停止定位
                    mlocationClient.stopLocation();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    @Override
    protected void onDestroy() {
        destroyLocation();
        super.onDestroy();
    }</code></pre> 
<p>运行得到日志：</p> 
<p><img alt="" src="https://img2018.cnblogs.com/blog/1041439/201905/1041439-20190521174939921-540808649.png"></p> 
<p>&nbsp;</p> 
