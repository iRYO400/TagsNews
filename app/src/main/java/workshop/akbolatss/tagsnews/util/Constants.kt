package workshop.akbolatss.tagsnews.util

object Constants {

    const val BASE_URL = "http://cloud.feedly.com/"

    const val DB_NAME = "tagnews.db"
    const val FB_PACKAGE_NAME = "com.facebook.katana"
    const val VK_PACKAGE_NAME = "com.vkontakte.android"
    const val TW_PACKAGE_NAME = "com.twitter.android"

    const val INTENT_REQUEST_CODE = "RequestCode"
    const val INTENT_HOUR = "ReminderHour"
    const val INTENT_MINUTE = "ReminderMinute"
    const val FIRST_START = "ITS_FIRST"

    const val NOTIFICATION_ID = 99999
    const val NOTIFICATION_CHANNEL_ID = "workshop.akbolatss.tagsnews.channel"
    const val NOTIFICATION_CHANNEL_NAME = "PaperFeed"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "PaperFeed - RSS Newsletter"

    const val SELECTED_THEME = "VIEW_THEME" // False = Day, True = Night
    const val ITEMS_VIEW_MODE = "ItemsViewMode"

    const val EMPTY_ITEM = -100
    const val ITEM_VIEW_HEAD = 0
    const val ITEM_VIEW_MAIN = 1
    const val ITEM_VIEW_FOOT = 2
}
