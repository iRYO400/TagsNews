package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.board.BoardView

/**
 * Dagger2 module for BoardComponent
 * @see workshop.akbolatss.tagsnews.di.component.BoardComponent
 */
@Module
class BoardModule(private val mView: BoardView) {

    /**
     * MVP View Provider
     * @see BoardView
     */
    @ActivityScope
    @Provides
    internal fun provideView(): BoardView {
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
