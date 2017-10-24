package startup.app.speedopt;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import startup.app.speedopt.interfaces.IFirstDrawListener;

/**
 * Created by popfisher on 2016/11/24.
 */

public class FirstDrawListenView extends View {

    private boolean isFirstDrawFinish = false;
    private boolean isFirstMeasureFinish = false;
    private boolean isFirstLayoutFinish = false;

    private IFirstDrawListener mIFirstDrawListener;

    public FirstDrawListenView(Context context, AttributeSet attrs) {
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isFirstMeasureFinish) {
            isFirstMeasureFinish = true;
            if (mIFirstDrawListener != null) {
                mIFirstDrawListener.onFirstMeasureFinish();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isFirstLayoutFinish) {
            isFirstLayoutFinish = true;
            if (mIFirstDrawListener != null) {
                mIFirstDrawListener.onFirstLayoutFinish();
            }
        }
    }

}

