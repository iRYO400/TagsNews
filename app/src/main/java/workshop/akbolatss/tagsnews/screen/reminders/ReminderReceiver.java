package workshop.akbolatss.tagsnews.screen.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ReminderReceiver extends BroadcastReceiver {

    private static final String TAG = "TAG";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive Broadcast");
        context.startService(new Intent(context, ReminderService.class));

    }
}
