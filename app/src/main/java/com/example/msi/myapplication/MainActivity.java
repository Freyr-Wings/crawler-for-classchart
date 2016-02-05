package com.example.msi.myapplication;
import android.content.Intent;
import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.os.Handler;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.net.CookieHandler;
    import java.net.CookieManager;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.net.URLEncoder;
    import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    import android.app.Activity;
    import android.text.TextUtils;
    import android.view.View;
    import android.webkit.CookieSyncManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import org.jsoup.Connection;
    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;

public class MainActivity extends Activity {
    static List<String> datastring = new ArrayList<>();
    Button downImgBtn = null;
    ImageView showImageView = null;
    Intent intent = null;
    // 声明控件对象
    private EditText id233, pwd233, xdvfb233;
    // 声明显示返回数据库的控件对象
    private TextView tv_result;
    private Handler handler = new Handler();
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置显示的视图
        setContentView(R.layout.activity_main);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        System.out.println(cookieManager);
        // 通过 findViewById(id)方法获取用户名的控件对象
        id233 = (EditText) findViewById(R.id.et_name);
        // 通过 findViewById(id)方法获取用户密码的控件对象
        pwd233 = (EditText) findViewById(R.id.et_pass);
        xdvfb233 = (EditText) findViewById(R.id.et_yzm);
        // 通过 findViewById(id)方法获取显示返回数据的控件对象
        tv_result = (TextView) findViewById(R.id.tv_result);
        final ImageView imageView = (ImageView)this.findViewById(R.id.imagview_show);
        new Thread() {
            public void run() {
                String urlbitmap = "http://210.42.121.241/servlet/GenImg";
                //得到可用的图片
                Bitmap bitmap = getHttpBitmap(urlbitmap);

                //显示
                imageView.setImageBitmap(bitmap);
                 // 调用loginByPost方法
            };
        }.start();

