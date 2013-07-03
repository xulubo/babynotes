package cn.year11.babynote.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseDialog extends AlertDialog {

	protected View mView;
	protected OnConfirmListener mConfirmListener;

	public BaseDialog(Context context, int layoutId)
	{
		super(context);
		initialize (context, layoutId);
	}
	
	public void initialize(Context context, int layoutId) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		mView = inflater.inflate(layoutId, null);

		setButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onSave();
				if (mConfirmListener != null) {
					mConfirmListener.onConfirm();
				}
			}

		});
		
		setButton(BUTTON_NEGATIVE , "取消", (DialogInterface.OnClickListener)null);
		super.setView(mView);
		setupView();
	}

	public View findViewById(int id)
	{
		return mView.findViewById(id);
	}
	
	public View findV(int id)
	{
		return super.findViewById(id);
	}
	
	@Override
	public void show() {
		bindData();
		super.show();
	}
	
	
	public void setOnConfirmListener(OnConfirmListener l) {
		mConfirmListener = l;
	}
	
	public static abstract class OnConfirmListener
	{
		public abstract void onConfirm();
	}
	
	protected abstract void setupView();
	protected abstract void onSave();	
	protected abstract void bindData();
}
