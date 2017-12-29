package workshop.akbolatss.tagsnews.repositories;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;

public interface ReminderItemRepository {

    Single<List<ReminderItem>> onLoadReminders();

    void onAddReminder(ReminderItem rItem);

    void onRemoveReminder(ReminderItem rItem);

    void onUpdateReminder(ReminderItem rItem);

    void onActivateReminder(ReminderItem rItem);

    void onDeactivateReminder(ReminderItem rItem);
}
