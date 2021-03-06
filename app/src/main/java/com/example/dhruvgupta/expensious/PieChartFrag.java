package com.example.dhruvgupta.expensious;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;


public class PieChartFrag extends SimpleFragment {

    public static Fragment newInstance() {
        return new PieChartFrag();
    }

    private PieChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_pie, container, false);
        
        mChart = (PieChart) v.findViewById(R.id.frag_simple_pie_chart);
        mChart.setDescription("");
        mChart.setCenterText("Sample");
        mChart.setCenterTextSize(22f);
         
        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f); 
        mChart.setTransparentCircleRadius(50f);
        
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);
        
        mChart.setData(generatePieData());
        
        return v;
    }
}
