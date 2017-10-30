package workshop.akbolatss.tagsnews.screen.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;
import java.util.Random;

import workshop.akbolatss.tagsnews.R;


public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "The action is " + action);
        if (action.equals(ReminderService.SEND_NOTIFICATION)) {
            String bigText = intent.getStringExtra(ReminderService.NOTIFICATIONS_DATA);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_main)
                    .setContentTitle("Новости к этому часу")
                    .setContentText("Потяните, чтобы увидеть больше")
                    .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(bigText))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
    }
}
