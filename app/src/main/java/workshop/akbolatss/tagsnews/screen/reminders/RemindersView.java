package workshop.akbolatss.tagsnews.screen.reminders;

import java.util.List;

import workshop.akbolatss.tagsnews.base.BaseView;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;

public interface RemindersView extends BaseView {

    void onUpdateReminders();

    void onAddReminder();

    void onShowReminders(List<ReminderItem> reminderItems);
}
