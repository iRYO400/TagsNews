package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.board.BoardView

@Module
class BoardModule(private val mView: BoardView) {

    @ActivityScope
    @Provides
    internal fun provideView(): BoardView {
        return mView
    }

    @ActivityScope
    @Provides
    internal fun provideDbRssSourceRepository(daoSession: DaoSession, newsApiService: NewsApiService): DBRssSourceRepository {
        return DBRssSourceRepository(daoSession, newsApiService)
    }
}
