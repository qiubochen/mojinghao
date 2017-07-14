package com.example.qiubochen.mojinghao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class createDeal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceate_deal);
        Button button=(Button)findViewById(R.id.adddeal);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrdealThread crdealThread=new CrdealThread();
                crdealThread.start();
            }
        });
    }
    class CrdealThread extends Thread{
        @Override
        public void run() {
            EditText editTextgoodsname=(EditText)findViewById(R.id.edgoodsname);
            EditText editTextgoodsvalue=(EditText)findViewById(R.id.edgoodsvalue);
            EditText editTextgoodsnum=(EditText)findViewById(R.id.edgoodsnum);
            String strname=editTextgoodsname.getText().toString();
            String strvalue=editTextgoodsvalue.getText().toString();
            String strnum=editTextgoodsnum.getText().toString();
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("goodsname",strname).put("goodsvalue",strvalue).put("goodsnum",strnum);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URL url=new URL("http://10.0.2.2:8080/test8/pesondetailServlet");
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                OutputStream oos = httpURLConnection.getOutputStream();
                String jsonstr=jsonObject.toString();
                String TAG="crgoods";
                Log.d(TAG, "run: "+jsonstr);
                oos.write(jsonstr.getBytes());
                oos.flush();
                oos.close();
                if(httpURLConnection.getResponseCode()==200)
                    System.out.print("请求成功");
                else
                    System.out.print("请求失败");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
