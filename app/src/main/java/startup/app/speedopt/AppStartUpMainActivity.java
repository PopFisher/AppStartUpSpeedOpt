package startup.app.speedopt;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import startup.app.speedopt.log.AppStartUpTimeLog;
import startup.app.speedopt.utils.BlockingUtil;

public class AppStartUpMainActivity extends Activity implements MainRootView.IFirstDrawListener {

    /** 是否是第一次获取焦点 */
    private boolean mIsFirstFocus = true;
    private MainRootView mMainRootView;

    private Handler mHandler = new Handler();

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

        initView();
        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onCreate end");
        AppStartUpTimeLog.logCurTotalTime("Activity onCreate end");
    }

    private void initView() {
        mMainRootView = (MainRootView) findViewById(R.id.activity_app_start_up_main);
        mMainRootView.setFirstDrawListener(this);
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
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BlockingUtil.simulateBlocking(10);  // 模拟阻塞100毫秒
                    AppStartUpTimeLog.logTimeDiff("Activity onWindowFocusChanged true end");
                    AppStartUpTimeLog.logCurTotalTime("Activity onWindowFocusChanged true end");
                }
            });
        }
    }

    @Override
    public void onFirstDrawFinish() {
        AppStartUpTimeLog.logTimeDiff("Activity onFirstDrawFinish");
        AppStartUpTimeLog.logCurTotalTime("Activity onFirstDrawFinish");
        onLazyInit();
    }

    private void onLazyInit() {

    }
}
