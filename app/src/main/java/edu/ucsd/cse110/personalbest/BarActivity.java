package edu.ucsd.cse110.personalbest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        int[] weekWalks=getIntent().getIntArrayExtra("weekWalks");
        int[] weekSteps=getIntent().getIntArrayExtra("weekSteps");
        int[] weekGoals=getIntent().getIntArrayExtra("weekGoals");
        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        Date now = cal.getTime();
        Date[] timestamp=new Date[7];
        timestamp[6]=now;
        for (int i=1;i<7;i++){
            cal.add(Calendar.DATE,-1);
            timestamp[6-i]=cal.getTime();
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(timestamp[0], weekSteps[0]),
                new DataPoint(timestamp[1], weekSteps[1]),
                new DataPoint(timestamp[2], weekSteps[2]),
                new DataPoint(timestamp[3], weekSteps[3]),
                new DataPoint(timestamp[4], weekSteps[4]),
                new DataPoint(timestamp[5], weekSteps[5]),
                new DataPoint(timestamp[6], weekSteps[6]),
        });
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(timestamp[0], weekWalks[0]),
                new DataPoint(timestamp[1], weekWalks[1]),
                new DataPoint(timestamp[2], weekWalks[2]),
                new DataPoint(timestamp[3], weekWalks[3]),
                new DataPoint(timestamp[4], weekWalks[4]),
                new DataPoint(timestamp[5], weekWalks[5]),
                new DataPoint(timestamp[6], weekWalks[6]),
        });
        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(timestamp[0], weekGoals[0]),
                new DataPoint(timestamp[1], weekGoals[1]),
                new DataPoint(timestamp[2], weekGoals[2]),
                new DataPoint(timestamp[3], weekGoals[3]),
                new DataPoint(timestamp[4], weekGoals[4]),
                new DataPoint(timestamp[5], weekGoals[5]),
                new DataPoint(timestamp[6], weekGoals[6]),
        });
        series2.setColor(Color.RED);
        series3.setColor(Color.BLACK);
        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        series.setSpacing(10);
        series2.setSpacing(10);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this,new SimpleDateFormat("MM/dd")));
        graph.getGridLabelRenderer().setNumHorizontalLabels(2);
        graph.getViewport().setMinX(timestamp[0].getTime());
        android.icu.util.Calendar max=android.icu.util.Calendar.getInstance();
        max.add(Calendar.DATE,1);
        graph.getViewport().setMaxX(max.getTime().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false,true);
        series.setTitle("Total Steps");
        series2.setTitle("Exercise Steps");
        series3.setTitle("Goal");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public void goBack(View view){
        finish();
    }
}
