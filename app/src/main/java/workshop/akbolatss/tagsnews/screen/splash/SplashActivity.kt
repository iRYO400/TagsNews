package workshop.akbolatss.tagsnews.screen.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer

import javax.inject.Inject

import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerSplashComponent
import workshop.akbolatss.tagsnews.di.module.SplashModule
import workshop.akbolatss.tagsnews.screen.board.BoardActivity

class SplashActivity : BaseActivity(), SplashView {

    @Inject
    lateinit var mPresenter: SplashPresenter

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)

        DaggerSplashComponent.builder()
                .appComponent(appComponent)
                .splashModule(SplashModule(this))
                .build()
                .inject(this)

        initNewsSources()
    }

    private fun initNewsSources() {
        mPresenter.onInitBaseSources()
        object : CountDownTimer(300, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, BoardActivity::class.java))
                finish()
            }
        }.start()
    }

    override fun onBackPressed() {}

    override fun getContentView(): Int {
        return R.layout.activity_splash
    }
}
