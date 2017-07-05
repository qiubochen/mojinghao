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

public class persondetail extends AppCompatActivity {
    private Toolbar detailTool;
    String TAG = "persondetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondetail);
        detailTool = (Toolbar) findViewById(R.id.deToolBar);
        setSupportActionBar(detailTool);
        detailTool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int to = item.getItemId();
                if (to == R.id.tool) {//tool是共用的，我不确定是否有问题
                    Intent i = new Intent(persondetail.this, createDeal.class);
                    startActivity(i);
                }
                return false;
            }

        });
        detailThread deThread=new detailThread();
        deThread.start();
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,deThread.getList(),R.layout.persondetail_item,new String[]{"name","num","sell","value"},new int[]{R.id.persondetail_item_name,R.id.persondetail_item_num,R.id.persondetail_item_sell,R.id.persondetail_item_value});
        ListView listView=(ListView)findViewById(R.id.persondetail_listview);//listview的排布
        listView.setAdapter(simpleAdapter);//设置adapter
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolba, menu);
        return true;
    }

    class detailThread extends Thread {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        @Override
        public void run() {
            try {
                URL url = new URL("http://10.0.2.2:8080/test8/pesondetailServlet");
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
                            peDetailNews peDetailNews=new peDetailNews();


                            peDetailNews.setGoodsname(jsO.getString("goodsname"));
                            peDetailNews.setGoodsnum(jsO.getInt("goodssell"));
                            peDetailNews.setGoodssell(jsO.getInt("goodsnum"));
                            peDetailNews.setGoodsvalue(jsO.getInt("goodsvalue"));
                            listitem.put("name",peDetailNews.getGoodsname());
                            listitem.put("num",peDetailNews.getGoodsnum());
                            listitem.put("sell",peDetailNews.getGoodssell());
                            listitem.put("value",peDetailNews.getGoodsvalue());
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