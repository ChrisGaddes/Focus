package com.chrisgaddes.focus;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.chrisgaddes.focus.databinding.ActivityOtherBinding;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daprlabs.aaron.swipedeck.SwipeDeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OtherActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OtherActivity";
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

    private ActivityOtherBinding binding;
    private boolean click;
    private int mActivePointerId;
    private float initialXPress;
    private float initialYPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other);

        int statusBarHeight = getStatusBarHeight();

        setStatusBarTranslucent(true);
        addPaddingForStatusBar();

        // TODO remove this hardcodedness
        size_panel = dpToPx(54 * 4 + 24);

        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (binding.includedToolbarLayout.toolbar != null) {
            setSupportActionBar(binding.includedToolbarLayout.toolbar);
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

//        startCountdownTimer(3000, 1000);

//        swipe_down_bar = toolbar;
        debugging_text = (TextView) findViewById(R.id.debugging_text);

        // Swipe down "menu"
        binding.includedToolbarLayout.toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        click = true;
                        //gesture has begun
                        float x;
                        float y;

                        //cancel any current animations
                        v.clearAnimation();

                        mActivePointerId = event.getPointerId(0);

                        x = event.getX();
                        y = event.getY();

                        // TODO remove this nonsense
                        if (!panel_open) {
                            dY = v.getY() - event.getRawY();
                        } else {
                            dY = size_panel - event.getRawY();
                        }

                        initialXPress = x;
                        initialYPress = y;

                        break;

                    case MotionEvent.ACTION_MOVE:
                        //gesture is in progress

                        final int pointerIndex = event.findPointerIndex(mActivePointerId);
                        // prevents multiple touches
                        if (pointerIndex < 0 || pointerIndex > 0) {
                            break;
                        }

                        final float xMove = event.getX(pointerIndex);
                        final float yMove = event.getY(pointerIndex);

                        //calculate distance moved
                        final float dx = xMove - initialXPress;
                        final float dy = yMove - initialYPress;

                        Log.d("X:", "" + v.getX());

                        //calc rotation here
                        float posX = binding.mainLayout.getX() + dx;
                        float posY = binding.mainLayout.getY() + dy;

                        //in this circumstance consider the motion a click
                        if (Math.abs(dx + dy) > 5) click = false;


//                        String text_debug = "event.getRawY: " + String.valueOf(event.getRawY()) + "\n panel: " + String.valueOf(size_panel) + "\n dY: " + String.valueOf(dY);
//                        binding.debuggingText.setText(text_debug);

                        // animates mainLayout
                        binding.mainLayout.animate()
                                //.x(event.getRawX() + dX)
                                .y(posY)
                                .setDuration(0)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
                        //gesture has finished

                        dY = v.getY() - event.getRawY();

                        //check if this is a click event and then perform a click
                        if (click) {
                            // v.performClick();
                        }

                        // TODO fix interpolators to not always close on touch
                        if (!panel_open) {
                            if (event.getRawY() < dpToPx(75)) {
                                binding.mainLayout.animate()
                                        .y(event.getRawY() + dY)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = false;
                            } else if (event.getRawY() >= dpToPx(75)) {
                                binding.mainLayout.animate()
                                        .y(size_panel)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = true;
                            }
                        } else {

                            if (event.getRawY() < size_panel - dY - dpToPx(125)) {
                                binding.mainLayout.animate()
                                        .y(event.getRawY() + dY)
                                        .setDuration(200)
                                        .setInterpolator(new AccelerateDecelerateInterpolator())
                                        .start();
                                panel_open = false;

                            } else if (event.getRawY() >= size_panel - dY - dpToPx(125)) {
                                binding.mainLayout.animate()
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


        CompoundButton.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String key = null;
                        switch (buttonView.getId()) {
                            case R.id.ckbx_spades:
                                key = "key1";
                                use_spades = isChecked;
                                break;
                            case R.id.ckbx_hearts:
                                key = "key2";
                                use_hearts = isChecked;
                                break;
                            case R.id.ckbx_clubs:
                                key = "key2";
                                use_clubs = isChecked;
                                break;
                            case R.id.ckbx_diamonds:
                                key = "key2";
                                use_diamonds = isChecked;
                                break;
                            default:
                                return;
                        }
                        // Save the state here using key
                        loadSuitsCards();
                    }
                };

        binding.includedOptionsPanel.ckbxSpades.setOnCheckedChangeListener(listener);
        binding.includedOptionsPanel.ckbxHearts.setOnCheckedChangeListener(listener);
        binding.includedOptionsPanel.ckbxClubs.setOnCheckedChangeListener(listener);
        binding.includedOptionsPanel.ckbxDiamonds.setOnCheckedChangeListener(listener);

//
//        binding.checkboxDrag = (CheckBox)             findViewById(R.id.checkbox_drag);


        testData = new ArrayList<>();
        for (
            // remove hardcoded 52
                int i = 0;
                i < 52; i++)

        {
            testData.add(String.valueOf(i));
        }

        initializeImages();

        setSwipeAdapter(testData);


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

    private void setSwipeAdapter(ArrayList<String> data) {
        // sets SwipeDeckAdapter with testData Arraylist
        adapter = new SwipeDeckAdapter(data, this);
//        if (binding.swipeDeck != null) {
        binding.swipeDeck.setAdapter(adapter);
//        }

        binding.swipeDeck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long stableId) {
                Log.i("OtherActivity", "card was swiped left, position in adapter: " + stableId);
            }

            @Override
            public void cardSwipedRight(long stableId) {
                Log.i("OtherActivity", "card was swiped right, position in adapter: " + stableId);

            }

//            @Override

            public boolean isDragEnabled(long itemId) {
                return dragCheckbox.isChecked();
            }
        });
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
                startCountdownTimer(5000, 1000);
