package com.example.qiubochen.mojinghao;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.system.Os.read;

// TODO: 2017/7/11 改名字
public class personsearch extends AppCompatActivity {

    Button btnpersondetail;
    private Toolbar peTool;//toolbar
    private final static String TAG = "personsearch";
    EditText idEd=null;
    EditText peEd=null;
    TextView teView;
    // 全局的listView

    // listView adapter 自定义，添加一个setData（List）

    // adapter.setData()   MainThread

    // setData : nodifiedDataChange();

    // 1 全部数据
    // 2 搜索
    // flag

    // if (flag  == 1)
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personsearch);
        peEd = (EditText) findViewById(R.id.personName);
        idEd = (EditText) findViewById(R.id.personId);
        teView=(TextView)findViewById(R.id.personsearch_item_id);
        peTool = (Toolbar) findViewById(R.id.Petoolbar);
        setSupportActionBar(peTool);//设置toolbar
        peTool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int to = item.getItemId();
                if (to == R.id.tool) {
                    Intent i = new Intent(personsearch.this, createPeron.class);
                    startActivity(i);
                }
                return false;
            }
        });


        // btnp   android:background="#B5B5B5"ersondetail=(Button)findViewById(R.id.btnforpersondetail);
        // btnpersondetail.setOnClickListener(new View.OnClickListener() {
        //   @Override
        // public void onClick(View v) {
        //   Intent intent=new Intent(personsearch.this,persondetail.class);//跳转到详细情况中去
        // startActivity(intent);
        //}
        //});

        //搜索按钮
        Button btnforsort = (Button) findViewById(R.id.btnsort);
        //按钮触发事件
        btnforsort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取数据的线程
                // TODO: 2017/7/11 获取名字和id
                String peString = peEd.getText().toString();
                String idString = idEd.getText().toString();
                Log.d(TAG,"string"+peString+" "+idString );


                //listview

                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(peString,idString);


            }
        });
        myThread my = new myThread("NewsListServlet");
        my.start();
        AdapterMeth(my);
        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {//刷新操作
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        myThread my1 = new myThread("NewsListServlet");
                        my1.start();
                        AdapterMeth(my1);
                    }
                }, 500);
            }
        });
    }

    // string  exeture的参数类型
    // object
    // doinbackground
    public class MyAsyncTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] params) {

                List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("personname", params[0]).put("id", params[1]);
                    URL url = new URL("http://morjoe.cc/duankou.php");
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url.openConnection();

                    httpURLConnection1.setDoInput(true);
                    httpURLConnection1.setDoOutput(true);
                    httpURLConnection1.setRequestMethod("POST");
                    httpURLConnection1.setUseCaches(false);
                    httpURLConnection1.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                if (httpURLConnection1.getResponseCode()==200)
//                    System.out.print("sort请求成功");
                    OutputStream outputstream = httpURLConnection1.getOutputStream();
                    String str = jsonObject.toString();
                    Log.d(TAG, "run: " + str);
                    outputstream.write(str.getBytes());
                    Log.d(TAG, "ouputstream:" + outputstream);

                    outputstream.flush();

                    outputstream.close();
//                if(httpURLConnection1.getResponseCode()==200) {
                    Log.d(TAG, "close");
                    InputStream input = httpURLConnection1.getInputStream();
                    Log.d(TAG, "InP " + input);
                    int le = -1;
                    byte[] b = new byte[1024];
                    while ((le = input.read(b)) != -1) {
                        baos.write(b, 0, le);
                    }
                    input.close();
                    String jsaa = new String(baos.toByteArray());
                    Log.d(TAG, "jsa " + jsaa);
                    JSONArray jsA1 = null;
                    try {
                        jsA1 = new JSONArray(jsaa);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsA1.length(); i++) {
                        Map<String, Object> listitem = new HashMap<String, Object>();
                        JSONObject jsO = (JSONObject) jsA1.get(i);
                        personNews psn = new personNews();


                        psn.setId(jsO.getString("id"));
                        psn.setLv(jsO.getInt("class"));
                        psn.setName(jsO.getString("personname"));
                        psn.setOverage(jsO.getInt("overage"));
                        listitem.put("name", psn.getName());
                        listitem.put("Id", psn.getId());
                        listitem.put("lv", psn.getLv());
                        listitem.put("Overage", psn.getOverage());
                        list2.add(listitem);
                    }
//                }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return list2;
        }

        @Override
        protected void onPostExecute( Object mm) {
            List<Map<String, Object>> li=(List<Map<String, Object>>)mm;
            ListView listView1 = (ListView) findViewById(R.id.personsearch_listview);//listview的排布

            SimpleAdapter simpleAdapter1 = new SimpleAdapter(personsearch.this, li, R.layout.personsearch_item, new String[]{"name", "Id", "lv", "Overage"}, new int[]{R.id.personsearch_item_name, R.id.personsearch_item_id, R.id.personsearch_item_lv, R.id.personsearch_item_stock});

            listView1.setAdapter(simpleAdapter1);//设置adapter

            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(personsearch.this, persondetail.class);//跳转到详细情况中去
                    String textStr=teView.getText().toString();
                    intent.putExtra("perId",textStr);

                    startActivity(intent);
                }
            });
