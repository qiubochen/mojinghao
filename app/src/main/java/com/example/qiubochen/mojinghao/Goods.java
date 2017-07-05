package com.example.qiubochen.mojinghao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Goods extends AppCompatActivity {
     private Toolbar tool;
    String TAG="goodsNews";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_goods);
        tool=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tool);//设置toolbar
        tool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int to=item.getItemId();
                if(to==R.id.tool){
                    Intent i=new Intent(Goods.this,createGoods.class);
                    startActivity(i);
                }
                return false;
            }
        });
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.goodstitlebar);

//        Map<String,Object> gooditem= new HashMap<String ,Object>();
//        gooditem.put("name","mojinghao");
//        gooditem.put("sale","123878");
//        gooditem.put("account","12338797");
//        goodsli.add(gooditem);
//        Map<String,Object> gooditem1= new HashMap<String ,Object>();
//        gooditem1.put("name","mojingha");
//        gooditem1.put("sale","12");
//        gooditem1.put("account","133");
//        goodsli.add(gooditem1);
        goodsThread goodsthread = new goodsThread();
        goodsthread.start();
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,goodsthread.getList(),R.layout.goods_listitem,new String[]{"name","value","sum"},new int[]{R.id.goodsname,R.id.goodsvalue,R.id.goodssum});
        ListView goodlistview=(ListView)findViewById(R.id.goods_listview);
        goodlistview.setAdapter(simpleAdapter);//设置listview
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolba,menu);
        return true;
    }
    class goodsThread extends Thread{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        @Override
        public void run() {
            try {
                URL url = new URL("http://10.0.2.2:8080/test8/goodsServelet");
                byte[] data = new byte[1024];

                int len = 0;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//建立连接
                httpURLConnection.setConnectTimeout(3000);//设置超时
                httpURLConnection.setReadTimeout(3000);//设置超时
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");//用get的方式来建立连接
                httpURLConnection.setRequestProperty("Charset", "UTF-8");//格式为中文
                if (httpURLConnection.getResponseCode() == 200) {//观察是否可以连接，可以的话返回200
                    Log.d(TAG, "onCreate: sada");
                    InputStream is = httpURLConnection.getInputStream();
                    while ((len = is.read(data)) != -1) {
                        os.write(data, 0, len);
                    }
                    is.close();
                    Log.d(TAG, "onCreate: as" + is);
                    Log.d(TAG, "run: " + os);
                }
                String jsons = new String(os.toByteArray());
                JSONArray jsA = null;
                try {
                    jsA = new JSONArray(jsons);
                    for(int i=0;i<jsA.length();i++){
                        try {

                            Map<String,Object> listitem= new HashMap<String ,Object>();
                            JSONObject jsO=(JSONObject)jsA.get(i);
                            goodsNews goodsnews=new goodsNews();


                            goodsnews.setGoodsName(jsO.getString("goodsname"));
                            goodsnews.setGoodsvalue(jsO.getInt("goodsvalue"));
                            goodsnews.setGoodssum(jsO.getInt("goodssum"));
                            listitem.put("name",goodsnews.getGoodsName());

                            listitem.put("sum",goodsnews.getGoodssum());
                            listitem.put("value",goodsnews.getGoodsvalue());
                            list.add(listitem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<Map<String, Object>> getList() {
            return list;
        }
    }
}

