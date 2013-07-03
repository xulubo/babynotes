package cn.year11.babynote.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class IconToolbar extends LinearLayout{

	public IconToolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAdapter(ListAdapter adapter) {
		
	}
	
	public void addToolButton ()
	{
		
	}
	
	class ToolButton extends TextView {		
		public ToolButton(Context context, Drawable icon, String name)
		{
			super(context);
			this.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
			this.setText(name);
		}
	}
}
