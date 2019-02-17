package edu.ucsd.cse110.personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.personalbest.fitness.GoogleFitAdapter;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final long MS_IN_DAY=86400000;
    private String fitnessServiceKey = "GOOGLE_FIT";
    private static final String TAG = "StepCountActivity";
    private FitnessService fitnessService;
    private static int state=0; //0 for standby, 1 for active
    private TextView etic;
    private TextView espc;
    private TextView estc;
    private TextView exercising;
    private TextView etil;
    private TextView espl;
    private TextView estl;
    private TextView goal_content;
    private TextView complete_content;
    private TextView remaining_content;
    private Button tmp_update_button;
    private long goal = 5000;

    private SharedPreferences stepData;
    private LocalTime savePrevStepTime;
    public int[] weekSteps = new int[7];
    public int[] weekWalks = new int[7];
    IntentionalStep walk;

    private class walkUpdateTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            int step=1000;
            final long currentTime=Calendar.getInstance().getTimeInMillis()/1000;
            walk=new IntentionalStep(currentTime);
            final IncidentalStep starting=new IncidentalStep(Integer.parseInt(complete_content.getText().toString()));
            while(state==1){
                try{
                    Thread.sleep(step);
                    String[] publishable=new String[3];
                    fitnessService.updateStepCount();
                    walk.setStep(Integer.parseInt(complete_content.getText().toString())-starting.getStep());
                    walk.setTime(Calendar.getInstance().getTimeInMillis()/1000);
                    publishable[0]=""+walk.getStep();
                    publishable[1]=""+walk.getTimeElapsed();
                    publishable[2]=""+walk.getSpeed();
                    publishProgress(publishable);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            return "Congratulations! You walked "+walk.getStep()+" steps during this workout.";
        }

        protected void onProgressUpdate(String... text){
            estc.setText(text[0]+" steps");
            etic.setText(text[1]+" seconds");
            espc.setText(text[2]+" km/h");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putInt(""+walk.getTimeStart(),100);
                    //walk.getStep());
            editor.commit();
            System.out.println(""+walk.getTimeStart()+" "+walk.getStep());
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        tmp_update_button = findViewById(R.id.tmp_update_button);
        tmp_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
                setBarChart();
            }
        });

        goal_content = findViewById(R.id.goal_content);
        complete_content = findViewById(R.id.complete_content);
        remaining_content = findViewById(R.id.remaining_content);
        this.setGoalCount(this.goal);


        if(state==0) {
            etic = findViewById(R.id.exercise_time_content);
            espc = findViewById(R.id.exercise_speed_content);
            estc = findViewById(R.id.exercise_step_content);
            exercising = findViewById(R.id.exercise_label);
            etil = findViewById(R.id.exercise_time_label);
            espl = findViewById(R.id.exercise_speed_label);
            estl = findViewById(R.id.exercise_step_label);
            etic.setVisibility(View.INVISIBLE);
            espc.setVisibility(View.INVISIBLE);
            estc.setVisibility(View.INVISIBLE);
            exercising.setVisibility(View.INVISIBLE);
            espl.setVisibility(View.INVISIBLE);
            etil.setVisibility(View.INVISIBLE);
            estl.setVisibility(View.INVISIBLE);
        }
        Button seb=findViewById(R.id.start_button);
        seb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState(v);

            }
        });
    }

    private void switchState(View v){
        Button seb=findViewById(R.id.start_button);
        if(state==0){
            state=1;
            seb.setText("End");
            etic.setVisibility(View.VISIBLE);
            espc.setVisibility(View.VISIBLE);
            estc.setVisibility(View.VISIBLE);
            exercising.setVisibility(View.VISIBLE);
            espl.setVisibility(View.VISIBLE);
            etil.setVisibility(View.VISIBLE);
            estl.setVisibility(View.VISIBLE);
            walkUpdateTask runner=new walkUpdateTask();
            runner.execute();
        }
        else{
            state=0;
            etic.setVisibility(View.INVISIBLE);
            espc.setVisibility(View.INVISIBLE);
            estc.setVisibility(View.INVISIBLE);
            exercising.setVisibility(View.INVISIBLE);
            espl.setVisibility(View.INVISIBLE);
            etil.setVisibility(View.INVISIBLE);
            estl.setVisibility(View.INVISIBLE);
            seb.setText("Start");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//       If authentication was required during google fit setup, this will be called after the user authenticates
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount();
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }
    public void setStepCount(long stepCount) {
        complete_content.setText(String.valueOf(stepCount));
        long remaining = this.goal - stepCount;
        remaining_content.setText(String.valueOf(remaining));
    }

    public void showBarChart(View view){
        Intent intent=new Intent(this,BarActivity.class);
        intent.putExtra("weekWalks",weekWalks);
        intent.putExtra("weekSteps",weekSteps);
        startActivity(intent);
    }
    public void setBarChart(){
        fitnessService.getStepHistory();
        android.icu.util.Calendar cal = android.icu.util.Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.set(android.icu.util.Calendar.HOUR_OF_DAY,23);
        cal.set(android.icu.util.Calendar.MINUTE,59);
        cal.set(android.icu.util.Calendar.SECOND,59);
        cal.set(android.icu.util.Calendar.MILLISECOND,999);
        long endTime = cal.getTimeInMillis();
        cal.add(android.icu.util.Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();
        SharedPreferences sharedPreferences=getSharedPreferences("user_name",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Map<String,?> map=sharedPreferences.getAll();
        Iterator it=map.entrySet().iterator();
        weekWalks=new int[7];
        while(it.hasNext()){
            Map.Entry pair=(Map.Entry)it.next();
            System.out.println(pair);
            long locator=Long.parseLong(pair.getKey().toString())*1000-startTime;
            System.out.println(locator);
            if(locator<0){
                editor.remove(pair.getKey().toString());
            }
            else{
                int day=(int)(locator/MS_IN_DAY);
                weekWalks[day]+=Integer.parseInt(pair.getValue().toString());
            }
        }
    }

    public void setGoalCount(long goal) {
        goal_content.setText(String.valueOf(goal));
    }
}
