package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.repositories.DBRssItemRepository;
import workshop.akbolatss.tagsnews.repositories.RssItemRepository;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesView;

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

}
