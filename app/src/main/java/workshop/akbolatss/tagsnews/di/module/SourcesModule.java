package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.model.dao.DaoSession;
import workshop.akbolatss.tagsnews.screen.sources.SourcesView;


@Module
public class SourcesModule {

    private SourcesView mView;

    public SourcesModule(SourcesView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    SourcesView provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBRssSourceRepository provideDbRssSourceRepository(DaoSession daoSession, NewsApiService newsApiService) {
        return new DBRssSourceRepository(daoSession, newsApiService);
    }
}
