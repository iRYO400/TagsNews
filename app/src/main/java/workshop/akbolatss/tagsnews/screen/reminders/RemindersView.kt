package workshop.akbolatss.tagsnews.screen.reminders

import workshop.akbolatss.tagsnews.base.BaseView
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

interface RemindersView : BaseView {

    fun onUpdateReminders()

    fun onAddReminder()

    fun onShowReminders(reminderItems: List<ReminderItem>)
}
