package cn.year11.babynote.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.year11.babynote.R;

import com.babycenter.pregnancytracker.common.Week;
import com.babycenter.pregnancytracker.common.WeekAndDay;

public class WeekListItem
  implements TodayListItem
{
  private boolean isCurrentWeek = false;
  private final Week week;

  public WeekListItem(Week paramWeek)
  {
    this.week = paramWeek;
  }

  public boolean blockViewRecycling()
  {
    if (isCurrentWeek())
    	return true;
    
    return false;
  }

  public int getLayoutId()
  {
    return R.layout.week;
  }

  public View getView(TodayActivity paramTodayActivity, View paramView, ViewGroup paramViewGroup)
  {
    LinearLayout localLinearLayout;
    ImageView localImageView;
    WeekAndDay localWeekAndDay;
    if ((paramView == null) || (isCurrentWeek()))
    {
      localLinearLayout = (LinearLayout)paramTodayActivity.getLayoutInflater().inflate(getLayoutId(), paramViewGroup, false);
      localImageView = (ImageView)localLinearLayout.findViewById(2131361883);
      
    }
    else {
        localLinearLayout = (LinearLayout)paramView;
    }

    return localLinearLayout;
  }

  public boolean isCurrentWeek()
  {
    return this.isCurrentWeek;
  }

  public boolean isEnabled()
  {
    return false;
  }

  public void setIsCurrentWeek(boolean paramBoolean)
  {
    this.isCurrentWeek = paramBoolean;
  }
}

/* Location:           C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     com.babycenter.pregnancytracker.app.WeekListItem
 * JD-Core Version:    0.6.0
 */