 package com.cgz.netimageviewer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

 public class MainActivity extends AppCompatActivity {

     private View mIv;

     private static final int LOAD_ERROR = 2;
     private static final int LOAD_IMAGE = 1;
     private Handler mHandler = new Handler(new Handler.Callback() {
         @Override
         public boolean handleMessage(Message msg) {
             switch (msg.what) {
                 case LOAD_IMAGE:

                     break;
                 case LOAD_ERROR:
                     System.out.println("LOAD_ERROR 加载失败");
                     Toast.makeText(MainActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                     break;
                 default:
                     break;
             }
             return false;
         }
     });

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mIv = findViewById(R.id.IV);
         //1.连接服务器，获取所有的图片链接信息
         loadAllImaagePath();
    }

     private void loadAllImaagePath() {
         new Thread(){
             @Override
             public void run() {
                 // 浏览器发送一个get请求就可以把服务器的数据获取出来
                 // 用代码模拟一个http的get请求

                 try {
                     // 1.得到服务器资源的路径
                     URL url = new URL("http://192.168.102.115:80/img/gaga.html");
                     // 2.通过这个路径打开浏览器链接
                     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                     // 3.设置请求方式为GET
                     conn.setRequestMethod("GET");//注意请求方式只能大写，不能小写

                     //为了有一个更好的UI提醒，获取服务器的返回状态码
                     int code = conn.getResponseCode();

                     if (code == 200 ) {// 返回成功
                         InputStream is = conn.getInputStream();
                         File file = new File(getCacheDir(), "info.txt");
                         FileOutputStream fos = new FileOutputStream(file);

                         int len;
                         byte[] buffer = new byte[1024];
                         while ((len = is.read(buffer)) != -1){
                             fos.write(buffer,0,len);
                         }
                         is.close();
                         fos.close();

                         System.out.println("code = 200");
                         //获取所有链接之后，就要去加载图片

                     } else if (code == 404){// 资源未找到
                         Message msg = Message.obtain();
                         msg.what = LOAD_ERROR;
                         msg.obj = "获取html失败，返回码："+code;
                         mHandler.sendMessage(msg);

                     } else {// 其他响应码
                         Message msg = Message.obtain();
                         msg.what = LOAD_ERROR;
                         msg.obj = "获取html失败，返回码："+code;
                         mHandler.sendMessage(msg);

                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }
         }.start();
     }

     public void pre(View view) {
     }

     public void next(View view) {
     }
 }
