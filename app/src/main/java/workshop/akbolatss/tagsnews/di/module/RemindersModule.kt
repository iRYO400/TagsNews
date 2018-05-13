package workshop.akbolatss.tagsnews.di.module

import dagger.Module
import dagger.Provides
import workshop.akbolatss.tagsnews.api.NewsApiService
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository
import workshop.akbolatss.tagsnews.model.dao.DaoSession
import workshop.akbolatss.tagsnews.screen.reminders.RemindersView

/**
 * Dagger2 module for RemindersComponent
 * @see workshop.akbolatss.tagsnews.di.component.RemindersComponent
 */
@Module
class RemindersModule {

    private var mView: RemindersView? = null

    constructor() {}

    constructor(mView: RemindersView) {
        this.mView = mView
    }

    /**
     * MVP View Provider
     * @see RemindersView
     */
    @ActivityScope
    @Provides
    fun provideView(): RemindersView {
        return mView!!
    }

    /**
     * DB Repository provider for Reminder
     * @see DBReminderItemRepository
     */
    @ActivityScope
    @Provides
    internal fun provideDBReminderItemRepository(daoSession: DaoSession): DBReminderItemRepository {
        return DBReminderItemRepository(daoSession)
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
