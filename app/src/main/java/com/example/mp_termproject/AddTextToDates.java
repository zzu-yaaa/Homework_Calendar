package com.example.mp_termproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class AddTextToDates implements LineBackgroundSpan {

    private String dayPrice;
    private boolean done;
    private int barColor;
    public AddTextToDates(String text,boolean done, int barColor) {
        dayPrice = text;
        this.done = done;
        this.barColor = barColor;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom, //위치조정 변수들
            CharSequence text, int start, int end, int lineNumber
    ) {
        Paint rectPaint = new Paint();
        if(done){
            rectPaint.setColor(barColor);
        }
        else{
            rectPaint.setColor(barColor);
        }
        //paint 설정 건드리면 날짜도 같이 바뀜
        //paint.setColor(Color.RED); // 텍스트의 색상 설정
        //paint.setTextSize(24); // 텍스트의 크기 설정
        //사각형 그리고 text 그림 (순서 지킬것)
        canvas.drawRect(left, top+50, right, bottom+40, rectPaint);
        canvas.drawText(dayPrice, ((left + right) / 4), (bottom + 30), paint);

    }
}
