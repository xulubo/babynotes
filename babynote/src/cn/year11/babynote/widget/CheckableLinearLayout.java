package cn.year11.babynote.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

	private boolean mIsEnabled = false;
	private static final int[] CHECKED_STATE_SET = {
		android.R.attr.state_checked
	};
	
	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isChecked() {
		return mIsEnabled;
	}

	@Override
	public void setChecked(boolean checked) {
		mIsEnabled = checked;
		refreshDrawableState();
	}

	@Override
	public void toggle() {
		setChecked(!mIsEnabled);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace+1);
		if (isChecked()) {
			mergeDrawableStates(drawableState, CHECKED_STATE_SET);
		}
		
		return drawableState;
	}

	@Override
	public boolean performClick() {
		toggle();
		return super.performClick();
	}

	
}