//                refresh();
                return true;
            case R.id.menu_pause:
//                stopTimer();
                loadSuitsCards();
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
        DisplayMetrics displayMetrics = OtherActivity.this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

//    //Not used currently
//    public int pxToDp(int px) {
//        DisplayMetrics displayMetrics = OtherActivity.this.getResources().getDisplayMetrics();
//        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
//        return dp;
//    }


    private void startCountdownTimer(long millisInFuture, long countDownInterval) {

        binding.countdownView.setVisibility(View.VISIBLE);
        binding.countdownViewBackground.setVisibility(View.VISIBLE);

        new CountDownTimer(millisInFuture, countDownInterval) {

            public void onTick(long millisUntilFinished) {
                YoYo.with(Techniques.FadeIn)
                        .duration(700)
                        .playOn(binding.countdownView);
                String str = String.valueOf(millisUntilFinished / 1000 +1);
                binding.countdownView.setText(str);

                if (millisUntilFinished < 2500) {
                    YoYo.with(Techniques.FadeOut)
                            .duration(700)
                            .playOn(binding.countdownViewBackground);
                }

                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.countdownView.setVisibility(View.GONE);
                binding.countdownViewBackground.setVisibility(View.GONE);
                refresh();
            }

        }.start();

//        for (int n = 3; n >= 0; n--) {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(700)
//                    .playOn(binding.countdownView);
//
////            binding.countdownView.setAlpha();
//            binding.countdownView.setText(String.valueOf(n));
    }


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

                getDrawableResourceID(OtherActivity.this, str_probCurrent_file_name);


//            Picasso.with(context).load(R.drawable.food).fit().centerCrop().into(imageView);

                Glide.with(OtherActivity.this)
                        .load(getDrawableResourceID(OtherActivity.this, str_probCurrent_file_name))
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
                    // runs if clicked on card

//                    Log.i("Layer type: ", Integer.toString(v.getLayerType()));
//                    Log.i("Hardware Accel type:", Integer.toString(View.LAYER_TYPE_HARDWARE));
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
//        Collections.addAll(images, Constant.CLUBS);
//        SwipeDeckAdapter.getItemId();
    }

    private void loadSuitsCards() {
        initializeImages();

        // TODO remove hardcoding
        use_clubs = true;
        use_diamonds = true;
        use_spades = true;
        use_hearts = true;

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

    // A method to find height of the status bar //TODO update this to support multiwindow mode
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void addPaddingForStatusBar() {
        binding.outerFrameLayout.setPadding(0, getStatusBarHeight(), 0, 0); //getStatusBarHeight()
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

// Supposedly, this is the only way to get multiwindow transparent statusbar and nav bar dimensions
//        ViewCompat.setOnApplyWindowInsetsListener(view, new OnApplyWindowInsetsListener() {
//@Override
//public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
//    final int statusBar = insets.getSystemWindowInsetTop();
//    final int navigationBar = insets.getSystemWindowInsetBottom();
//    return insets;
//}
//});
