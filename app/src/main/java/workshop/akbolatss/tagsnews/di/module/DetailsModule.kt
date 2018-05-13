package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.details.DetailsView

/**
 * Dagger2 module for DetailsComponent
 * @see workshop.akbolatss.tagsnews.di.component.DetailsComponent
 */
@Module
class DetailsModule(private val mView: DetailsView) {

    /**
     * MVP View Provider
     * @see DetailsView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): DetailsView {
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

    /**
     * DB Repository provider for Rss sources
     * @see DBRssSourceRepository
     */
    @ActivityScope
    @Provides
    internal fun provideDbRssSourceRepository(daoSession: DaoSession, newsApiService: NewsApiService): DBRssSourceRepository {
        return DBRssSourceRepository(daoSession, newsApiService)
    }
}
