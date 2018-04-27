package workshop.akbolatss.tagsnews.application;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.hawk.Hawk;

import workshop.akbolatss.tagsnews.di.component.AppComponent;
import workshop.akbolatss.tagsnews.di.component.DaggerAppComponent;
import workshop.akbolatss.tagsnews.di.module.AppModule;

import static workshop.akbolatss.tagsnews.util.Constants.SELECTED_THEME;

public class App extends Application {

    private static final String BASE_URL = "http://cloud.feedly.com/";
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(getApplicationContext(), BASE_URL))
                .build();

        Hawk.init(getApplicationContext()).build();

        if (Hawk.get(SELECTED_THEME, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Fresco.initialize(getApplicationContext());
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
