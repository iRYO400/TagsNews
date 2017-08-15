package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.repositories.DBRssItemRepository;
import workshop.akbolatss.tagsnews.repositories.RssItemRepository;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesView;

/**
 * Created by AkbolatSS on 14.08.2017.
 */

@Module
public class FavoritesModule {

    private FavoritesView mView;

    public FavoritesModule(FavoritesView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    FavoritesView provideView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBRssItemRepository provideRepository(DaoSession daoSession) {
        return new DBRssItemRepository(daoSession);
    }
}
