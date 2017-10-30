package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.news.NewsView;


@Module
public class NewsListModule {

    private NewsView mView;

    public NewsListModule(NewsView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    NewsView provideView(){
        return mView;
    }
}
