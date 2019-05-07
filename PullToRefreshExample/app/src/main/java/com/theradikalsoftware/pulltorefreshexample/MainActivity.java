package com.theradikalsoftware.pulltorefreshexample;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView text;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.mainact_textview_number);
        swipeRefreshLayout = findViewById(R.id.mainact_swiperefreshlayot_swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GenerateRandomNumber();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GenerateRandomNumber();
    }

    private void GenerateRandomNumber(){
        int randNum = (int)(Math.random() * 100 + 1); //0 - 100
        text.setText("Random Number: " + randNum);
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }
}
