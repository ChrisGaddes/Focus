package com.chrisgaddes.focus;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.chrisgaddes.focus.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMenuBinding binding;

//    final ViewGroup transitionsContainer = (ViewGroup) view.findViewById(R.id.transitions_container);
//    final TextView text = (TextView) transitionsContainer.findViewById(R.id.text);
//    final Button button = (Button) transitionsContainer.findViewById(R.id.button);
    boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);

        User user = new User("Test", "User");
        binding.setUser(user);

        binding.loadMainActivity.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.load_main_activity:
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MenuActivity.this);
                Intent intent = new Intent(MenuActivity.this, OtherActivity.class);
                startActivity(intent); //(intent, options.toBundle())
                break;

            default:
                break;
        }
    }










    // object
    public class User {
        private final String firstName;
        private final String lastName;
        public User(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        public String getFirstName() {
            return this.firstName;
        }
        public String getLastName() {
            return this.lastName;
        }
    }

    public class Callbacks {
        public View.OnClickListener clickListener;
        public void nameChanged(Editable editable) {
            //...
        }

    }

    public static class Handlers {

        private static int getCurrentIntValue(TextView view) {
            try {
                return Integer.parseInt(view.getText().toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        public static void increment(TextView view, int max) {
//            int value = getCurrentIntValue(view);
//            binding.setQuantity(Math.max(max, value + 1));
        }

        public static void decrement(View view, int min) {
//            int value = getCurrentIntValue(view);
//            binding.setQuantity(Math.min(min, value - 1));
        }
    }
}
