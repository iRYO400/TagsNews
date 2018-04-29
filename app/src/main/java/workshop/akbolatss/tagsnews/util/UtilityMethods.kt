package workshop.akbolatss.tagsnews.util

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.text.format.DateUtils
import android.util.Log
import workshop.akbolatss.tagsnews.screen.reminders.ReminderService
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object UtilityMethods {

    fun isWifiConnected(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        return wifiInfo.networkId != -1
    }

    fun scheduleJob(context: Context, requestCode: Int) {
        val serviceComponent = ComponentName(context, ReminderService::class.java)
        val builder = JobInfo.Builder(requestCode, serviceComponent)
        builder.setPersisted(true)
        builder.setPeriodic(1000 * 60 * 60 * 24) // millis*seconds*minutes*hours
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
//        builder.setRequiresCharging(true) // we don't care if the device is charging or not
        val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    fun scheduleOffJob(context: Context, requestCode: Int) {
        val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(requestCode)
    }

    /**
     * RSS time converter, e.g. "Sat, 24 Apr 2010 14:01:00 GMT"
     */
    fun convertTime(timestamp: String): String {
        val mDataFormat: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
        try {
            val date = mDataFormat.parse(timestamp)
            val niceDateStr = DateUtils.getRelativeTimeSpanString(date.time, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
            Log.d("TAG", "niceData $niceDateStr and date " + date.time)
            return niceDateStr.toString()
        } catch (e: ParseException) {
            Log.e("ParseException", "CYKA BLYAT! Unparseable date " + e.message)
            return timestamp
        }
    }
}
