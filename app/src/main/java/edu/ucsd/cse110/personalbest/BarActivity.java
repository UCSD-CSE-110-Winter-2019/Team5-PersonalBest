package edu.ucsd.cse110.personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.type.Color;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.ucsd.cse110.personalbest.Managers.FireStoreManager;
import edu.ucsd.cse110.personalbest.Managers.SharedPrefManager;

public class BarActivity extends AppCompatActivity {
    CombinedChart walkchart;
    CombinedChart exercisechart;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String source = intent.getStringExtra("source");
        if (source.equals("default")) {
            user = new User();
            SharedPreferences sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
            SharedPrefManager sharedPrefManager = new SharedPrefManager(sharedPreferences, user);
            sharedPrefManager.retrieveData();
        } else {
            user = new User();
            user.setEmailAddress(source, false);
            FireStoreManager fireStoreManager = new FireStoreManager(user);
            fireStoreManager.retrieveData();
        }

        exercisechart = findViewById(R.id.exercisechart);
        walkchart = findViewById(R.id.walkchart);
        updateData();

        Switch mySwitch = findViewById(R.id.walk_switch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateData();
                if (isChecked) {
                    exercisechart.setVisibility(View.VISIBLE);
                    walkchart.setVisibility(View.GONE);
                }
                else{
                    exercisechart.setVisibility(View.GONE);
                    walkchart.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    // function to go back to mainActivity
    public void goBack(View view) {
        finish();
    }

    private void updateData(){
        //generateTestData();
        ArrayList<Integer> exerciseHistory= user.getExerciseHistory();
        ArrayList<Integer> walkHistory=user.getWalkHistory();
        ArrayList<Integer> goalHistory=user.getGoalHistory();
        Collections.reverse(exerciseHistory);
        Collections.reverse(walkHistory);
        Collections.reverse(goalHistory);
        List<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            entries2.add(new BarEntry(i, 29-i >= walkHistory.size() ? 0 : walkHistory.get(29-i)));
        }
        Log.d("",user.getWalkHistory().toString());
        BarDataSet dataSet2 = new BarDataSet(entries2, "Walk");
        BarData barData2 = new BarData(dataSet2);
        XAxis xAxis2=walkchart.getXAxis();
        YAxis yAxis2=walkchart.getAxisLeft();
        xAxis2.setAxisMaximum(29);
        xAxis2.setAxisMinimum(0);
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        yAxis2.setAxisMinimum(0);

        List<Entry> entries3 = new ArrayList<>();
        for(int i=0;i<30;i++){
            entries3.add(new Entry(i, 29-i >= goalHistory.size() ? 0 : goalHistory.get(29-i)));
        }
        LineDataSet lineDataSet=new LineDataSet(entries3,"Goal");
        lineDataSet.setCircleColor(R.color.purple);
        LineData lineData=new LineData(lineDataSet);
        CombinedData combinedData=new CombinedData();
        combinedData.setData(barData2);
        combinedData.setData(lineData);
        walkchart.setData(combinedData);
        walkchart.animateY(1000);
        walkchart.setDragEnabled(true);
        walkchart.getAxisRight().setEnabled(false);
        walkchart.setTouchEnabled(true);
        walkchart.setVisibleXRangeMaximum(15);
        walkchart.moveViewToX(29);
        walkchart.invalidate();

        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            entries.add(new BarEntry(30-exerciseHistory.size(),new float[]{29-i >= exerciseHistory.size() ? 0 : exerciseHistory.get(29-i),
                    29-i >= walkHistory.size() ? 0 : walkHistory.get(29-i)}));
        }
        BarDataSet dataSet = new BarDataSet(entries, "Exercise");
        dataSet.setColor(R.color.red);
        BarData barData = new BarData(dataSet,dataSet2);
        XAxis xAxis=exercisechart.getXAxis();
        YAxis yAxis=exercisechart.getAxisLeft();
        xAxis.setAxisMaximum(29);
        xAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        yAxis.setAxisMinimum(0);
        CombinedData combinedData2=new CombinedData();
        combinedData2.setData(barData);
        combinedData2.setData(lineData);
        exercisechart.setData(combinedData2);
        exercisechart.getAxisRight().setEnabled(false);
        exercisechart.animateY(1000);
        exercisechart.setTouchEnabled(true);
        exercisechart.setVisibleXRangeMaximum(10);
        exercisechart.moveViewToX(29);
        exercisechart.invalidate();

    }

    private void generateTestData(){
        user=new User();
        Random random=new Random();
        ArrayList<Integer> one=new ArrayList<>();
        for(int i=0;i<30;i++){
            one.add(random.nextInt(5000));
        }
        ArrayList<Integer> two=new ArrayList<>();
        for(int i=0;i<30;i++){
            two.add(random.nextInt(5000));
        }
        ArrayList<Integer> three=new ArrayList<>();
        for(int i=0;i<30;i++){
            three.add(random.nextInt(5000));
        }
        user.setWalkHistory(one,false);
        user.setExerciseHistory(two,false);
        user.setGoalHistory(three,false);
    }
}
