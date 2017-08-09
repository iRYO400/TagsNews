package workshop.akbolatss.tagsnews.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.module.AppModule;

/**
 * Created by AkbolatSS on 08.08.2017.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    Retrofit exposeRetrofit();

    NewsApiService exposeApi();

    Context exposeContext();
}
