package com.mttnow.fluttr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mttnow.fluttr.service.hotels.HotelsService;

public class PresenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenter);
    }

}
