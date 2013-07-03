package cn.year11.babynote.dialog;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import cn.year11.babynote.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public class DigitSelectDialog extends AlertDialog {
	
	protected DigitSelectDialog(Context context, int[] currentItems, OnConfirmListener l) {
		super(context);
		setupView(currentItems, l);
	}

	private void setupView(int[] currentItems, final OnConfirmListener l)
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.number_ctl_layout, null);
		
		final WheelView v10 = (WheelView) layout.findViewById(R.id.digit_x10);
		v10.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 9));
	
		final WheelView v1 = (WheelView) layout.findViewById(R.id.digit_x1);
		v1.setViewAdapter(new NumericWheelAdapter(getContext(), 0, 9, "%d"));
		v1.setCyclic(true);
		
		v10.setCurrentItem(currentItems[1]);
		v1.setCurrentItem(currentItems[0]);
	
		
		setTitle("服药剂量");
		setView(layout);
		setButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (l!=null) {
					l.onConfirm(v10.getCurrentItem()*10 + v1.getCurrentItem());			
				}
			}
			
		});
		setButton(BUTTON_NEGATIVE, "取消", (DialogInterface.OnClickListener)null);
	}
	
	public static abstract class OnConfirmListener
	{
		public abstract void onConfirm(int n);
	}
}