//            MyThread2 t = (MyThread2) mm;
//
//            AdapterMeth2(t);
            // list

            // adapter.setData()

            // TODO: 2017/7/11 这个方法是在主线程运行
        }
    }

    void AdapterMeth(myThread my) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, my.getList(), R.layout.personsearch_item, new String[]{"name", "Id", "lv", "Overage"}, new int[]{R.id.personsearch_item_name, R.id.personsearch_item_id, R.id.personsearch_item_lv, R.id.personsearch_item_stock});
         ListView listView = (ListView) findViewById(R.id.personsearch_listview);//listview的排布
//       listView.invalidate();
//       listView.postInvalidate();

        listView.setAdapter(simpleAdapter);//设置adapter
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(personsearch.this, persondetail.class);//跳转到详细情况中去
                Log.d(TAG,"hahaha");

                String textStr=teView.getText().toString();
                Log.d(TAG,"hahah"+textStr);
                intent.putExtra("perId",textStr);
//                Log.d(TAG,"hahaha"+idEd.getText().toString());
                startActivity(intent);
            }
        });
    }

//    void AdapterMeth2(MyThread2 my) {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolba, menu);
        return true;
    }

//    class MyThread2 extends Thread {
//
//        }


    class myThread extends Thread {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String servlet;

        myThread(String ser) {
            servlet = ser;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("http://morjoe.cc/duankou.php");
                byte[] data = new byte[1024];

                int len = 0;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//建立连接
                httpURLConnection.setConnectTimeout(3000);//设置超时
                httpURLConnection.setReadTimeout(3000);//设置超时
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");//用get的方式来建立连接
//                httpURLConnection.setRequestMethod("POST");
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
//                myThread myt = null;
//                Log.d(TAG, "onCreate1: "+myt.getOs());
//                JSONArray jsA = null;
//                try {
//                    jsA=new JSONArray(myt.getOs());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                String jsons = new String(os.toByteArray());
                JSONArray jsA = null;
                try {
                    jsA = new JSONArray(jsons);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //List<Map<String ,Object>>list=new ArrayList<Map<String,Object>>();
                for (int i = 0; i < jsA.length(); i++) {
                    try {

                        Map<String, Object> listitem = new HashMap<String, Object>();
                        JSONObject jsO = (JSONObject) jsA.get(i);
                        personNews psn = new personNews();


                        psn.setId(jsO.getString("id"));
                        psn.setLv(jsO.getInt("class"));
                        psn.setName(jsO.getString("personname"));
                        psn.setOverage(jsO.getInt("overage"));
                        listitem.put("name", psn.getName());
                        listitem.put("Id", psn.getId());
                        listitem.put("lv", psn.getLv());
                        listitem.put("Overage", psn.getOverage());
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
