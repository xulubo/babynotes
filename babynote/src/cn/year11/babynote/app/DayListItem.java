package cn.year11.babynote.app;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.year11.babynote.R;

import com.babycenter.pregnancytracker.common.Artifact;
import com.babycenter.pregnancytracker.common.Day;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class DayListItem implements TodayListItem, View.OnFocusChangeListener {
	private static final int MAX_ARTIFACTS_PER_DAY = 3;
	private final Day day;
	private boolean isCurrentDay = false;
	private boolean isInCurrentWeek = false;
	private boolean isWeekLast = false;

	public DayListItem(Day paramDay) {
		this.day = paramDay;
	}

	private void applyFonts(Activity paramActivity,
			ArtifactViewHolder paramArtifactViewHolder) {
		Typeface localTypeface = Typeface.createFromAsset(
				paramActivity.getAssets(), "fonts/Georgia.ttf");
		paramArtifactViewHolder.artifactTeaser.setTypeface(localTypeface);
		paramArtifactViewHolder.artifactTitleLabel.setTypeface(localTypeface);
	}

	private String getImageAssetPathFromUrl(String paramString)
			throws MalformedURLException {
		String[] arrayOfString = new URL(paramString).getPath().split("/");
		return "images/thumbnails/"
				+ arrayOfString[(arrayOfString.length - 1)].replaceAll(
						".je*pg", ".png");
	}

	private Bitmap getThumbnailFromUrl(String paramString,
			AssetManager paramAssetManager) throws IOException {
		return BitmapFactory.decodeStream(paramAssetManager
				.open(getImageAssetPathFromUrl(paramString)));
	}

	private void resetArtifactContainer(LinearLayout paramLinearLayout) {
		paramLinearLayout.setOnClickListener(null);
		ArtifactViewHolder localArtifactViewHolder = (ArtifactViewHolder) paramLinearLayout
				.getTag();
		localArtifactViewHolder.rightOuterContainer.setPadding(0, 8, 0, 8);
		localArtifactViewHolder.isFirst = false;
		localArtifactViewHolder.artifactDivider.setVisibility(8);
		localArtifactViewHolder.artifactTitleLabel.setMinimumHeight(0);
		localArtifactViewHolder.daysTogoLabel.setVisibility(8);
		localArtifactViewHolder.artifactCategoryLabel.setText("");
		localArtifactViewHolder.artifactCategoryLabel.setVisibility(8);
		localArtifactViewHolder.artifactTeaser.setText("");
		localArtifactViewHolder.artifactTeaser.setVisibility(8);
		localArtifactViewHolder.artifactTitleLabel.setText("");
		localArtifactViewHolder.artifactTitleLabel.setVisibility(8);
		localArtifactViewHolder.artifactThumbnail.setVisibility(8);
		localArtifactViewHolder.dayLabel.setText("");
		localArtifactViewHolder.dayLabel.setVisibility(8);
		localArtifactViewHolder.activityCheckbox.setVisibility(8);
	}

	public boolean blockViewRecycling() {
		return false;
	}

	public int getLayoutId() {
		return R.layout.todaylist_day;
	}

	public View getView(TodayActivity paramTodayActivity, View paramView,
			ViewGroup paramViewGroup) {
		LinearLayout dayViewLayout;
		int i;

		View.OnClickListener onClickListener;
		int j;

		LinearLayout dayListItemLayout;
		ArtifactViewHolder localArtifactViewHolder;
		if (paramView == null) {
			i = 0;
			dayViewLayout = (LinearLayout) paramTodayActivity
					.getLayoutInflater().inflate(getLayoutId(), paramViewGroup,
							false);
		} else {
			i = 1;
			dayViewLayout = (LinearLayout) paramView;
		}

		if (!isCurrentDay()) {
			dayViewLayout
					.setBackgroundResource(R.drawable.shape_bg_current_day);
		} else if (isInCurrentWeek()) {
			dayViewLayout.setBackgroundResource(R.color.background_blue);
		} else {
			dayViewLayout.setBackgroundColor(0);
		}

		final TodayActivity todayActivity = paramTodayActivity;
		onClickListener = new View.OnClickListener() {
			public void onClick(View paramView) {
				DayListItem.ArtifactViewHolder localArtifactViewHolder = (DayListItem.ArtifactViewHolder) paramView
						.getTag();
				if (5 == localArtifactViewHolder.artifactType)
					DayListItem.this.playVideoId(todayActivity,
							localArtifactViewHolder.artifactId);

				if (4 == localArtifactViewHolder.artifactType) {
					DayListItem.this.showImageArtifact(todayActivity,
							localArtifactViewHolder.artifactId);
				}

				DayListItem.this.showArtifactId(todayActivity,
						localArtifactViewHolder.artifactId);

			}
		};

		int totalArtifacts = this.day.artifacts.size();
		for (j = 0; j < totalArtifacts; j++) {
			if (j >= 3)
				break;

			dayListItemLayout = (LinearLayout) dayViewLayout.getChildAt(j);

			if (i == 0) {
				// break label174;
				localArtifactViewHolder = new ArtifactViewHolder();
				CheckBox localCheckBox1 = (CheckBox) dayListItemLayout
						.findViewById(R.id.activity_checkbox);
				localArtifactViewHolder.activityCheckbox = localCheckBox1;

				TextView localTextView1 = (TextView) dayListItemLayout
						.findViewById(R.id.day_label);
				localArtifactViewHolder.dayLabel = localTextView1;

				TextView localTextView2 = (TextView) dayListItemLayout
						.findViewById(R.id.artifact_category_label);
				localArtifactViewHolder.artifactCategoryLabel = localTextView2;

				TextView localTextView3 = (TextView) dayListItemLayout
						.findViewById(R.id.artifact_title_label);
				localArtifactViewHolder.artifactTitleLabel = localTextView3;

				TextView localTextView4 = (TextView) dayListItemLayout
						.findViewById(R.id.artifact_teaser);
				localArtifactViewHolder.artifactTeaser = localTextView4;

				ImageView localImageView1 = (ImageView) dayListItemLayout
						.findViewById(R.id.artifact_thumbnail);
				localArtifactViewHolder.artifactThumbnail = localImageView1;

				ImageView localImageView2 = (ImageView) dayListItemLayout
						.findViewById(R.id.artifact_divider);
				localArtifactViewHolder.artifactDivider = localImageView2;

				LinearLayout localLinearLayout3 = (LinearLayout) dayListItemLayout
						.findViewById(R.id.day_right_outer_container);
				localArtifactViewHolder.rightOuterContainer = localLinearLayout3;

				TextView localTextView5 = (TextView) dayListItemLayout
						.findViewById(R.id.days_togo_label);
				localArtifactViewHolder.daysTogoLabel = localTextView5;

				applyFonts(paramTodayActivity, localArtifactViewHolder);
				dayListItemLayout.setTag(localArtifactViewHolder);
				dayListItemLayout.setOnFocusChangeListener(this);
			} else {
				resetArtifactContainer(dayListItemLayout);
				localArtifactViewHolder = (ArtifactViewHolder) dayListItemLayout
						.getTag();
			}

			dayListItemLayout.setVisibility(8);

			Artifact localArtifact;

			// label406:
			ImageView thumbnailImageView = null;

			if (j > 0) {
				localArtifactViewHolder.rightOuterContainer.setPadding(0, 0, 0,
						8);
				localArtifactViewHolder.artifactDivider.setVisibility(0);
			}
			localArtifact = (Artifact) this.day.artifacts.get(j);
			dayListItemLayout.setVisibility(0);
			dayListItemLayout.setClickable(true);
			int m = localArtifact.artifactType;
			localArtifactViewHolder.artifactType = m;
			long l = localArtifact.id;
			localArtifactViewHolder.artifactId = l;
			dayListItemLayout.setOnClickListener(onClickListener);

			// PregnancyTrackerApplication localPregnancyTrackerApplication
			// =
			// (PregnancyTrackerApplication)paramTodayActivity.getApplication();
			// boolean bool =
			// localPregnancyTrackerApplication.getChecklistItemState(localArtifact.id);
			// localArtifactViewHolder.activityCheckbox.setOnCheckedChangeListener(null);
			// localArtifactViewHolder.activityCheckbox.setChecked(bool);
//			CheckBox localCheckBox2 = localArtifactViewHolder.activityCheckbox;
			/*
			 * 2 local2 = new CompoundButton.OnCheckedChangeListener(
			 * localPregnancyTrackerApplication, localArtifact) { public void
			 * onCheckedChanged(CompoundButton paramCompoundButton, boolean
			 * paramBoolean) {
			 * this.val$application.setChecklistItemState(this.val$a.id,
			 * paramBoolean); } };
			 * localCheckBox2.setOnCheckedChangeListener(local2);
			 */
			if (j == 0) {
				localArtifactViewHolder.dayLabel.setVisibility(0);
				localArtifactViewHolder.dayLabel
						.setText(this.day.formattedDate);
			}

			if ((Artifact.ACTIVITY_TYPE == localArtifact.artifactType)
					|| (Artifact.REMINDER_TYPE == localArtifact.artifactType))
				localArtifactViewHolder.activityCheckbox.setVisibility(0);

			if ((localArtifact.category != null)
					&& (localArtifact.category.length() > 0)
					&& (localArtifact.artifactType != Artifact.REMINDER_TYPE)
					&& (localArtifact.artifactType != Artifact.VIDEO_TYPE)
					&& (localArtifact.artifactType != Artifact.IMAGE_TYPE)) {
				TextView categoryLabelTextView = localArtifactViewHolder.artifactCategoryLabel;
				categoryLabelTextView.setText(localArtifact.category
						.toUpperCase(Locale.getDefault()));
				categoryLabelTextView.setVisibility(0);
			}

			if ((localArtifact.artifactType == Artifact.VIDEO_TYPE)
					|| (localArtifact.artifactType == Artifact.IMAGE_TYPE)) {
				thumbnailImageView = localArtifactViewHolder.artifactThumbnail;
			}

			try {
				if (thumbnailImageView != null) {
					thumbnailImageView.setImageBitmap(getThumbnailFromUrl(
							localArtifact.thumbnail,
							paramTodayActivity.getAssets()));
					thumbnailImageView.setVisibility(0);

					// label720:
					thumbnailImageView.setScaleType(ImageView.ScaleType.FIT_XY);
				}
				if ((localArtifact.title != null)
						&& (localArtifact.title.length() > 0)) {
					TextView titleLabel = localArtifactViewHolder.artifactTitleLabel;
					titleLabel.setText(localArtifact.title);
					titleLabel.setVisibility(0);
				}

				if ((localArtifact.teaser == null)
						|| (localArtifact.teaser.length() <= 0)
						|| (localArtifact.artifactType == Artifact.REMINDER_TYPE))
					continue;

				TextView teaserTextView = localArtifactViewHolder.artifactTeaser;
				teaserTextView.setText(localArtifact.teaser);
				teaserTextView.setVisibility(0);

				localArtifactViewHolder.isFirst = true;
				if (!isCurrentDay()) {
				} else {
					localArtifactViewHolder.daysTogoLabel.setVisibility(0);
					TextView daysTogoLabelTextView = localArtifactViewHolder.daysTogoLabel;
					String str = paramTodayActivity
							.getString(R.string.todayview_daystogo_n);
					Object[] arrayOfObject = new Object[1];
					arrayOfObject[0] = Integer.valueOf(this.day.daysToGo);
					daysTogoLabelTextView.setText(String.format(str,
							arrayOfObject));
				}

				// label897:
			} catch (Exception localException) {
			}
		}

		return dayViewLayout;
	}

	public boolean isCurrentDay() {
		return this.isCurrentDay;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isInCurrentWeek() {
		return this.isInCurrentWeek;
	}

	public boolean isWeekLast() {
		return this.isWeekLast;
	}

	public void onFocusChange(View paramView, boolean paramBoolean) {
		if ((paramBoolean) && (paramView.getVisibility() == 0))
			paramView.setBackgroundResource(2131165198);

		paramView.setBackgroundColor(0);
	}

	void playVideoId(Activity paramActivity, long paramLong) {
		/*
		 * if
		 * (NetworkUtil.isNetworkAvailable(paramActivity.getApplicationContext
		 * ())) { Intent localIntent = new Intent(paramActivity,
		 * BcPlayerActivity.class); localIntent.putExtra("artifactId",
		 * paramLong); paramActivity.startActivity(localIntent); } while (true)
		 * { return; new
		 * AlertDialog.Builder(paramActivity).setMessage(2131230771
		 * ).setNeutralButton("Ok", null).show(); }
		 */
	}

	public void setCurrentDay(boolean paramBoolean) {
		this.isCurrentDay = paramBoolean;
		setInCurrentWeek(true);
	}

	public void setInCurrentWeek(boolean paramBoolean) {
		this.isInCurrentWeek = paramBoolean;
	}

	public void setWeekLast(boolean paramBoolean) {
		this.isWeekLast = paramBoolean;
	}

	boolean showArtifactId(Activity paramActivity, long paramLong) {
		/*
		 * Intent localIntent = new Intent(paramActivity,
		 * ArtifactDetailActivity.class); localIntent.putExtra("artifactId",
		 * paramLong); paramActivity.startActivity(localIntent);
		 */
		return true;
	}

	boolean showImageArtifact(Activity paramActivity, long paramLong) {
		/*
		 * Intent localIntent = new Intent(paramActivity,
		 * ImageArtifactDetailActivity.class);
		 * localIntent.putExtra("artifactId", paramLong);
		 * paramActivity.startActivity(localIntent);
		 */
		return true;
	}

	public static class ArtifactViewHolder {
		public CheckBox activityCheckbox;
		public TextView artifactCategoryLabel;
		public ImageView artifactDivider;
		public long artifactId;
		public TextView artifactTeaser;
		public ImageView artifactThumbnail;
		public TextView artifactTitleLabel;
		public int artifactType;
		public LinearLayout dayContainer;
		public LinearLayout dayItemRightColumn;
		public TextView dayLabel;
		public LinearLayout dayRightInnerContainer;
		public TextView daysTogoLabel;
		public boolean isFirst;
		public int rightColumnWidth;
		public LinearLayout rightOuterContainer;
	}
}

/*
 * Location: C:\work\tools\android-decompile-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name: com.babycenter.pregnancytracker.app.DayListItem JD-Core
 * Version: 0.6.0
 */