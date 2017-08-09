package workshop.akbolatss.tagsnews.application;

import android.app.Application;
import android.support.annotation.NonNull;

import workshop.akbolatss.tagsnews.di.component.AppComponent;
import workshop.akbolatss.tagsnews.di.component.DaggerAppComponent;
import workshop.akbolatss.tagsnews.di.module.AppModule;

/**
 * Created by AkbolatSS on 08.08.2017.
 */

public class App extends Application {

    private static final String BASE_URL = "https://newsapi.org/";
    private static AppComponent mAppComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(getApplicationContext(), BASE_URL))
                .build();

    }

    @NonNull
    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
