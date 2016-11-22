package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mttnow.fluttr.managers.HotelStreamManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    HotelStreamManager hotelStreamManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button begin = (Button) findViewById(R.id.begin_btn);
        begin.setOnClickListener(this);

        hotelStreamManager = new HotelStreamManager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.begin_btn:
                startActivity(new Intent(this, PresenterActivity.class));
                break;
        }
    }
}
