package workshop.akbolatss.tagsnews.application;

import android.app.Application;
import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import workshop.akbolatss.tagsnews.di.component.AppComponent;
import workshop.akbolatss.tagsnews.di.component.DaggerAppComponent;
import workshop.akbolatss.tagsnews.di.module.AppModule;

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


//        try {
//            Reservoir.init(this, 2048);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Hawk.init(getApplicationContext()).build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
