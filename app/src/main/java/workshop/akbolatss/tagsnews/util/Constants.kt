package workshop.akbolatss.tagsnews.util

object Constants {

    /**
     * URL for API calls
     */
    const val BASE_URL = "http://cloud.feedly.com/"
    /**
     * Share info
     */
    const val SHARE_INFO = "Tag News (Beta) bit.ly/TagNewsApp"
    /**
     * Data Base name. That will actually created on smartphone's memory
     */
    const val DB_NAME = "tagnews.db"

    /**
     * Package names for Facebook, VK.com, Twitter
     */
    const val FB_PACKAGE_NAME = "com.facebook.katana"
    const val VK_PACKAGE_NAME = "com.vkontakte.android"
    const val TW_PACKAGE_NAME = "com.twitter.android"

    /**
     * Intent keys. Used when passing data between screens
     */
    const val INTENT_RSS_FEED_ITEM = "RssFeedItem"
    const val INTENT_REQUEST_CODE = "RequestCode"
    const val INTENT_HOUR = "ReminderHour"
    const val INTENT_MINUTE = "ReminderMinute"
    const val FIRST_START = "ITS_FIRST"

    const val NOTIFICATION_ID = 500
    const val NOTIFICATION_CHANNEL_ID = "workshop.akbolatss.tagsnews.channel"
    const val NOTIFICATION_CHANNEL_NAME = "TagNews"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "TagNews - RSS Newsletter"

    const val SELECTED_THEME = "VIEW_THEME" // False = Day, True = Night

    const val ITEM_VIEW_MAIN = 1
}
