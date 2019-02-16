package edu.ucsd.cse110.personalbest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.personalbest.fitness.GoogleFitAdapter;

import java.time.LocalTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    // Default service key is GOOGLE_FIT for the MainActivity
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
    private Button change_goal_button;
    // Default goal is 5000
    private Goal goal = new Goal(0);
    private int newGoalStep;

    private SharedPreferences stepData;
    private LocalTime savePrevStepTime;


    private class walkUpdateTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            int step=1000;
            final long currentTime=Calendar.getInstance().getTimeInMillis()/1000;
            IntentionalStep walk=new IntentionalStep(currentTime);
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
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepData = getSharedPreferences("StepData", MODE_PRIVATE);
        stepData.edit().putInt("first", 0)
                        .putInt("second", 0)
                        .putInt("third", 0)
                        .putInt("fourth",0)
                        .putInt("fifth", 0)
                        .putInt("sixth", 0)
                        .putInt("seventh", 0).commit();
        savePrevStepTime = LocalTime.parse("23:59:59");
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new GoogleFitAdapter(mainActivity);
            }
        });

        // When running test, change service key to TEST_SERVICE
        if (getIntent().getStringExtra(FITNESS_SERVICE_KEY) != null) {
            fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        }
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        // Initial setup for google fit service
        fitnessService.setup();

        // Click on the update button will update steps count
        tmp_update_button = findViewById(R.id.tmp_update_button);
        tmp_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fitnessService.updateStepCount();
            }
        });

        goal_content = findViewById(R.id.goal_content);
        complete_content = findViewById(R.id.complete_content);
        remaining_content = findViewById(R.id.remaining_content);
        this.setGoalCount(this.goal.getStep());

        Button change = findViewById(R.id.goal_update_button);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog("Set Up New Goal", "Input your new goal here:");
            }
        });

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
            seb.setTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(this, "Start Exercising!", Toast.LENGTH_SHORT).show();
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
            seb.setTextColor(Color.parseColor("#000000"));



            if( goal.isAchieved( Integer.parseInt(complete_content.getText().toString()) ) ) {
                promptDialog("Update New Goal", "You have completed today's goal! Do you want to set a new goal?");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If authentication was required during google fit setup, this will be called after the user authenticates
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
        long remaining = this.goal.getStep() - stepCount;
        if(remaining>0){
            remaining_content.setText(String.valueOf(remaining));
        }
        else if (!remaining_content.getText().toString().equals("DONE!")) {
            remaining_content.setText("DONE!");
            Toast.makeText(this, "Congratulations! You have completed today's goal!", Toast.LENGTH_LONG).show();
        }

    }

    public void setGoalCount(long goal) {
        goal_content.setText(String.valueOf(goal));
    }

    public int getGoalCount() {
        return this.goal.getStep();
    }

    private void promptDialog ( String title, String message ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        newGoalStep = -1;

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input as integer;
        input.setInputType(InputType.TYPE_CLASS_NUMBER );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGoalStep = Integer.parseInt(input.getText().toString());
                if (newGoalStep != -1) {
                    goal.setStep(newGoalStep);
                    setGoalCount(goal.getStep());
                    int complete = Integer.parseInt(complete_content.getText().toString());
                    int remaining = newGoalStep - complete;
                    if (remaining <= 0) {
                        remaining_content.setText("DONE!");
                    } else {
                        remaining_content.setText(String.valueOf(remaining));
                    }

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}