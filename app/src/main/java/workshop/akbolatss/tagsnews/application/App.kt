package workshop.akbolatss.tagsnews.application

import android.app.Application
import android.support.v7.app.AppCompatDelegate

import com.orhanobut.hawk.Hawk

import workshop.akbolatss.tagsnews.di.component.AppComponent
import workshop.akbolatss.tagsnews.di.component.DaggerAppComponent
import workshop.akbolatss.tagsnews.di.module.AppModule

import workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(applicationContext, BASE_URL))
                .build()

        Hawk.init(applicationContext).build()

        if (Hawk.get(SELECTED_THEME, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //        Fresco.initialize(getApplicationContext());
    }

    companion object {

        private const val BASE_URL = "http://cloud.feedly.com/"
    }
}
