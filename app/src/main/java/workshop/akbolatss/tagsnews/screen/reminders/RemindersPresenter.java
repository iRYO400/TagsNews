package workshop.akbolatss.tagsnews.screen.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;

import static android.content.Context.ALARM_SERVICE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE;

public class RemindersPresenter extends BasePresenter<RemindersView> {

    @Inject
    protected Context mContext;

    @Inject
    protected DBReminderItemRepository mRepository;

    @Inject
    public RemindersPresenter() {
    }

    public void onActivateNotification(ReminderItem rItem, boolean isNewAdded){
        if (!isNewAdded) {
            mRepository.onActivateReminder(rItem);
        }

        AlarmManager amc = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(mContext, ReminderReceiver.class);
        myIntent.putExtra(INTENT_REQUEST_CODE, rItem.getRequestCode());
        myIntent.putExtra(INTENT_HOUR, rItem.getHour());
        myIntent.putExtra(INTENT_MINUTE, rItem.getMinute());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, rItem.getRequestCode(), myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, rItem.getHour());
        calendar.set(Calendar.MINUTE, rItem.getMinute());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            amc.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            amc.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void onDeactivateNotification(ReminderItem rItem) {
        mRepository.onDeactivateReminder(rItem);

        AlarmManager amc = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(mContext, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, rItem.getRequestCode(), myIntent, 0);
        amc.cancel(pendingIntent);
    }

    public void onAddReminder(ReminderItem rItem){
        onActivateNotification(rItem, true);
        mRepository.onAddReminder(rItem);
        getView().onUpdateReminders();
    }

    public void onUpdate(ReminderItem rItem) {
        mRepository.onUpdateReminder(rItem);
        getView().onUpdateReminders();
    }

    public void onRemoveReminder(ReminderItem rItem){
        onDeactivateNotification(rItem);
        mRepository.onRemoveReminder(rItem);
        getView().onUpdateReminders();
    }

    public void onLoadReminders() {
        mRepository.onLoadReminders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<List<ReminderItem>>() {
                    @Override
                    public void onSuccess(List<ReminderItem> reminderItems) {
                        getView().onShowReminders(reminderItems);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
