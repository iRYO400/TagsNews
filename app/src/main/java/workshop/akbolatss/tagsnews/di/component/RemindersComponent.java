package workshop.akbolatss.tagsnews.di.component;


import dagger.Component;
import workshop.akbolatss.tagsnews.di.module.RemindersModule;
import workshop.akbolatss.tagsnews.di.scope.ActivityScope;
import workshop.akbolatss.tagsnews.screen.reminders.ReminderService;
import workshop.akbolatss.tagsnews.screen.reminders.RemindersActivity;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = RemindersModule.class)
public interface RemindersComponent {

    void inject(RemindersActivity activity);

    void inject(ReminderService reminderService);
}
