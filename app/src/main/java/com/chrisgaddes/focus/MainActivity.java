
package com.chrisgaddes.focus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button btn_load_second_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_load_second_activity = (Button) findViewById(R.id.btn_load_second_activity);
        btn_load_second_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSecondActivity();
            }
        });

//        SwipeStack swipeStack = (SwipeStack) findViewById(R.id.swipeStack);
//        swipeStack.setAdapter(new SwipeStackAdapter(mData));

    }


    private void loadSecondActivity() {
//        Intent intent = new Intent(this, RecyclerViewDisplayActivity.class);

//        intent.putExtra(Constant.LAYOUT_MANAGER, Constant.LINEAR_LAYOUT_MANAGER);

        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}