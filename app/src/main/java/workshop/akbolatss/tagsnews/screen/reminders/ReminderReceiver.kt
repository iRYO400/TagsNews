package workshop.akbolatss.tagsnews.screen.reminders

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import workshop.akbolatss.tagsnews.util.Logger

import workshop.akbolatss.tagsnews.util.Constants.INTENT_HOUR
import workshop.akbolatss.tagsnews.util.Constants.INTENT_MINUTE
import workshop.akbolatss.tagsnews.util.Constants.INTENT_REQUEST_CODE
import workshop.akbolatss.tagsnews.util.UtilityMethods

/**
 * BroadcastReceiver triggers by AlarmManager
 * @see BroadcastReceiver
 */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Logger.i("TAG", " Request code " + intent.getIntExtra(INTENT_REQUEST_CODE, -1))
        UtilityMethods.scheduleJob(context, intent.getIntExtra(INTENT_REQUEST_CODE, -1))
    }
}
