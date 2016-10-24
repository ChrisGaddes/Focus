package com.chrisgaddes.focus;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

public class ChronometerView extends TextView implements Runnable {
    private long startTime = 0L;
    private long beginTime = 0L;
    private long overallDuration = 0L;
    private long warningDuration = 0L;
    private boolean isRunning = false;
    private long elapsedSeconds;

    public ChronometerView(Context context) {
        super(context);

        reset();
    }

    public ChronometerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        reset();
    }

    @Override
    public void run() {
        isRunning = true;

        elapsedSeconds = (SystemClock.elapsedRealtime() - startTime);

//        if (elapsedSeconds <= overallDuration) {
//            long remainingSeconds = elapsedSeconds;
//            long minutes = remainingSeconds / 60;
//            long seconds = remainingSeconds - (60 * minutes);
//            long milliseconds = (remainingSeconds/1000)/100;


        String time_duration = getTimeDurationAsString(elapsedSeconds);

        setText(time_duration);

//            setText(String.format("%d:%02d:%03d", minutes, seconds,milliseconds));

//            if (elapsedSeconds >= overallDuration - warningDuration) {
//                setTextColor(0xFFFF6600); // orange
//            } else {
//                setTextColor(Color.WHITE);
//            }

            postDelayed(this, 1);
//        } else {
////            setText("0:00");
//            setText("Overtime");
//            setTextColor(Color.WHITE);
//            isRunning = false;
//        }
    }

    public void reset() {
        startTime = SystemClock.elapsedRealtime();
        setText("--:--");
//        setTextColor(Color.WHITE);
    }

    //
    public void setPauseTimeOffset(long pause_time_offset) {
        startTime = startTime + pause_time_offset;
    }

    public void stop() {
        removeCallbacks(this);
        isRunning = false;
    }

    public long getCurrentTime() {
        return elapsedSeconds;
    }

    public boolean isRunning() {
        return (isRunning);
    }

    public void setOverallDuration(long overallDuration) {
        this.overallDuration = overallDuration;
    }

    public void setWarningDuration(long warningDuration) {
        this.warningDuration = warningDuration;
    }

    //    http://stackoverflow.com/a/7651285/6388083
    public static final String getTimeDurationAsString(long milliseconds) {
        int millis  = (int) (milliseconds % 1000);
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append((char)('0' + hours / 10))
                    .append((char)('0' + hours % 10)).append(":");
        }
        sb.append((char)('0' + minutes / 10))
                .append((char)('0' + minutes % 10)).append(":")
                .append((char)('0' + seconds / 10))
                .append((char)('0' + seconds % 10)).append(".")
                .append((char)('0' + millis / 100));
        return sb.toString();
    }
}