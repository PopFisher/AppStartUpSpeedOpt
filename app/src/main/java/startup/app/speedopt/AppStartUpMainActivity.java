package startup.app.speedopt;

import android.app.Activity;
import android.os.Bundle;

import startup.app.speedopt.log.AppStartUpTimeLog;
import startup.app.speedopt.utils.BlockingUtil;

public class AppStartUpMainActivity extends Activity {

    /** 是否是第一次获取焦点 */
    private boolean mIsFirstFocus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppStartUpTimeLog.isColdStart) {
            AppStartUpTimeLog.isColdStart = false;
        } else {
            AppStartUpTimeLog.markStartTime("Activity onCreate");
        }
        AppStartUpTimeLog.logTimeDiff("Activity onCreate start");

        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity setContent start");
        setContentView(R.layout.activity_app_start_up_main);
        AppStartUpTimeLog.logTimeDiff("Activity setContent end");

        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onCreate end");
        AppStartUpTimeLog.logCurTotalTime("Activity onCreate end");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppStartUpTimeLog.logTimeDiff("Activity onResume start");

        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onResume end");
        AppStartUpTimeLog.logCurTotalTime("Activity onResume end");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mIsFirstFocus) {
            mIsFirstFocus = false;
            AppStartUpTimeLog.logTimeDiff("Activity onWindowFocusChanged true start");

            BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

            AppStartUpTimeLog.logTimeDiff("Activity onWindowFocusChanged true end");
            AppStartUpTimeLog.logCurTotalTime("Activity onWindowFocusChanged true end");
        }
    }

}
