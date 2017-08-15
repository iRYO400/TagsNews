package workshop.akbolatss.tagsnews.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.module.AppModule;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;

/**
 * Created by AkbolatSS on 08.08.2017.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    NewsApiService exposeApi();

    Context exposeContext();

    DaoSession exposeDaoSession();
}
