package digitalpromo.cabsdemo.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Flag for indication if user pressed back button twice
     */
    protected Boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() < 1) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Для выхода нажмите кнопку назад еще раз", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }
}
