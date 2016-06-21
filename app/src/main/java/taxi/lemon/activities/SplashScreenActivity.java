package taxi.lemon.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import taxi.lemon.R;

public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Delay for displaying splash screen
     */
    public static final int DELAY = 3 * 1000;

    /**
     * Step for countdown timer
     */
    public static final int STEP = 1000;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new CountDownTimer(DELAY, STEP) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        }.start();
    }
}
