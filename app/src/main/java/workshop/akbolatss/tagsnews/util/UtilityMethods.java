package workshop.akbolatss.tagsnews.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import static android.content.Context.WIFI_SERVICE;


public class UtilityMethods {

    public static boolean isWifiConnected(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (wifiInfo.getNetworkId() == -1) {
            return false;
        } else {
            return true;
        }
    }
}
