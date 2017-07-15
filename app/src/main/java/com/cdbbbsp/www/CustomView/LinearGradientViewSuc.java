package com.cdbbbsp.www.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.cdbbbsp.www.Utils.MyUtils;

/**
 * 创建人 xiaojun
 * 创建时间 2017/7/14-10:57
 */

public class LinearGradientViewSuc extends View {
    private LinearGradient linearGradient = null;
    private Paint paint = null;
    private Context context;



    public LinearGradientViewSuc(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init(){
        linearGradient = new LinearGradient(MyUtils.getInstance().getScreenWidthOrHeight(context,true)
                , 0, 0, MyUtils.getInstance().getScreenWidthOrHeight(context,false), new int[] {
                Color.parseColor("#ffff92"), Color.parseColor(
                "#009900")}, null,
                Shader.TileMode.CLAMP);
        paint = new Paint();
    }

    private int width,height;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(false);
        paint.setStrokeWidth(height);
        //设置渲染器
        paint.setShader(linearGradient);
        //绘制圆环
        canvas.drawLine(0,height/2.0f,width,height/2.0f,paint);
    }
}
