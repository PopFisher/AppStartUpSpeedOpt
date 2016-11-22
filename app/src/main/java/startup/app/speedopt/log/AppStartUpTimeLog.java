package startup.app.speedopt.log;

        import android.util.Log;

        import startup.app.speedopt.BuildConfig;

/**
 * Created by popfisher on 2016/11/15.
 * 统计启动时间的日志
 */

public class AppStartUpTimeLog {

    private final static String TAG = AppStartUpTimeLog.class.getSimpleName();

    private static long mLastTime = 0;
    private static long mFirstTime = 0;
    private static String mLastMarkStr = "";
    private static String mFirstMarkStr = "";

    /**
     * 是否是冷启动，true为冷启动，false为热启动
     */
    public static boolean isColdStart = false;

    private final static boolean mEnabled = BuildConfig.DEBUG;

    /**
     * 记录开始计时的位置
     * @param markStr
     */
    public static void markStartTime(String markStr) {
        if (!mEnabled) {
            return;
        }
        mFirstTime = System.currentTimeMillis();
        mLastTime = mFirstTime;
        mFirstMarkStr = markStr;
        mLastMarkStr = mFirstMarkStr;
        Log.d(TAG, isColdStart ? "\n[冷启动] " : "\n[热启动]");
        Log.d(TAG, "From [" + markStr + "] start the timer");
    }

    /**
     * 输出与上一次log的时间差
     * @param markStr
     */
    public static void logTimeDiff(String markStr) {
        if (!mEnabled) {
            return;
        }
        long time = System.currentTimeMillis();
        Log.d(TAG, "[" + mLastMarkStr + "] -> " + "[" + markStr + "] time cost: " + (time - mLastTime));
        mLastTime = time;
        mLastMarkStr = markStr;
    }

    /**
     * 输出当前位置到开始计时点的总时间差
     * @param markStr
     */
    public static void logCurTotalTime(String markStr) {
        if (!mEnabled) {
            return;
        }
        Log.d(TAG, "[" + mFirstMarkStr + "] --> [" + markStr + "] total time cost：" + (System.currentTimeMillis() - mFirstTime));
        mLastMarkStr = markStr;
    }
}
