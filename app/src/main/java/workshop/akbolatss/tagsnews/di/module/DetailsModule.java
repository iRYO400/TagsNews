package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.repositories.DBRssItemRepository;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.details.DetailsView;

@Module
public class DetailsModule {

    private DetailsView mView;

    public DetailsModule(DetailsView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    DetailsView provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBRssItemRepository provideRepository(DaoSession daoSession) {
        return new DBRssItemRepository(daoSession);
    }
}
