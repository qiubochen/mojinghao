package com.example.qiubochen.mojinghao;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class createPeron extends AppCompatActivity {
   String TAG="createPe";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate_peron);
        Button btnforadd=(Button)findViewById(R.id.cradd);
        btnforadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edpersonname=(EditText)findViewById(R.id.cr_personname);
                String stedpersonname=edpersonname.getText().toString();
                EditText edperesonlv=(EditText)findViewById(R.id.cr_personlv);
                String stedpersonlv=edperesonlv.getText().toString();
                EditText edpersonid=(EditText)findViewById(R.id.cr_personid);
                String stedpersonid=edpersonid.getText().toString();
                EditText edpersonoverage=(EditText)findViewById(R.id.cr_personoverage);
                Log.d(TAG, "onClick: jjj");
                String stedpersonoverage=edpersonoverage.getText().toString();
                crThread crthread=new crThread(stedpersonname,stedpersonid,stedpersonlv,stedpersonoverage);
                crthread.start();
            }
        });


    }
    class crThread extends Thread{
        String pename,peid,pelv,peoverage;

        public crThread(String name,String id,String lv,String overage){
            pename=name;
            pelv=lv;
            peid=id;
            peoverage=overage;

        }
        @Override
        public void run() {
            try {

//                JSONArray str=new JSONArray();
                JSONObject st=new JSONObject();
                st.put("name",pename).put("id",peid).put("lv",pelv).put("overage",peoverage);
//                str.put(st);
                URL url=new URL("http://10.0.2.2:8080/test8/NewsListServlet");
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                OutputStream oos = httpURLConnection.getOutputStream();
//                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(oos));
                String jsonstr=st.toString();
                Log.d(TAG, "run: "+jsonstr);

                oos.write(jsonstr.getBytes());
                oos.flush();
                oos.close();


//                httpURLConnection.connect();
//                oos.close();
//                bw.write(jsonstr);
//                bw.flush();
//                Log.d(TAG, "run: "+bw);
//                bw.close();
//                oos.close();
                if(httpURLConnection.getResponseCode()==200) {
                    System.out.println("dopost请求成功");
                    //httpURLConnection.setReadTimeout(3000);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
