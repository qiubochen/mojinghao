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

public class createGoods extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goods);
        Button btnforcr=(Button)findViewById(R.id.addGoods);



        btnforcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrgoodsThread crgoods=new CrgoodsThread();
                crgoods.start();

            }
        });

    }

    class CrgoodsThread extends Thread{
        @Override
        public void run() {
            EditText editTextname=(EditText) findViewById(R.id.crgoodsname);
            EditText editTextvalue=(EditText)findViewById(R.id.crgoodsvalue);
            String strna= editTextname.getText().toString();
            String strva= editTextvalue.getText().toString();
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("goodsname",strna).put("goodsvalue",strva);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URL url=new URL("http://10.0.2.2:8080/test8/goodsServelet");
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
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
