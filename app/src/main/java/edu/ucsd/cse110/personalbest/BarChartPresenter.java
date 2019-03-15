package edu.ucsd.cse110.personalbest;

import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

public class BarChartPresenter implements IUserObserver {
    BarChart chart1;
    BarChart chart2;
    LineChart chart3;
    User user;
    public BarChartPresenter(User user, BarChart chart1, BarChart chart2, LineChart chart3){
        this.chart1=chart1;
        this.chart2=chart2;
        this.chart3=chart3;
        this.user=user;
        user.register(this);
    }
    @Override
    public void onDataChange() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ArrayList<Integer> a=user.getExerciseHistory();
            entries.add(new BarEntry(-i, i >= user.getExerciseHistory().size() ? 0 : a.get(i)));
        }
        BarDataSet dataSet = new BarDataSet(entries, "BarDataSet");
        BarData barData = new BarData(dataSet);
        chart1.setData(barData);
        chart1.invalidate();

        List<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            entries2.add(new BarEntry(-i, i >= user.getWalkHistory().size() ? 0 : user.getWalkHistory().get(i)));
        }
        Log.d("emfmmls",user.getWalkHistory().toString());
        BarDataSet dataSet2 = new BarDataSet(entries, "BarDataSet");
        BarData barData2 = new BarData(dataSet2);
        chart2.setData(barData2);
        chart2.invalidate();
    }
}
