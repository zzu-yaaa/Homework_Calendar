package com.example.mp_termproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.widget.Toast;

public class AddTextToDates implements LineBackgroundSpan {

    private String dayPrice;
    private int barColor;
    private int order;

    public AddTextToDates(String text, int barColor, int order) {
        if(text.length()>4){
            dayPrice = text.substring(0, 3) + "...";
        }
        else{
            dayPrice = text;
        }
        //dayPrice = text;
        this.barColor = barColor;
        this.order = order;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom, //position adjustment variables
            CharSequence text, int start, int end, int lineNumber
    ) {
        Paint rectPaint = new Paint();
        rectPaint.setColor(barColor);

        int startTop = top+45;
        int startBottom = bottom+36;
        int startBottom2 = bottom+30;
        int topUp = 30;

        for(int i=1;i<=10;i++){
            if(order==i){
                Log.i("coordinate", "top : "+top+" bottom : "+bottom);
                canvas.drawRoundRect(left, startTop, right, startBottom, 20, 20, rectPaint);
                canvas.drawText(dayPrice, ((left + right) / 8), startBottom2, paint);
            }
            startTop +=30;
            startBottom +=30;
            startBottom2 +=30;
        } 
    }
}
