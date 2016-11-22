package startup.app.speedopt.log;

import android.util.Log;

import startup.app.speedopt.BuildConfig;


/**
 * Created by popfisher on 2016/11/21.
 */

public class AppLog {

    private static final String TAG = AppLog.class.getSimpleName();
    private final static boolean mEnabled = BuildConfig.DEBUG;

    public static void log(String content) {
        if (!mEnabled) {
            return;
        }
        Log.d(TAG, content);
    }

    public static void log(String tag, String content) {
        if (!mEnabled) {
            return;
        }
        Log.d(tag, content);
    }
}
