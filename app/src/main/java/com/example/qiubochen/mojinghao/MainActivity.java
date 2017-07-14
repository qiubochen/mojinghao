package com.example.qiubochen.mojinghao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button personbutton;
    Button goodbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        personbutton=(Button) findViewById(R.id.person);//人物查询的按钮
        personbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent personintent=new Intent(MainActivity.this,personsearch.class);
                startActivity(personintent);
            }
        });
        goodbutton=(Button)findViewById(R.id.goods);//货物查询的按钮
        goodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goodintent=new Intent(MainActivity.this,Goods.class);
                startActivity(goodintent);
            }
        });
    }
}
