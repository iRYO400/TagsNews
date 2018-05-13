package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssItemRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesView

/**
 * Dagger2 module for FavoritesComponent
 * @see workshop.akbolatss.tagsnews.di.component.FavoritesComponent
 */
@Module
class FavoritesModule(private val mView: FavoritesView) {

    /**
     * MVP View Provider
     * @see FavoritesView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): FavoritesView {
        return mView
    }

    /**
     * DB Repository provider for Rss item
     * @see DBRssItemRepository
     */
    @ActivityScope
    @Provides
    internal fun provideRepository(daoSession: DaoSession): DBRssItemRepository {
        return DBRssItemRepository(daoSession)
    }
}
