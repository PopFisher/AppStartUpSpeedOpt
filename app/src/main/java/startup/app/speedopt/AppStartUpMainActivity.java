package startup.app.speedopt;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.ViewTreeObserver;
import android.widget.ListView;

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

        AppStartUpTimeLog.logTimeDiff("Activity onCreate start");

        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity setContentView start");
        setContentView(R.layout.activity_app_start_up_main);
        AppStartUpTimeLog.logTimeDiff("Activity setContentView end");

        initView();
        BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒

        AppStartUpTimeLog.logTimeDiff("Activity onCreate end");
    }

    private FirstDrawLayoutRoot.IFirstDrawListener mMainFirstDrawListener = new FirstDrawLayoutRoot.IFirstDrawListener() {
        @Override
        public void onFirstDrawFinish() {
            AppLog.log("Activity MainRootView onFirstDrawFinish");
            onLazyInit();
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
        firstDrawLayoutRootOne.setFirstDrawListener(new FirstDrawLayoutRoot.IFirstDrawListener() {

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
        FirstDrawLayoutRoot firstDrawLayoutRootOneChild = (FirstDrawLayoutRoot) findViewById(R.id.root_layout_one_child);
        firstDrawLayoutRootOneChild.setFirstDrawListener(new FirstDrawLayoutRoot.IFirstDrawListener() {

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
        firstDrawLayoutRootTwo.setFirstDrawListener(new FirstDrawLayoutRoot.IFirstDrawListener() {

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
        FirstDrawLayoutRoot firstDrawLayoutRootTwoChild = (FirstDrawLayoutRoot) findViewById(R.id.root_layout_two_child);
        firstDrawLayoutRootTwoChild.setFirstDrawListener(new FirstDrawLayoutRoot.IFirstDrawListener() {

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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppStartUpTimeLog.logTimeDiff("Activity onFirstDrawFinish", true);
                    }
                });
                AppLog.log("Activity FirstDrawLayoutRootTwo Child onFirstDrawFinish");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLog.log("Activity onResume");
        AppStartUpTimeLog.logTimeDiff("Activity onResume start");

        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        AppLog.log("Activity onWindowFocusChanged " + hasFocus);
        if (hasFocus && mIsFirstFocus) {
            mIsFirstFocus = false;
            AppStartUpTimeLog.logTimeDiff("onWindowFocusChanged true start");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    BlockingUtil.simulateBlocking(100);  // 模拟阻塞100毫秒
                    AppStartUpTimeLog.logTimeDiff("onWindowFocusChanged true end");
                }
            });
        }
    }

    private void onLazyInit() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTimeLogListView.setAdapter(new TimeLogAdapter(getBaseContext(), AppStartUpTimeLog.mTimeNoteDataArrayList));
            }
        }, 300);

    }
}
