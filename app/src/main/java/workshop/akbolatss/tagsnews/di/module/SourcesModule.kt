package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.sources.SourcesView

/**
 * Dagger2 module for SourcesComponent
 * @see workshop.akbolatss.tagsnews.di.component.SourcesComponent
 */
@Module
class SourcesModule(private val mView: SourcesView) {

    /**
     * MVP View Provider
     * @see SourcesView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): SourcesView {
        return mView
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
