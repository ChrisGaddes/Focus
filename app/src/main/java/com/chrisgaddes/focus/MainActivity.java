package com.chrisgaddes.focus;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chrisgaddes.focus.databinding.ActivityMainBinding;
import com.daprlabs.aaron.swipedeck.SwipeDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private SwipeDeck swipeDeck;
    private Context context = this;
    private SwipeDeckAdapter adapter;
    private ArrayList<String> testData;
    private CheckBox dragCheckbox;

    private boolean use_spades;
    private boolean use_hearts;
    private boolean use_clubs;
    private boolean use_diamonds;


    private boolean timer_running;


    private ArrayList<String> images;
    private String str_probCurrent_file_name;
    //    private Toolbar toolbar;
//    private Toolbar swipe_down_bar;
    private RelativeLayout main_layout;
    private float dX, dY;
    private float dx_down_pt, dy_down_pt;
    private float size_panel;
    private boolean panel_open;
    private TextView debugging_text;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        // TODO remove this hardcodedness
        size_panel = dpToPx(54 * 4 + 24);

        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (binding.includedToolbarLayout.toolbar != null) {
            setSupportActionBar(binding.includedToolbarLayout.toolbar);
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

//        swipe_down_bar = toolbar;
        debugging_text = (TextView) findViewById(R.id.debugging_text);

        // Swipe down "menu"
        binding.includedToolbarLayout.toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        if (!panel_open) {
                            dY = v.getY() - event.getRawY();
                        } else {
                            dY = size_panel - event.getRawY();
                        }

                        break;

                    case MotionEvent.ACTION_MOVE:
                        String text_debug = "event.getRawY: " + String.valueOf(event.getRawY()) + "\n panel: " + String.valueOf(size_panel) + "\n dY: " + String.valueOf(dY);
                        binding.debuggingText.setText(text_debug);

                        binding.mainLayout.animate()
                                //.x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
//                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();

                        // TODO fix interpolators to not always close on touch
                        if (!panel_open) {

                            if (event.getRawY() < dpToPx(75)) {
                                binding.mainLayout.animate()
                                        //.x(event.getRawX() + dX)
                                        .y(event.getRawY() + dY)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = false;

                            } else if (event.getRawY() >= dpToPx(75)) {
                                binding.mainLayout.animate()
                                        //.x(event.getRawX() + dX)
                                        .y(size_panel)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = true;
                            }
                        } else {

                            if (event.getRawY() < size_panel - dY - dpToPx(125)) {
                                binding.mainLayout.animate()
                                        //.x(event.getRawX() + dX)
                                        .y(event.getRawY() + dY)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = false;

                            } else if (event.getRawY() >= size_panel - dY - dpToPx(125)) {
                                binding.mainLayout.animate()
                                        //.x(event.getRawX() + dX)
                                        .y(size_panel)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = true;
                            }
                        }


                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


//        CheckBox ckbx_spades = (CheckBox) findViewById(R.id.ckbx_spades);
//        CheckBox ckbx_hearts = (CheckBox) findViewById(R.id.ckbx_hearts);
//        CheckBox ckbx_clubs = (CheckBox) findViewById(R.id.ckbx_clubs);
//        CheckBox ckbx_diamonds = (CheckBox) findViewById(R.id.ckbx_diamonds);

//        binding.

        // sets listeners on suits checkboxes

        binding.includedOptionsPanel.ckbxSpades.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                use_spades = isChecked;
                // TODO For efficiency, make it so it doesn't load all suits each time a checkbox is changed, but just does it when the settings panel is closed
                loadSuitsCards();
            }
        });

        binding.includedOptionsPanel.ckbxHearts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                use_hearts = isChecked;
                loadSuitsCards();
            }
        });

        binding.includedOptionsPanel.ckbxClubs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                use_clubs = isChecked;
                loadSuitsCards();
            }
        });

        binding.includedOptionsPanel.ckbxDiamonds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                use_diamonds = isChecked;
                loadSuitsCards();
            }
        });


//        swipe_down_bar.setOnTouchListener(new View.OnTouchListener() {


//        hideSystemUI();


// Sets bars translucent
//        setStatusBarTranslucent(true);
//        setNavBarTranslucent(true);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


// Now retrieve the DrawerLayout so that we can set the status bar color.
// This only takes effect on Lollipop, or when using translucentStatusBar
// on KitKat.
//        SwipeFrameLayout swipeFrameLayout = (SwipeFrameLayout) findViewById(R.id.swipeLayout);
//        swipeFrameLayout.setStatusBarBackgroundColor();

//        binding.swipeDeck.setPadding(0, getToolbarHeight(),0, 0); //getStatusBarHeight()
//
//        binding.checkboxDrag = (CheckBox)
//
//                findViewById(R.id.checkbox_drag);


        testData = new ArrayList<>();
        for (
                int i = 0;
                i < 53; i++)

        {
            testData.add(String.valueOf(i));
        }

        initializeImages();

        adapter = new

                SwipeDeckAdapter(testData, this);

        if (binding.swipeDeck != null)

        {
            binding.swipeDeck.setAdapter(adapter);
        }

        binding.swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback()

        {
            @Override
            public void cardSwipedLeft(long stableId) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + stableId);
            }

            @Override
            public void cardSwipedRight(long stableId) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + stableId);

            }

