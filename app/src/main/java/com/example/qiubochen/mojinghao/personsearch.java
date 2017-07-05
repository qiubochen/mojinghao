package com.example.qiubochen.mojinghao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.system.Os.read;

public class personsearch extends AppCompatActivity {

    Button btnpersondetail;
    private Toolbar peTool;//toolbar
   private final static String TAG="personsearch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personsearch);
        peTool =(Toolbar)findViewById(R.id.Petoolbar);
        setSupportActionBar(peTool);//设置toolbar
        peTool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int to=item.getItemId();
                if(to==R.id.tool)
                {
                    Intent i=new Intent(personsearch.this,createPeron.class);
                    startActivity(i);
                }
                return false;
            }
        });
       // btnpersondetail=(Button)findViewById(R.id.btnforpersondetail);
       // btnpersondetail.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View v) {
             //   Intent intent=new Intent(personsearch.this,persondetail.class);//跳转到详细情况中去
               // startActivity(intent);
            //}
        //});
        myThread my=new myThread();
        my.start();
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,my.getList(),R.layout.personsearch_item,new String[]{"name","Id","lv","Overage"},new int[]{R.id.personsearch_item_name,R.id.personsearch_item_id,R.id.personsearch_item_lv,R.id.personsearch_item_stock});
        ListView listView=(ListView)findViewById(R.id.personsearch_listview);//listview的排布
        listView.setAdapter(simpleAdapter);//设置adapter
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(personsearch.this,persondetail.class);//跳转到详细情况中去
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolba,menu);
        return true;
    }

    class myThread extends Thread{
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        List<Map<String ,Object>>list=new ArrayList<Map<String,Object>>();
        @Override
        public void run() {
            try {
                URL url=new URL("http://10.0.2.2:8080/test8/NewsListServlet");
                byte[]data=new byte[1024];

                int len=0;
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();//建立连接
                httpURLConnection.setConnectTimeout(3000);//设置超时
                httpURLConnection.setReadTimeout(3000);//设置超时
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");//用get的方式来建立连接
                httpURLConnection.setRequestProperty("Charset","UTF-8");//格式为中文
                if(httpURLConnection.getResponseCode()==200){//观察是否可以连接，可以的话返回200
                    Log.d(TAG, "onCreate: sada");
                    InputStream is=httpURLConnection.getInputStream();
                    while((len=is.read(data))!=-1){
                        os.write(data,0,len);
                    }
                    is.close();
                    Log.d(TAG, "onCreate: as"+is);
                    Log.d(TAG, "run: "+os);
                }
//                myThread myt = null;
//                Log.d(TAG, "onCreate1: "+myt.getOs());
//                JSONArray jsA = null;
//                try {
//                    jsA=new JSONArray(myt.getOs());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                String jsons=new String(os.toByteArray());
                JSONArray jsA=null;
                try {
                    jsA=new JSONArray(jsons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //List<Map<String ,Object>>list=new ArrayList<Map<String,Object>>();
                for(int i=0;i<jsA.length();i++){
                    try {

                        Map<String,Object> listitem= new HashMap<String ,Object>();
                        JSONObject jsO=(JSONObject)jsA.get(i);
                        personNews psn=new personNews();


                        psn.setId(jsO.getString("id"));
                        psn.setLv(jsO.getInt("class"));
                        psn.setName(jsO.getString("personname"));
                        psn.setOverage(jsO.getInt("overage"));
                        listitem.put("name",psn.getName());
                        listitem.put("Id",psn.getId());
                        listitem.put("lv",psn.getLv());
                        listitem.put("Overage",psn.getOverage());
                        list.add(listitem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
        /*istitem.put("name","asd");
        listitem.put("Id","123");
        listitem.put("lv","1");
        listitem.put("stock","123");*/



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public List<Map<String, Object>> getList() {
            return list;
        }

        public ByteArrayOutputStream getOs() {
            return this.os;
        }
    }

}