        initUI();
        downImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showImageView.setVisibility(View.VISIBLE);
                String imgUrl = "http://210.42.121.241/servlet/GenImg";
                new DownImgAsyncTask().execute(imgUrl);
            }
        });
    }
    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    public void initUI() {
        showImageView = (ImageView)findViewById(R.id.imagview_show);
        downImgBtn = (Button)findViewById(R.id.btn_download_img);
    }

    /**
     * 从指定URL获取图片
     * @param url
     * @return
     */
    private Bitmap getImageBitmap(String url){
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showImageView.setImageBitmap(null);
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            Bitmap b = getImageBitmap(params[0]);
            return b;
        }
        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result!=null){
                showImageView.setImageBitmap(result);
            }
        }
    }


     /**
      * POST请求操作
      *
      * @param id
      * @param pwd
      * @param xdvfb
      */
     public void loginByPost(String id, String pwd, String xdvfb) {

         try {
             // 请求的地址
             String spec = "http://210.42.121.241/servlet/Login";
             // 根据地址创建URL对象
             URL url = new URL(spec);
             // 根据URL对象打开链接
             HttpURLConnection urlConnection = (HttpURLConnection) url
                     .openConnection();

             // 设置请求的方式
             urlConnection.setRequestMethod("POST");
             // 设置请求的超时时间
             urlConnection.setReadTimeout(5000);
             urlConnection.setConnectTimeout(5000);
             // 传递的数据
             String data = "id=" + URLEncoder.encode(id,"GB2312")
                     + "&pwd=" + URLEncoder.encode(pwd,"GB2312")
                     + "&xdvfb=" + URLEncoder.encode(xdvfb,"GB2312");
             // 设置请求的头
             urlConnection.setRequestProperty("Connection", "keep-alive");
             // 设置请求的头
             urlConnection.setRequestProperty("Content-Type",
                     "application/x-www-form-urlencoded");
             // 设置请求的头
             urlConnection.setRequestProperty("Content-Length",
                     String.valueOf(data.getBytes().length));
             // 设置请求的头
             urlConnection
                     .setRequestProperty("User-Agent",
                             "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/42.0");
             urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
             urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
             OutputStream os = urlConnection.getOutputStream();
             os.write(data.getBytes());

             final StringBuffer sb = new StringBuffer();//把获取的数据不断存放到StringBuffer中；
             BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "GB2312"));//使用reader向输入流中读取数据，并不断存放到StringBuffer中；
             String line;
             while ((line = reader.readLine()) != null) {//只要还没有读取完，就不断读取；
                 sb.append(line);//在StringBuffer中添加；
             }
             handler.post(new Runnable() {//使用Handler更新UI；当然这里也可以使用sendMessage();handMessage()来进行操作；
                    @Override
                    public void run() {
                    tv_result.setText(sb.toString());//StringBuffer转化为String输出；
                    }
             });
             os.flush();



         }
         catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();}
         catch (Exception e) {
                e.printStackTrace();
         }
         try {

             // 请求的地址
             String spec = "http://210.42.121.241/servlet/Svlt_QueryStuLsn?action=queryStuLsn";
             // 根据地址创建URL对象
             URL url = new URL(spec);
             // 根据URL对象打开链接
             HttpURLConnection urlConnection2 = (HttpURLConnection) url
                     .openConnection();
             // 设置请求的方式
             urlConnection2.setRequestMethod("GET");

             // 设置请求的超时时间
             urlConnection2.setReadTimeout(5000);
             urlConnection2.setConnectTimeout(5000);
             urlConnection2
                     .setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/42.0");
             urlConnection2.setDoOutput(true); // 发送POST请求必须设置允许输出
             urlConnection2.setDoInput(true); // 发送POST请求必须设置允许输入
             OutputStream os = urlConnection2.getOutputStream();

             final StringBuffer sb = new StringBuffer();//把获取的数据不断存放到StringBuffer中；
             BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream(), "GB2312"));//使用reader向输入流中读取数据，并不断存放到StringBuffer中；
             String line;
             String lessons = null;
             int i = 0;
             while ((line = reader.readLine()) != null) {//只要还没有读取完，就不断读取；
                 sb.append(line);//在StringBuffer中添加；
                 Pattern p = Pattern.compile(".*?;//课程名$");
                 Matcher m = p.matcher(line);
                 List<String> result=new ArrayList<>();

                 while(m.find()){
                     result.add(m.group());
                     lessons = line;
                     lessons = lessons.replaceAll("\\s+","");
                     lessons = lessons.replaceAll("varlessonName=\"","");
                     lessons = lessons.replaceAll("\";//课程名","");
                     System.out.println(lessons);
                     datastring.add(lessons);
                     HashSet h = new HashSet(datastring);
                     datastring.clear();
                     datastring.addAll(h);

                 }
                 for(String s1:result){
                     System.out.println(s1);
                 }

             }
             handler.post(new Runnable() {//使用Handler更新UI；当然这里也可以使用sendMessage();handMessage()来进行操作；
                 @Override
                 public void run() {
                     tv_result.setText(sb.toString());//StringBuffer转化为String输出；
                 }
             });
             os.flush();

         }
         catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();}
         catch (Exception e) {
             e.printStackTrace();
         }

     }
    static int t = datastring.size();
    public static List<String> generateData(int size)
    {
        List<String> datas = new ArrayList<>();
        datas.addAll(datastring);
        return datas;
    }
     /**
     * 通过android:onClick="login"指定的方法 ， 要求这个方法中接受你点击控件对象的参数v
     *
     * @param v
     */
     public void login(View v) {
         // 获取点击控件的id
         int id1 = v.getId();
         // 根据id进行判断进行怎么样的处理
         switch (id1) {
             // 登陆事件的处理
             case R.id.btn_login:
                 // 获取用户名
                 final String id = id233.getText().toString();
                 // 获取用户密码
                 final String pwd = pwd233.getText().toString();
                 final String xdvfb = xdvfb233.getText().toString();
                 if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pwd)) {
                     Toast.makeText(this, "用户名或者密码不能为空", Toast.LENGTH_LONG).show();
                 } else {
                     // 开启子线程
                     new Thread() {
                         public void run() {
                             loginByPost(id, pwd, xdvfb); // 调用loginByPost方法
                             intent = new Intent(MainActivity.this, MainActivity2.class);
                             startActivity(intent);
                         };
                     }.start();
                 }
                 break;
         }

     }

}
