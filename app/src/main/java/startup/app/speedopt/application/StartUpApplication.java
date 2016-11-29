package startup.app.speedopt.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.SystemClock;

import startup.app.speedopt.log.AppLog;
import startup.app.speedopt.log.AppStartUpTimeLog;
import startup.app.speedopt.utils.BlockingUtil;

/**
 * Created by popfisher on 2016/11/21.
 */

public class StartUpApplication extends Application {

    @Override
    public void onCreate() {
        // 程序创建时调用，次方法应该执行应该尽量快，否则会拖慢整个app的启动速度
        super.onCreate();
        AppStartUpTimeLog.isColdStart = true;   // 设置为冷启动标志
        AppLog.log("StartUpApplication onCreate");
        AppStartUpTimeLog.logTimeDiff("Application onCreate");
        BlockingUtil.simulateBlocking(500); // 模拟阻塞100毫秒
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AppLog.log("StartUpApplication attachBaseContext");
        AppStartUpTimeLog.markStartTime("Application attachBaseContext", true);
        BlockingUtil.simulateBlocking(200); // 模拟阻塞100毫秒
    }

    @Override
    public void onLowMemory() {
        // 程序剩余内存低时调用
        super.onLowMemory();
        AppLog.log("StartUpApplication onLowMemory");
    }

    @Override
    public void onTerminate() {
        // 程序终止时调用，真机环境几乎不会调用
        super.onTerminate();
        AppLog.log("StartUpApplication onTerminate");
    }

    @Override
    public void onTrimMemory(int level) {
        // 内存清理时调用
        super.onTrimMemory(level);
        AppLog.log("StartUpApplication onTrimMemory level: " + level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 程序配置变化时调用，比如转屏
        super.onConfigurationChanged(newConfig);
        AppLog.log("StartUpApplication onConfigurationChanged");
    }
}
