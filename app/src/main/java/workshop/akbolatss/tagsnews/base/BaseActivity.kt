package workshop.akbolatss.tagsnews.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity

import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.AppComponent

abstract class BaseActivity : AppCompatActivity() {

    protected val appComponent: AppComponent
        get() = (application as App).appComponent

    protected abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        onViewReady(savedInstanceState, intent)
    }

    @CallSuper
    protected open fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        //For Child activities
    }
}
