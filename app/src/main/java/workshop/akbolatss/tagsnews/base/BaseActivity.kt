package workshop.akbolatss.tagsnews.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import me.yokeyword.swipebackfragment.SwipeBackActivity
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.AppComponent
import workshop.akbolatss.tagsnews.screen.splash.SplashActivity

abstract class BaseActivity : SwipeBackActivity() {

    protected val appComponent: AppComponent
        get() = (application as App).appComponent

    protected abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        if (this !is SplashActivity) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }

        onInitDagger()
        onViewReady(savedInstanceState, intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    protected open fun onInitDagger() {}


    @CallSuper
    protected open fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        //For Child activities
    }
}
