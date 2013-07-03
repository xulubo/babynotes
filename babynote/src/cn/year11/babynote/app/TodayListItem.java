package cn.year11.babynote.app;

import android.view.View;
import android.view.ViewGroup;

public abstract interface TodayListItem
{
  public abstract boolean blockViewRecycling();

  public abstract int getLayoutId();

  public abstract View getView(TodayActivity paramTodayActivity, View paramView, ViewGroup paramViewGroup);

  public abstract boolean isEnabled();
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.app.TodayListItem
 * JD-Core Version:    0.6.0
 */