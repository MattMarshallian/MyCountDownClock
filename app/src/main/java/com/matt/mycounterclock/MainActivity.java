package com.matt.mycounterclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int RESULT_SETTINGS = 1;
    private Handler handler = new Handler();
    private TextView textView;
    private int seconds = 10;
    MediaPlayer mediaPlayer1;
    MediaPlayer mediaPlayer2;
    MediaPlayer mediaPlayerSec;
    MediaPlayer mediaPlayerEnd;
    private boolean isRunning = false;
    private boolean isBeepEnabled = true;
    SharedPreferences sharedPrefs;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        seconds = Integer.parseInt(sharedPrefs.getString("prefTime", "10"));

        if (!isRunning) {
            mediaPlayerSec.start();
            handler.postDelayed(runnable, 100);
        }
        isRunning = true;

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        isBeepEnabled = sharedPrefs.getBoolean("prefBeep", true);
        String sec = sharedPrefs.getString("prefTime", "10");
        seconds = Integer.parseInt(sec);

        textView = (TextView) findViewById(R.id.clock);
        textView.setText(String.format("%d:%02d", seconds / 60, seconds % 60));
        textView.getBackground().setAlpha(50);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer1 = MediaPlayer.create(this, R.raw.beep2);
        mediaPlayer2 = MediaPlayer.create(this, R.raw.beep1);
        mediaPlayerSec = MediaPlayer.create(this, R.raw.beep_sec);
        mediaPlayerEnd = MediaPlayer.create(this, R.raw.beep_end);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer1.release();
        mediaPlayer2.release();
        mediaPlayerSec.release();
        mediaPlayerEnd.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, RESULT_SETTINGS);
                break;

            case R.id.stopClock:
                Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
                isRunning = false;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();

                isBeepEnabled = sharedPrefs.getBoolean("prefBeep", true);
                String sec = sharedPrefs.getString("prefTime", "10");
                seconds = Integer.parseInt(sec);
                textView.setText(String.format("%d:%02d", seconds / 60, seconds % 60));
                break;

        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (seconds >= 0 && isRunning) {
                textView.setText(String.format("%d:%02d", seconds / 60, seconds % 60));

                if (isBeepEnabled) {
                    if (seconds > 6) {
                        mediaPlayerSec.start();
                    }

                    if (4 <= seconds && seconds <= 6) {
                        mediaPlayer1.start();
                    }

                    if (1 <= seconds && seconds <= 3) {
                        mediaPlayer2.start();
                    }

                    if (seconds == 0) {
                        mediaPlayerEnd.start();
                    }
                }
                handler.postDelayed(this, 1000);
                seconds--;
            }
            else {
                isRunning = false;
            }
        }
    };
}
