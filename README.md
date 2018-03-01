# Android Studio 中实现高德定位并获取相应信息 
  <p>Android开发项目时常常会遇到定位这个功能，所以写了这篇博客，今天主要讲的高德地图的定位并获取相应信息。<span style="color:#252525"><span style="color:#252525"> </span></span></p> 
<p>首先导入高德的jar包</p> 
<p><img alt="" height="114" src="https://static.oschina.net/uploads/space/2017/0224/114927_1jsx_2945455.png" width="329"></p> 
<p>选中jar包右键点击&nbsp; Add As Library， 在build.fradle中看到如下代码 表示导包成功</p> 
<pre><code class="language-java">compile files('libs/AMap_Location_V3.0.0_20160922.jar')</code></pre> 
<p>之后到高德申请key值，登录打开控制台 点击创建应用，如图：</p> 
<p><img alt="" height="545" src="https://static.oschina.net/uploads/space/2017/0224/133407_6iRd_2945455.png" width="629"></p> 
<p>填写相应信息，</p> 
<p>名称，PackageName：项目的包名，至于SHA1安全码可以这样获取，可以 Window+R 打开控制台&nbsp; 输入cmd点击确定</p> 
<p><img alt="" height="261" src="https://static.oschina.net/uploads/space/2017/0224/134141_13c6_2945455.png" width="427"></p> 
<p>之后在弹框中完成以下操作就可以看到SHA1安全码了</p> 
<p><img alt="" height="708" src="https://static.oschina.net/uploads/space/2017/0224/134745_x9ub_2945455.png" width="673"></p> 
<p>上图的密钥库口令默认的是：android &nbsp; &nbsp; &nbsp;&nbsp;<span style="color:#FF0000"> (注：输入口令是看不见的，输入完成Enter即可)</span></p> 
<p>以上是测试版获取SHA1，发布版SHA1获取请看博客&nbsp;&nbsp; <a href="https://my.oschina.net/zhangqie/blog/845684" rel="nofollow">Android Studio apk打包，keystore.jks文件生成，根据keystore密钥获取SHA1安全码 </a></p> 
<p>输入完信息确定就可以看到key值了</p> 
<p><img alt="" height="172" src="https://static.oschina.net/uploads/space/2017/0224/134918_siW4_2945455.png" width="1164"></p> 
<p>AndroidManifest.xml中加入权限</p> 
<pre><code class="language-html">    &lt;uses-permission android:name="android.permission.INTERNET" /&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;
    &lt;!-- 定位 --&gt;
    &lt;!-- 用于进行网络定位 --&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" &gt;
    &lt;/uses-permission&gt;
    &lt;!-- 用于访问GPS定位 --&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" &gt;
    &lt;/uses-permission&gt;
    &lt;uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"&gt;
    &lt;/uses-permission&gt;</code></pre> 
<p>&lt;application&gt;标签中添加Key值信息</p> 
<pre><code class="language-html"> &lt;meta-data
         android:name="com.amap.api.v2.apikey"
         android:value="ad6c99f7853c8bdce5bd35c5d3cbee76" &gt;
 &lt;/meta-data&gt;
 &lt;!-- 定位需要的服务 --&gt;
 &lt;service android:name="com.amap.api.location.APSService" &gt;
 &lt;/service&gt;</code></pre> 
<p>&nbsp;</p> 
<p>运行效果如图：</p> 
<p><img alt="" height="468" src="https://static.oschina.net/uploads/space/2017/0224/135711_BvOI_2945455.gif" width="273"></p> 
<span id="OSC_h2_1"></span>
