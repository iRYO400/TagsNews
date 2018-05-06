package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.reminders.RemindersView

@Module
class RemindersModule {

    private var mView: RemindersView? = null

    constructor() {}

    constructor(mView: RemindersView) {
        this.mView = mView
    }

    @ActivityScope
    @Provides
    fun provideView(): RemindersView {
        return mView!!
    }

    @ActivityScope
    @Provides
    internal fun provideDBReminderItemRepository(daoSession: DaoSession): DBReminderItemRepository {
        return DBReminderItemRepository(daoSession)
    }

    @ActivityScope
    @Provides
    internal fun provideDbRssSourceRepository(daoSession: DaoSession, newsApiService: NewsApiService): DBRssSourceRepository {
        return DBRssSourceRepository(daoSession, newsApiService)
    }
}
