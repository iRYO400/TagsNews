package workshop.akbolatss.tagsnews.screen.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import workshop.akbolatss.tagsnews.util.Logger;

import static workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE;
import static workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE;


public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.Companion.i("TAG", " Request code " + intent.getIntExtra(INTENT_REQUEST_CODE, -1)
                + " Hour " + intent.getIntExtra(INTENT_HOUR, -1)
                + " Minute " + intent.getIntExtra(INTENT_MINUTE, -1));
        Intent myIntent = new Intent(context, ReminderService.class);
        myIntent.putExtra(INTENT_REQUEST_CODE, intent.getIntExtra(INTENT_REQUEST_CODE, -1));
        myIntent.putExtra(INTENT_HOUR, intent.getIntExtra(INTENT_HOUR, -1));
        myIntent.putExtra(INTENT_MINUTE, intent.getIntExtra(INTENT_MINUTE, -1));
        context.startService(myIntent);
    }
}
