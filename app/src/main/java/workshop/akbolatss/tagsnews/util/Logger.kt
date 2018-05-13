package workshop.akbolatss.tagsnews.util

import android.util.Log
import workshop.akbolatss.tagsnews.BuildConfig

/**
 * Logcat helper. Used only in debug mode
 */
class Logger {
    companion object {
        fun i(name: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(name, msg)
            }
        }

        fun i(s: String) {
            if (BuildConfig.DEBUG) {
                Log.d("PaperFeed", s)
            }
        }

        fun e(name: String, msg: String) {
            if (BuildConfig.DEBUG) {
                Log.e(name, msg)
            }
        }

        fun e(s: String) {
            if (BuildConfig.DEBUG) {
                Log.e("PaperFeed", s)
            }
        }
    }
}