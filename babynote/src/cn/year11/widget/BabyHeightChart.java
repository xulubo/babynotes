package cn.year11.widget;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import cn.year11.widget.BabyChart.ChartSeries;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class BabyHeightChart extends BabyChart {
	final double[] mHeightLow = { 48.2, 52.1, 55.5, 58.5, 61.0, 63.2, 65.1,
			68.3, 71.0, 73.4, 76.6, 79.4, 81.9, 84.3, 88.9, 91.1, 95.0, 98.7,
			102.1, 105.3, 108.4, 111.2, 116.6, 121.6, 126.5, 131.4 };
	final double[] mHeightHigh = { 52.8, 57.0, 60.7, 63.7, 66.4, 68.6, 70.5,
			73.6, 76.3, 78.8, 82.3, 85.4, 88.4, 91.0, 95.8, 98.7, 103.1, 107.2,
			111.0, 114.5, 117.8, 121.0, 126.8, 132.2, 137.8, 143.6 };
	
	public BabyHeightChart() {
		super("身高情况", "年龄(月)", "身高(厘米)", 0, 100, 2, 150);
		ChartSeries low = new ChartSeries("标准低值", Color.GREEN, PointStyle.CIRCLE);
		ChartSeries high = new ChartSeries("标准高值", Color.BLUE, PointStyle.CIRCLE);
		
		for(int i=0; i<mMonths.length; i++) {
			low.add(mMonths[i], mHeightLow[i]);
			high.add(mMonths[i], mHeightHigh[i]);
		}
		
		addSeries(low);
		addSeries(high);
	}

}
