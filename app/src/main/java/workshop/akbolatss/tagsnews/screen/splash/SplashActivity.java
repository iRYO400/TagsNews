package workshop.akbolatss.tagsnews.screen.splash;

import android.content.Intent;
import android.os.Bundle;

import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;

/**
 * Created by AkbolatSS on 11.08.2017.
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}
