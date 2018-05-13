package workshop.akbolatss.tagsnews.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerSplashComponent
import workshop.akbolatss.tagsnews.di.module.SplashModule
import workshop.akbolatss.tagsnews.screen.board.BoardActivity
import workshop.akbolatss.tagsnews.util.Logger

/**
 * Launch activity
 */
class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var mPresenter: SplashPresenter

    private var mCountDownTimer: CountDownTimer? = null

    companion object {
        const val SPLASH_SCREEN_DURATION = 300L
    }

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        mCountDownTimer = object : CountDownTimer(SPLASH_SCREEN_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, BoardActivity::class.java))
                finish()
            }
        }

        initNewsSources()
    }

    /**
     * Init default values at start
     */
    private fun initNewsSources() {
        if (!mPresenter.isFirstInit) {
            mPresenter.onInitBaseSources()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        mCountDownTimer!!.start()
                    }, {
                        Logger.e("TAG", "SplashActivity: ${it.message}")
                    })
        } else {
            mCountDownTimer!!.start()
        }
    }

    /**
     * Disable back button
     */
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onInitDagger() {
        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .splashModule(SplashModule(this))
                .build()
                .inject(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }
}
