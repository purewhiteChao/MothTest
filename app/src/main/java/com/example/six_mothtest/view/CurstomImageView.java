package com.example.six_mothtest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by Android Studio.
 * User: Administrator
 * Date: 2019/4/30 0030
 * Time: 15:25
 * Describe: ${as}
 */
public class CurstomImageView extends AppCompatImageView {
    private Paint paint;
    private BitmapShader bitmapShader;
    private Bitmap bitmap;
    private Matrix matrix;

    public CurstomImageView(Context context) {
        this(context,null);
    }

    public CurstomImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CurstomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        notifyImageView();

    }

    public void notifyImageView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        Shader.TileMode tileMode = Shader.TileMode.MIRROR;
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        bitmap = drawable.getBitmap();
        bitmapShader = new BitmapShader(bitmap,tileMode,tileMode);
        paint.setShader(bitmapShader);
        matrix = new Matrix();
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float scaleWidth = (float) ((measuredWidth*1.0)/bitmapWidth);
        float scaleHeight = (float) ((measuredHeight*1.0)/bitmapHeight);
        float scale = Math.max(scaleWidth,scaleHeight);

        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        int r = Math.min(getMeasuredHeight(), getMeasuredWidth());
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,r/2,paint);
    }
}
