package edu.ucsd.cse110.personalbest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static int state=0; //0 for standby, 1 for active
    private TextView etic;
    private TextView espc;
    private TextView estc;
    private TextView exercising;
    private TextView etil;
    private TextView espl;
    private TextView estl;
    /* private class updateTask extends AsyncTask<String,String,String>{

         @Override
         protected String doInBackground(String... strings) {
             int step=100000;
             while(true){
                 try{
                     Thread.sleep(step);
                     String[] publishable=new String[2];
                     publishable[0]="";//Normal update value
                     publishable[1]="";//Normal update value
                     publishProgress();
                 }
                 catch(InterruptedException e){
                     e.printStackTrace();
                 }
             }
         }

         protected void onProgressUpdate(String... text){
             //Normal updates here
         }
     }*/
    private class walkUpdateTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            int step=1000;
            final long currentTime=Calendar.getInstance().getTimeInMillis()/1000;
            IntentionalStep walk=new IntentionalStep(currentTime);
            while(state==1){
                try{
                    Thread.sleep(step);
                    String[] publishable=new String[3];
                    walk.setStep(300);//Todo
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
            return "";
        }

        protected void onProgressUpdate(String... text){

            estc.setText(text[0]);
            etic.setText(text[1]);
            espc.setText(text[2]);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            seb.setTextColor(Color.parseColor("#ff0000"));
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
            seb.setTextColor(Color.parseColor("#000000"));
        }
    }
}


