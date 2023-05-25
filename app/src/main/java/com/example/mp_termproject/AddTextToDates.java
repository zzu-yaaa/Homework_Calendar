package com.example.mp_termproject;

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
        dayPrice = text;
        this.barColor = barColor;
        this.order = order;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom, //위치조정 변수들
            CharSequence text, int start, int end, int lineNumber
    ) {
        Paint rectPaint = new Paint();
        rectPaint.setColor(barColor);

        if(order==1){
            Log.i("coordinate", "top : "+top+" bottom : "+bottom);
            canvas.drawRect(left, top+50, right, bottom+36, rectPaint);
            canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 30), paint);
        }
        else if(order==2){
            canvas.drawRect(left, top+80, right, bottom+66, rectPaint);
            canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 60), paint);
        }
        else if(order==3){
            canvas.drawRect(left, top+110, right, bottom+96, rectPaint);
            canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 90), paint);
        }
        else if(order==4){
            canvas.drawRect(left, top+140, right, bottom+126, rectPaint);
            canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 120), paint);
        }
        else if(order==5){
            canvas.drawRect(left, top+170, right, bottom+156, rectPaint);
            canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 150), paint);
        }


        //paint 설정 건드리면 날짜도 같이 바뀜
        //paint.setColor(Color.RED); // 텍스트의 색상 설정
        //paint.setTextSize(24); // 텍스트의 크기 설정
        //사각형 그리고 text 그림 (순서 지킬것)
        //canvas.drawRect(left, top+50, right, bottom+40, rectPaint);
        //canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 30), paint);

    }
}
