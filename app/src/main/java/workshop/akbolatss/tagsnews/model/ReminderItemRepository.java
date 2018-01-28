package workshop.akbolatss.tagsnews.model;


import java.util.List;

import io.reactivex.Single;
import workshop.akbolatss.tagsnews.model.dao.ReminderItem;

public interface ReminderItemRepository {

    Single<List<ReminderItem>> onLoadReminders();

    void onAddReminder(ReminderItem rItem);

    void onRemoveReminder(ReminderItem rItem);

    void onUpdateReminder(ReminderItem rItem);

    void onActivateReminder(ReminderItem rItem);

    void onDeactivateReminder(ReminderItem rItem);
}
