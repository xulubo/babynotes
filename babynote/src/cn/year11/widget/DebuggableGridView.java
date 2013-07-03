package cn.year11.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class DebuggableGridView extends GridView {

	public DebuggableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void layoutChildren() {
		// TODO Auto-generated method stub
		
		super.layoutChildren();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
