package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
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
