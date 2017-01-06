package startup.app.speedopt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ListView;

import startup.app.speedopt.interfaces.IFirstDrawListener;
import startup.app.speedopt.log.AppLog;
import startup.app.speedopt.log.AppStartUpTimeLog;
import startup.app.speedopt.utils.BlockingUtil;

public class AppStartUpMainActivity extends Activity {

    /** 是否是第一次获取焦点 */
    private boolean mIsFirstFocus = true;
    private MainRootView mMainRootView;
    private ListView mTimeLogListView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.log("Activity onCreate");
        if (AppStartUpTimeLog.isColdStart) {
            AppStartUpTimeLog.isColdStart = false;
        } else {
            AppStartUpTimeLog.markStartTime("Activity onCreate", false);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AppStartUpTimeLog.logTimeDiff("Activity onCreate start", false, true);

        BlockingUtil.simulateBlocking(300);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity setContentView start");
        setContentView(R.layout.activity_app_start_up_main);
        AppStartUpTimeLog.logTimeDiff("Activity setContentView end");

        initView();
        BlockingUtil.simulateBlocking(200);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onCreate end");
    }

    private IFirstDrawListener mMainFirstDrawListener = new IFirstDrawListener() {
        @Override
        public void onFirstDrawFinish() {
            AppLog.log("Activity MainRootView onFirstDrawFinish");
        }

        @Override
        public void onFirstMeasureFinish() {
            AppLog.log("Activity MainRootView onFirstMeasureFinish");
        }

        @Override
        public void onFirstLayoutFinish() {
            AppLog.log("Activity MainRootView onFirstLayoutFinish");
        }
    };

    private void initViewTreeListener() {
        mMainRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                AppLog.log("Activity ViewTreeObserver onGlobalLayout");
            }
        });
    }

    private void initView() {
        mTimeLogListView = (ListView) findViewById(R.id.time_log_listview);
        mMainRootView = (MainRootView) findViewById(R.id.activity_app_start_up_main);
        mMainRootView.setFirstDrawListener(mMainFirstDrawListener);
        initViewTreeListener();
        FirstDrawLayoutRoot firstDrawLayoutRootOne = (FirstDrawLayoutRoot) findViewById(R.id.root_layout_one);
        firstDrawLayoutRootOne.setFirstDrawListener(new IFirstDrawListener() {

            @Override
            public void onFirstMeasureFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne onFirstMeasureFinish");
            }

            @Override
            public void onFirstLayoutFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne onFirstLayoutFinish");
            }

            @Override
            public void onFirstDrawFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne onFirstDrawFinish");
            }
        });
        FirstDrawListenView firstDrawLayoutRootOneChild = (FirstDrawListenView) findViewById(R.id.root_layout_one_child);
        firstDrawLayoutRootOneChild.setFirstDrawListener(new IFirstDrawListener() {

            @Override
            public void onFirstMeasureFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne Child onFirstMeasureFinish");
            }

            @Override
            public void onFirstLayoutFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne Child onFirstLayoutFinish");
            }

            @Override
            public void onFirstDrawFinish() {
                AppLog.log("Activity FirstDrawLayoutRootOne Child onFirstDrawFinish");
            }
        });
        FirstDrawLayoutRoot firstDrawLayoutRootTwo = (FirstDrawLayoutRoot) findViewById(R.id.root_layout_two);
        firstDrawLayoutRootTwo.setFirstDrawListener(new IFirstDrawListener() {

            @Override
            public void onFirstMeasureFinish() {
                AppLog.log("Activity FirstDrawLayoutRootTwo onFirstMeasureFinish");
            }

            @Override
            public void onFirstLayoutFinish() {
                AppLog.log("Activity FirstDrawLayoutRootTwo onFirstLayoutFinish");
            }

            @Override
            public void onFirstDrawFinish() {
                AppLog.log("Activity FirstDrawLayoutRootTwo onFirstDrawFinish");
            }
        });
        FirstDrawListenView firstDrawLayoutRootTwoChild = (FirstDrawListenView) findViewById(R.id.root_layout_two_child);
        firstDrawLayoutRootTwoChild.setFirstDrawListener(new IFirstDrawListener() {

            @Override
            public void onFirstMeasureFinish() {
                AppLog.log("Activity FirstDrawLayoutRootTwo Child onFirstMeasureFinish");
            }

            @Override
            public void onFirstLayoutFinish() {
                AppLog.log("Activity FirstDrawLayoutRootTwo Child onFirstLayoutFinish");
            }

            @Override
            public void onFirstDrawFinish() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        AppStartUpTimeLog.logTimeDiff("Activity onFirstDrawFinish", true);
//                        onLazyInit();
//                    }
//                });
                AppLog.log("Activity FirstDrawLayoutRootTwo Child onFirstDrawFinish");
                AppStartUpTimeLog.logTimeDiff("onFirstDrawFinish start", false, true);
                onLazyInit();
                BlockingUtil.simulateBlocking(200);
                AppStartUpTimeLog.logTimeDiff("onFirstDrawFinish end", true, false);
            }
        });
    }

    @Override
    protected void onResume() {
        AppLog.log("Activity onResume");
        AppStartUpTimeLog.logTimeDiff("Activity onResume start", false, true);

        super.onResume();

        // 这个方式统计时间有点晚了，已经看到界面了到时候，主进程也不一定是空闲的
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                // 主进程空闲时调用，也可以用第一次回调统计主界面加载到显示的时间，但是延迟加载的话可能会影响这个结果
                AppLog.log("queueIdle");
                return false;
            }
        });

        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onResume end", true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppLog.log("Activity onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLog.log("Activity onPause");
    }

    @Override
    public void  onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AppLog.log("Activity onWindowFocusChanged " + hasFocus);
        if (hasFocus && mIsFirstFocus) {
            mIsFirstFocus = false;
            AppStartUpTimeLog.logTimeDiff("FocusChanged true start", false, true);
            BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒
            AppStartUpTimeLog.logTimeDiff("FocusChanged true end");
        }
    }

    private void onLazyInit() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTimeLogListView.setAdapter(new TimeLogAdapter(getBaseContext(), AppStartUpTimeLog.mTimeNoteDataArrayList));
            }
        });
    }
}
