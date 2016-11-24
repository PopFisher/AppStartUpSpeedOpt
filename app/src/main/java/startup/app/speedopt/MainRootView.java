package startup.app.speedopt;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by popfisher on 2016/11/24.
 */

public class MainRootView extends FrameLayout {

    private boolean isFirstDrawFinish = false;

    private IFirstDrawListener mIFirstDrawListener;

    public interface IFirstDrawListener {
        void onFirstDrawFinish();
    }

    public MainRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isFirstDrawFinish) {
            isFirstDrawFinish = true;
            if (mIFirstDrawListener != null) {
                mIFirstDrawListener.onFirstDrawFinish();
            }
        }
    }

    public void setFirstDrawListener(IFirstDrawListener firstDrawListener) {
        mIFirstDrawListener = firstDrawListener;
    }
}

