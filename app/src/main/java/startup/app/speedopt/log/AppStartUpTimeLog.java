package startup.app.speedopt.log;

import android.util.Log;

import java.util.ArrayList;

import startup.app.speedopt.BuildConfig;

/**
 * Created by popfisher on 2016/11/15.
 * 统计启动时间的日志
 */

public class AppStartUpTimeLog {

    private final static String TAG = AppStartUpTimeLog.class.getSimpleName();

    private static long mLastTime = 0;
    private static long mFirstTime = 0;
    private static String mFirstMarkStr = "";
    private static String mColdStartTag = "";

    public static ArrayList<TimeNoteData> mTimeNoteDataArrayList = new ArrayList<TimeNoteData>();

    /**
     * 是否是冷启动，true为冷启动，false为热启动
     */
    public static boolean isColdStart = false;

    private final static boolean mEnabled = BuildConfig.DEBUG;

    /**
     * 记录开始计时的位置
     *
     * @param markStr
     */
    public static void markStartTime(String markStr, boolean coldStart) {
        if (!mEnabled) {
            return;
        }

        mColdStartTag = coldStart ? "[[ 冷启动 ]]\n" : "[[ 热启动 ]]\n";
        mTimeNoteDataArrayList.clear();
        mFirstTime = System.currentTimeMillis();
        mLastTime = mFirstTime;
        mFirstMarkStr = markStr;

        TimeNoteData timeNoteData = new TimeNoteData();
        timeNoteData.tag = mFirstMarkStr;
        mTimeNoteDataArrayList.add(timeNoteData);
    }

    /**
     * 输出与上一次log的时间差
     *
     * @param markStr
     */
    public static void logTimeDiff(String markStr) {
        logTimeDiff(markStr, false);
    }

    /**
     * 输出与上一次log的时间差
     *
     * @param markStr
     */
    public static void logTimeDiff(String markStr, boolean isNeedPrintLog) {
        if (!mEnabled) {
            return;
        }
        long time = System.currentTimeMillis();

        TimeNoteData timeNoteData = new TimeNoteData();
        timeNoteData.tag = markStr;
        timeNoteData.timeDiff = time - mLastTime;
        timeNoteData.totalTime = time - mFirstTime;
        mTimeNoteDataArrayList.add(timeNoteData);

        mLastTime = time;

        if (isNeedPrintLog) {
            printLog();
        }
    }

    private static void printLog() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mColdStartTag);

        for (TimeNoteData timeNoteData : mTimeNoteDataArrayList) {
            stringBuilder.append("[" + timeNoteData.tag + "] (" + timeNoteData.timeDiff + "，" + timeNoteData.totalTime + ")");
            stringBuilder.append(" --> \n");
        }
        stringBuilder.append("[[ End ]]");
        Log.i(TAG, stringBuilder.toString());
    }
}
