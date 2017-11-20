package workshop.akbolatss.tagsnews.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import workshop.akbolatss.tagsnews.api.NewsApiService;
import workshop.akbolatss.tagsnews.di.module.AppModule;
import workshop.akbolatss.tagsnews.repositories.source.DaoSession;
import workshop.akbolatss.tagsnews.screen.reminders.ReminderService;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    NewsApiService exposeApi();

    Context exposeContext();

    DaoSession exposeDaoSession();

    void inject(ReminderService reminderService);
}
