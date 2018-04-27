package workshop.akbolatss.tagsnews.screen.splash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

import javax.inject.Inject;

import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.model.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.model.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.model.dao.ReminderItem;
import workshop.akbolatss.tagsnews.model.dao.RssSource;
import workshop.akbolatss.tagsnews.screen.reminders.ReminderReceiver;
import workshop.akbolatss.tagsnews.util.Constants;
import workshop.akbolatss.tagsnews.util.Logger;

import static android.content.Context.ALARM_SERVICE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE;

public class SplashPresenter extends BasePresenter<SplashView> {

    @Inject
    protected Context mContext;

    @Inject
    protected DBRssSourceRepository mRepository;

    @Inject
    protected DBReminderItemRepository mReminderRepository;

    @Inject
    public SplashPresenter() {
    }

    public void onInitBaseSources() {

        if (!isFirstInit()) {
            RssSource rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("DTF.ru");
            rssSource.setLink("https://dtf.ru/rss/all");
            rssSource.setDescription("DTF — игры, разработка, монетизация, продвижение");
            rssSource.setPositionIndex(0);
            mRepository.initDefaultSource(rssSource);

            ReminderItem rItem = new ReminderItem();
            rItem.setIsActive(true);
            rItem.setHour(14);
            rItem.setMinute(46);
            rItem.setPM_AM("AM");
            rItem.setRequestCode(-100);
            mReminderRepository.onAddReminder(rItem);

            onActivateNotification(rItem);

            rItem = new ReminderItem();
            rItem.setIsActive(false);
            rItem.setHour(18);
            rItem.setMinute(0);
            rItem.setPM_AM("PM");
            rItem.setRequestCode(-200);
            mReminderRepository.onAddReminder(rItem);
        }
    }

    private void onActivateNotification(ReminderItem rItem) {
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
        calendar.set(Calendar.SECOND, 0);

        Logger.Companion.i("TAG", "Calendar " + calendar.getTimeInMillis());

        amc.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    private boolean isFirstInit() {
        if (Hawk.get(Constants.FIRST_START, false)) {
            return true;
        } else {
            Hawk.put(Constants.FIRST_START, true);
            return false;
        }
    }
}
