package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DESTINATION = "destination";
    private static final String DEPART = "depart";
    private static final String RETURN = "return";
    private static final String NUM_TRAVELLERS = "numTravellers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button) findViewById(R.id.search_btn);
        search.setOnClickListener(this);

        Button quick = (Button) findViewById(R.id.quick_nav);
        quick.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.quick_nav:
                Intent intent = new Intent();
                intent.putExtra(DESTINATION, "Paris");
                intent.putExtra(DEPART, "");
                intent.putExtra(RETURN, "");
                intent.putExtra(NUM_TRAVELLERS, 1);
                intent.setClass(this, HotelStreamActivity.class);
                startActivity(intent);
                break;
        }
    }
}
