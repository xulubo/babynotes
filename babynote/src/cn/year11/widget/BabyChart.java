package cn.year11.widget;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public abstract class BabyChart extends AbstractChart {
	static final double[] mMonths = { 0, 1, 2, 3, 4, 5, 6, 8, 10, 12, 15, 18, 21,
			24, 30, 36, 42, 48, 54, 60, 66, 72, 84, 96, 108, 120 };
	
	List<ChartSeries> mSeries = new ArrayList<ChartSeries>();
    XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    
    ChartSeries mBabySeries = new ChartSeries("±¦±´Êý¾Ý", Color.RED, PointStyle.DIAMOND);
    
    View mView = null;
    
	public BabyChart(String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax)
	{
		setChartSettings(mRenderer, title, xTitle, yTitle, xMin, xMax, yMin, yMax,
				Color.LTGRAY, Color.LTGRAY);
	}
	
	public void addSeries(ChartSeries s)
	{
		mSeries.add(s);
	}
	
	public View getView(Context context) {
		if (mView != null) {
			return mView;
		}
		
		mSeries.add(mBabySeries);
		for(ChartSeries s: mSeries)
		{
		      XYSeries series = new XYSeries(s.title, 0);
		      for(int i=0; i<s.mXs.size(); i++) {
		    	  series.add(s.mXs.get(i), s.mYs.get(i));
		      }
		      mDataset.addSeries(series);
		      
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(s.color);
		      r.setPointStyle(s.style);
		      r.setFillPoints(true);
		      mRenderer.addSeriesRenderer(r);
		}
		mView = ChartFactory.getLineChartView(context,
				mDataset, mRenderer);
		return mView;
	}
	
	public void add(double x, double y)
	{
		mBabySeries.add(x, y);
	}
	
	public static class ChartSeries
	{
		ArrayList<Double> mXs = new ArrayList<Double>();
		ArrayList<Double> mYs = new ArrayList<Double>();
		int color = Color.RED;
		PointStyle style = PointStyle.TRIANGLE;
		String title;

		public ChartSeries(String title, int color, PointStyle style)
		{
			this.color = color;
			this.style = style;
			this.title = title;
		}
		
		public void add(double x, double y)
		{
			this.mXs.add(x);
			this.mYs.add(y);
		}
	}
	
}
