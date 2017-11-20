package workshop.akbolatss.tagsnews.screen.splash;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.orhanobut.hawk.Hawk;

import java.util.Calendar;
import java.util.Random;

import javax.inject.Inject;

import workshop.akbolatss.tagsnews.base.BasePresenter;
import workshop.akbolatss.tagsnews.repositories.DBReminderItemRepository;
import workshop.akbolatss.tagsnews.repositories.DBRssSourceRepository;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItemDao;
import workshop.akbolatss.tagsnews.repositories.source.RssSource;
import workshop.akbolatss.tagsnews.screen.reminders.ReminderReceiver;
import workshop.akbolatss.tagsnews.util.Constants;

import static android.content.Context.ALARM_SERVICE;

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

        if (!isAlreadyInited()) {
            RssSource rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("DTF.ru");
            rssSource.setLink("https://dtf.ru/rss/all");
            rssSource.setDescription("DTF — игры, разработка, монетизация, продвижение");
            rssSource.setPositionIndex(0);
            mRepository.initDefaultSource(rssSource);

            rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("3DNews.ru");
            rssSource.setLink("https://3dnews.ru/software-news/rss/");
            rssSource.setDescription("");
            rssSource.setPositionIndex(1);
            mRepository.initDefaultSource(rssSource);

            rssSource = new RssSource();
            rssSource.setIsActive(true);
            rssSource.setTitle("Kanobu.ru: статьи");
            rssSource.setLink("http://kanobu.ru/rss/best/");
            rssSource.setDescription("Статьи от KANOBU.ru: обзоры, интервью, рецензиии");
            rssSource.setPositionIndex(2);
            mRepository.initDefaultSource(rssSource);

            Random random = new Random();

            ReminderItem rItem = new ReminderItem();
            rItem.setIsActive(true);
            rItem.setHour(9);
            rItem.setMinute(0);
            rItem.setPM_AM("AM");
            rItem.setRequestCode(0);
            mReminderRepository.onAddReminder(rItem);

            onActivateNotification(rItem);

            rItem = new ReminderItem();
            rItem.setIsActive(false);
            rItem.setHour(13);
            rItem.setMinute(0);
            rItem.setPM_AM("PM");
            rItem.setRequestCode(1);
            mReminderRepository.onAddReminder(rItem);

            rItem = new ReminderItem();
            rItem.setIsActive(false);
            rItem.setHour(19);
            rItem.setMinute(0);
            rItem.setPM_AM("PM");
            rItem.setRequestCode(2);
            mReminderRepository.onAddReminder(rItem);
        }
    }

    private void onActivateNotification(ReminderItem rItem){
        AlarmManager amc = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(mContext, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, rItem.getRequestCode(), myIntent, 0);

        // Set the alarm to start at 9:00 AM
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

    public boolean isAlreadyInited() {
        if (Hawk.contains(Constants.FIRST_START)){
            return true;
        } else {
            Hawk.put(Constants.FIRST_START, 1);
            return false;
        }
    }
}
