package workshop.akbolatss.tagsnews.di.component


import dagger.Component
import workshop.akbolatss.tagsnews.di.module.RemindersModule
import workshop.akbolatss.tagsnews.di.scope.ActivityScope
import workshop.akbolatss.tagsnews.screen.reminders.ReminderService
import workshop.akbolatss.tagsnews.screen.reminders.RemindersActivity

/**
 * Dagger2 Component with specific scope for RemindersActivity, ReminderService
 * @see RemindersModule
 * @see RemindersActivity
 * @see ReminderService
 */
@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(RemindersModule::class)])
interface RemindersComponent {

    fun inject(activity: RemindersActivity)

    fun inject(reminderService: ReminderService)
}
