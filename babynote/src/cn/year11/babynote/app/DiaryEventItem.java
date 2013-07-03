package cn.year11.babynote.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.year11.babynote.R;
import cn.year11.babynote.dialog.DialogFactory;
import cn.year11.babynote.provider.Attachment;
import cn.year11.babynote.provider.AttachmentDao;
import cn.year11.babynote.provider.EventDao;
import cn.year11.babynote.provider.EventManager;
import cn.year11.babynote.provider.event.Event;
import cn.year11.babynote.utils.MediaUtils;
import cn.year11.babynote.widget.VoicePlayer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiaryEventItem {
	LayoutInflater mInflater;
	private boolean mShowDivider;
	private Event mEvent;
	private Context mContext;
	private View mView;
	static private VoicePlayer mVoicePlayer = VoicePlayer.getInstance();
	
	public Context getContext() {
		return mContext;
	}

	public View getView() {
		return mView;
	}

	public Event getEvent() {
		return mEvent;
	}

	public DiaryEventItem(Context context, Event e, boolean showDivider) {
		mContext = context;
		mInflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mShowDivider = showDivider;
		mEvent = e;

		setupView();
	}

	public void setupView() {
		mView = (View) mInflater.inflate(R.layout.diarylist_event_item, null);
		ViewComponent c = new ViewComponent();
		c.timeLabel = (TextView) mView.findViewById(R.id.time_label);
		c.noteSnippet = (TextView) mView.findViewById(R.id.note_snippet);
		c.divider = (ImageView) mView.findViewById(R.id.note_divider);
		c.syncFlag = (ImageView)mView.findViewById(R.id.sync_flag);
		if (mShowDivider) {
			c.divider.setVisibility(View.VISIBLE);
		}
		c.att_container = (LinearLayout) mView.findViewById(R.id.att_container);

		bindData(c, mEvent);
		mView.setTag(c);
		mView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ViewComponent c = (ViewComponent) v.getTag();
				Event e = c.event;
				DialogFactory.showEventDialog(getContext(), e);
			}

		});

		mView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				showPopupMenu(v);

				return true;
			}
		});
	}

	// show Pop Up menu
	private void showPopupMenu(final View view) {
		new AlertDialog.Builder(getContext()).setItems(
				new CharSequence[] { "删除" },
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ViewComponent c = (ViewComponent) view.getTag();
						Event e = c.event;
						EventManager.delete(e.getId());
					}

				}).show();
	}

	private class ViewComponent {
		TextView timeLabel;
		TextView noteSnippet;
		ImageView divider;
		LinearLayout att_container;
		Event event;
		ImageView syncFlag;
	}

	public void bindData(ViewComponent c, Event e) {
		c.event = e;

		if (c.event.getBeginTime() != 0) {
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

			c.timeLabel.setText(timeFormat.format(new Date(c.event
					.getBeginTime())));
		}

		if (c.event.getSyncStatus() == 1) {
			c.syncFlag.setVisibility(View.VISIBLE);
		}
		
		Attachment[] atts = AttachmentDao.getInstance()
				.findByEventId(e.getId());
		if (atts != null) {
			for (Attachment att : atts) {
				ImageView iv = new ImageView(getContext());
				LayoutParams params = new LayoutParams(100, 100);
				iv.setPadding(0, 0, 10, 0);
				iv.setLayoutParams(params);
				iv.setScaleType(ImageView.ScaleType.FIT_XY);
				iv.setImageDrawable(att.getThumbnailDrawable());

				final Attachment finalAtt = att;
				View.OnClickListener onClickListener = new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (finalAtt.getContentType().equals(
								Attachment.CONTENT_TYPE_IMAGE)) {
							MediaUtils.showPicture(getContext(),
									finalAtt.getContentPath());
						} else if (finalAtt.getContentType().equals(
								Attachment.CONTENT_TYPE_VOICE)) {

							if (!mVoicePlayer.isPlaying()) {
								if (mVoicePlayer.prepare(finalAtt.getContentPath(), null))
									mVoicePlayer.start();
							} else {
								// if (mVoicePlayer.getListener() == null) {
								mVoicePlayer.stop();
								// }
							}
						}
					}
				};
				iv.setOnClickListener(onClickListener);
				c.att_container.addView(iv);
			}
		}

		// c.noteSnippet.setMovementMethod(LinkMovementMethod.getInstance());
		// c.noteSnippet.setText(new ClickableStringBuilder(EventList.this,
		// c.event.mEventDetail));
		if (!TextUtils.isEmpty(c.event.getEventDetail())) {
			createSpannable(c.noteSnippet, c.event.getEventDetail());
		} else {
			c.noteSnippet.setVisibility(View.GONE);
		}
	}

	public void createSpannable(TextView tv, String htmlLinkText) {

		if (htmlLinkText == null) {
			return;
		}

		Spanned sp = Html.fromHtml(htmlLinkText);
		URLSpan[] urls = sp.getSpans(0, htmlLinkText.length(), URLSpan.class);

		tv.setMovementMethod(LinkMovementMethod.getInstance());

		SpannableStringBuilder builder = new SpannableStringBuilder(sp);
		builder.clearSpans();// should clear old spans

		// 循环把链接发过去
		for (URLSpan url : urls) {
			MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
			builder.setSpan(myURLSpan, sp.getSpanStart(url),
					sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(builder);

	}

	private class MyURLSpan extends ClickableSpan {

		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			Toast.makeText(getContext(), mUrl, Toast.LENGTH_LONG).show();
			widget.setBackgroundColor(Color.parseColor("#008800FF"));

			Uri uri = Uri.parse(mUrl); // url为你要链接的地址
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			getContext().startActivity(intent);
		}
	}

}