//            @Override

            public boolean isDragEnabled(long itemId) {
                return dragCheckbox.isChecked();
            }
        });

        // sets left and right images which appear on each respective swipe
        binding.swipeDeck.setLeftImage(R.id.left_image);
        binding.swipeDeck.setRightImage(R.id.right_image);

        // sets onClickListeners for views
        binding.buttonLeft.setOnClickListener(this);
        binding.buttonRight.setOnClickListener(this);


        binding.buttonCenter.setOnClickListener(this);
        binding.includedOptionsPanel.btnSpadesIc.setOnClickListener(this);
        binding.includedOptionsPanel.btnHeartsIc.setOnClickListener(this);
        binding.includedOptionsPanel.btnClubsIc.setOnClickListener(this);
        binding.includedOptionsPanel.btnDiamondsIc.setOnClickListener(this);

        // Loads deck
        loadSuitsCards();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_left:
                binding.swipeDeck.swipeTopCardLeft(500);
                break;

            case R.id.button_right:
                binding.swipeDeck.swipeTopCardRight(180);
                break;

            case R.id.button_center:
                binding.swipeDeck.unSwipeCard();
                break;

            case R.id.btn_spades_ic:
                Toast.makeText(context, "Spades", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_hearts_ic:
                Toast.makeText(context, "Hearts", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_clubs_ic:
                Toast.makeText(context, "Clubs", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_diamonds_ic:
                Toast.makeText(context, "Diamonds", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                refresh();
                return true;
            case R.id.menu_pause:
                stopTimer();
                return true;
            case R.id.menu_overflow:
                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // converts dp to pixels
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//    //Not used currently
//    public int pxToDp(int px) {
//        DisplayMetrics displayMetrics = MainActivity.this.getResources().getDisplayMetrics();
//        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return dp;
//    }


    public int getNavBarHeight() {
        // navigation bar height
        int navigationBarHeight = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void startTimer() {
//        timer_view = (ChronometerView) findViewById(R.id.timer_view);
//        rc.setPauseTimeOffset(tinydb.getLong("TotalForegroundTime", 0));
        binding.timerView.setOverallDuration(2 * 600);
        binding.timerView.setWarningDuration(90);
//        rc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        binding.timerView.reset();
        binding.timerView.run();
    }

    private void stopTimer() {
//        addTimeToLog(workout_num, timer_view.getCurrentTime());
        if (binding.timerView != null) {
            binding.timerView.stop();
        }
//        workout_num = workout_num+1;
    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private List<String> data;
        private Context context;

        public SwipeDeckAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                v = inflater.inflate(R.layout.test_card2, parent, false);
            }
            //((TextView) v.findViewById(R.id.textView2)).setText(data.get(position));
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);


            if (!images.isEmpty()) {
                str_probCurrent_file_name = images.get(position);
//        getResources().getIdentifier(str_partCurrent_file_name, "drawable", getPackageName()

                getDrawableResourceID(MainActivity.this, str_probCurrent_file_name);


//            Picasso.with(context).load(R.drawable.food).fit().centerCrop().into(imageView);

                Glide.with(MainActivity.this)
                        .load(getDrawableResourceID(MainActivity.this, str_probCurrent_file_name))
                        .error(R.drawable.ic_empty)
                        .placeholder(R.drawable.card_blank)
                        .into(imageView);
            }


            TextView textView = (TextView) v.findViewById(R.id.sample_text);
            String item = (String) getItem(position);
            textView.setText(item);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
                    Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
                    /*Intent i = new Intent(v.getContext(), BlankActivity.class);
                    v.getContext().startActivity(i);*/
                }
            });
            return v;
        }

    }

    public static int getDrawableResourceID(Context context, String drawableResourceName) {
        return context.getResources().getIdentifier(drawableResourceName, "drawable", context.getPackageName());

    }

    private void initializeImages() {
        images = new ArrayList<>();
        images.clear();
//        SwipeDeckAdapter.getItemId();
    }

    private void loadSuitsCards() {
        initializeImages();
        if (use_clubs) {
            Collections.addAll(images, Constant.CLUBS);
        }

        if (use_diamonds) {
            Collections.addAll(images, Constant.DIAMONDS);
        }

        if (use_spades) {
            Collections.addAll(images, Constant.SPADES);
        }

        if (use_hearts) {
            Collections.addAll(images, Constant.HEARTS);
        }

        shuffleCards();
    }


    private void shuffleCards() {
        Collections.shuffle(images);
    }

    private void refresh() {
//        Log.d(TAG, String.valueOf(swipeDeck.getAdapterIndex()));
        goToTopOfDeck();
//        initializeImages();
        stopTimer();
        startTimer();
    }

    private void goToTopOfDeck() {
        for (int n = 0; n <= binding.swipeDeck.getAdapterIndex(); n++) {
            binding.swipeDeck.unSwipeCard();
            Log.d(TAG, String.valueOf(binding.swipeDeck.getAdapterIndex()));
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}






////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  STUFF THAT IS SAVED FOR LATER
//
////////////////////////////////////////////////////////////////////////////////////////////////////

//    private void setDeckSize(int size) {
//        images.subList(size, images.size()).clear();
//    }
//

//
//    protected void setNavBarTranslucent(boolean makeTranslucent) {
//        if (makeTranslucent) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        } else {
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
//    }
//
//    // A method to find height of the status bar
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//    // Calculate ActionBar height
//    public int getToolbarHeight() {
//        int result = 0;
//        TypedValue tv = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            result = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
//        }
//        return result;
//    }
//
//    public void hideStatusBar(View view) {
//        // Hide status bar
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
//
//    public void showStatusBar(View view) {
//        // Show status bar
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
//

