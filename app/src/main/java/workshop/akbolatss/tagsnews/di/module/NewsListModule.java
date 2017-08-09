package workshop.akbolatss.tagsnews.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.news.NewsView;

/**
 * Created by AkbolatSS on 08.08.2017.
 */
@Module
public class NewsListModule {

    private NewsView mView;

    public NewsListModule(NewsView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    NewsView proviewView(){
        return mView;
    }
}
