package cn.year11.widget;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class BabyWeightChart extends BabyChart {
	static final double[] mWeightLow = { 2.9, 3.6, 4.3, 5.0, 5.7, 6.3, 6.9,
			7.8, 8.6, 9.1, 9.8, 10.3, 10.8, 11.2, 12.1, 13.0, 13.9, 14.8, 15.7,
			16.6, 17.4, 18.4, 20.2, 22.2, 24.3, 26.8 };
	static final double[] mWeightHigh = { 3.8, 5.0, 6.0, 6.9, 7.6, 8.2, 8.8,
			9.8, 10.6, 11.3, 12.0, 12.7, 13.3, 14.0, 15.3, 16.4, 17.6, 18.7,
			19.9, 21.1, 22.3, 23.6, 26.5, 30.0, 34.0, 38.7 };

	public BabyWeightChart() {
		super("体重情况", "年龄(月)", "体重(千克)", 0, 100, 0, 100);
		ChartSeries low = new ChartSeries("标准低值", Color.GREEN, PointStyle.CIRCLE);
		ChartSeries high = new ChartSeries("标准高值", Color.BLUE, PointStyle.CIRCLE);
		
		for(int i=0; i<mMonths.length; i++) {
			low.add(mMonths[i], mWeightLow[i]);
			high.add(mMonths[i], mWeightHigh[i]);
		}
		
		addSeries(low);
		addSeries(high);
	}


}
