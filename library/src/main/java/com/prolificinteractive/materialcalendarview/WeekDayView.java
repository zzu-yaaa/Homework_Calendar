package com.prolificinteractive.materialcalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter;

import org.threeten.bp.DayOfWeek;

/**
 * Display a day of the week
 */
@SuppressLint("ViewConstructor") class WeekDayView extends AppCompatTextView {

  private WeekDayFormatter formatter = WeekDayFormatter.DEFAULT;
  private DayOfWeek dayOfWeek;

  @SuppressLint("ResourceAsColor")
  public WeekDayView(final Context context, final DayOfWeek dayOfWeek) {
    super(context);

    setGravity(Gravity.TOP);

    //setBackgroundColor(Color.MAGENTA);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    setDayOfWeek(dayOfWeek);
  }

  public void setWeekDayFormatter(@Nullable final WeekDayFormatter formatter) {
    this.formatter = formatter == null ? WeekDayFormatter.DEFAULT : formatter;
    setDayOfWeek(dayOfWeek);
  }

  public void setDayOfWeek(final DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
    setText(formatter.format(dayOfWeek));
  }

  //월화수목금토일 부분 높이 조정
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    // 원하는 가로 크기와 세로 크기를 설정합니다.
    int desiredWidth = widthSize - getPaddingLeft() - getPaddingRight();

    //int desiredWidth = 50;
    int desiredHeight = 100;

    // 최종적으로 설정할 타일의 가로 크기와 세로 크기를 계산합니다.
    int measuredWidth = Math.min(desiredWidth, widthSize);
    int measuredHeight = Math.min(desiredHeight, heightSize);

    setMeasuredDimension(measuredWidth, measuredHeight);
  }
}
