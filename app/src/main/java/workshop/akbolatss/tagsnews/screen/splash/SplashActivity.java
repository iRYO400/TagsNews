package workshop.akbolatss.tagsnews.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import javax.inject.Inject;

import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.application.App;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerSplashComponent;
import workshop.akbolatss.tagsnews.di.module.SplashModule;
import workshop.akbolatss.tagsnews.screen.board.BoardActivity;

/**
 * Created by AkbolatSS on 11.08.2017.
 */

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    protected SplashPresenter mPresenter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        DaggerSplashComponent.builder()
                .appComponent(App.getAppComponent())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);

        initNewsSources();
    }

    private void initNewsSources() {
        mPresenter.onInitBaseSources();
        new CountDownTimer(2000, 1000){
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, BoardActivity.class));
                finish();
            }
        }.start();
    }


    @Override
    public void onBackPressed() {
        //
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}
