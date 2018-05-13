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
import android.view.MotionEvent
import workshop.akbolatss.tagsnews.screen.reminders.ReminderService
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object UtilityMethods {

    /**
     * Check is WiFi is Connected
     */
    fun isWiFiConnected(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo

        return wifiInfo.networkId != -1
    }

    private const val ONE_DAY_INTERVAL = 24 * 60 * 60 * 1000L //hours*seconds*minutes*millis
    /**
     * Start Job in JobScheduler
     */
    fun scheduleJob(context: Context, requestCode: Int) {
        val serviceComponent = ComponentName(context, ReminderService::class.java)
        val builder = JobInfo.Builder(requestCode, serviceComponent)
//        builder.setPeriodic(ONE_DAY_INTERVAL)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
//        builder.setRequiresCharging(true) // we don't care if the device is charging or not
        val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    /**
     * Stop Job from JobScheduler
     */
    fun scheduleOffJob(context: Context, requestCode: Int) {
        val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(requestCode)
    }

    /**
     * RSS time converter, e.g. "Sat, 24 Apr 2018 14:01:00 GMT" to "5 hours ago"
     */
    fun convertTime(timestamp: String): String {
        val mDataFormat: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
        try {
            val date = mDataFormat.parse(timestamp)
            val niceDateStr = DateUtils.getRelativeTimeSpanString(date.time, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
            return niceDateStr.toString()
        } catch (e: ParseException) {
            Log.e("ParseException", "Unparseable date " + e.message)
            return timestamp
        }
    }

    /**
     * Calculate direction of moving touches
     */
    fun getDirection(x1: Float, y1: Float, x2: Float, y2: Float): Direction {
        val angle = getAngle(x1, y1, x2, y2)
        return Direction[angle]
    }

    /**
     * Helper methods to calculate angle
     */
    private fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        val rad = Math.atan2((y1 - y2).toDouble(), (x2 - x1).toDouble()) + Math.PI
        return (rad * 180 / Math.PI + 180) % 360
    }

    /**
     * Get distance between old and new touch coordinates(X, Y)
     */
    fun getDistance(ev: MotionEvent, startX: Float, startY: Float): Float {
        var distanceSum = 0f

        val dx = ev.getX(0) - startX
        val dy = ev.getY(0) - startY
        distanceSum += Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

        return distanceSum
    }

    /**
     * Enum for declared Directions
     */
    enum class Direction {
        NOT_DETECTED,
        UP,
        DOWN,
        LEFT,
        RIGHT;

        companion object {

            operator fun get(angle: Double): Direction {
                return if (inRange(angle, 45f, 135f)) {
                    Direction.UP
                } else if (inRange(angle, 0f, 45f) || inRange(angle, 315f, 360f)) {
                    Direction.RIGHT
                } else if (inRange(angle, 225f, 315f)) {
                    Direction.DOWN
                } else {
                    Direction.LEFT
                }
            }

            private fun inRange(angle: Double, init: Float, end: Float): Boolean {
                return angle >= init && angle < end
            }
        }
    }
}
