package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssItemRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.favorites.FavoritesView

@Module
class FavoritesModule(private val mView: FavoritesView) {

    @ActivityScope
    @Provides
    internal fun provideView(): FavoritesView {
        return mView
    }

    @ActivityScope
    @Provides
    internal fun provideRepository(daoSession: DaoSession): DBRssItemRepository {
        return DBRssItemRepository(daoSession)
    }
}
