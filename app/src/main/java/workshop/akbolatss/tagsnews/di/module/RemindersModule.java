package workshop.akbolatss.tagsnews.di.module;

import dagger.Module;
import dagger.Provides;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.repositories.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.reminders.RemindersView;

@Module
public class RemindersModule {
    private RemindersView mView;

    public RemindersModule(RemindersView mView) {
        this.mView = mView;
    }

    @ActivityScope
    @Provides
    public RemindersView getmView() {
        return mView;
    }

    @ActivityScope
    @Provides
    DBReminderItemRepository provideDBReminderItemRepository(DaoSession daoSession){
        return new DBReminderItemRepository(daoSession);
    }
}
