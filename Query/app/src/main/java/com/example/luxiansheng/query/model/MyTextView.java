package com.example.luxiansheng.query.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.luxiansheng.query.R;

/**
 * 创建自己的TextView
 */
public class MyTextView extends TextView{
    Paint paint=new Paint();
    PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    int width = wm.getDefaultDisplay().getWidth();
    int height = wm.getDefaultDisplay().getHeight()/9;
    int height1=height-30;
    public MyTextView(Context context, AttributeSet attributeSet,int defstyle){
        super(context,attributeSet,defstyle);
    }
    public MyTextView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        paint.setColor(getResources().getColor(R.color.darkgray));
        paint.setAntiAlias(true);
    }
    public MyTextView(Context context) {
        super(context);
        paint.setColor(getResources().getColor(R.color.darkgray));
        paint.setAntiAlias(true);
    }
    public void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(width,0);
        path.lineTo(width,height1);
        path.lineTo(width/8*3,height1);
        path.lineTo(width/8*2-40,height);
        path.lineTo(width/8*2,height1);
        path.lineTo(0,height1);
        path.close();
        canvas.setDrawFilter(pfd);
        canvas.drawPath(path,paint);
        super.onDraw(canvas);
        canvas.save();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max,height);
    }
    public void setBackgroundColor(int color) {
        paint.setColor(color);
    }
    public void setNotifiText(int text){
        setText(text+"");
    }
}
