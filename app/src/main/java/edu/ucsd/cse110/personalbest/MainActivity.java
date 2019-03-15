package edu.ucsd.cse110.personalbest;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsd.cse110.personalbest.Managers.FireStoreManager;
import edu.ucsd.cse110.personalbest.Managers.SharedPrefManager;
import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    // Default service key is GOOGLE_FIT for the MainActivity
    private String fitnessServiceKey = "GOOGLE_FIT";
    private FitnessService fitnessService;
    // 0 for standby, 1 for active
    private static int state = 0;

    private TextView exercise_label;
    private TextView exercise_step_label;
    private TextView exercise_time_label;
    private TextView exercise_speed_label;
    private TextView goal_content;
    private TextView complete_content;
    private TextView remaining_content;
    private TextView exercise_step_content;
    private TextView exercise_time_content;
    private TextView exercise_speed_content;

    private Button update_step_button;
    private Button update_goal_button;
    private Button walk_history_button;
    private Button start_button;
    private Button friend_list_button;
    private Button set_email_button;
    // Button for demo
    private Button add_steps_button;
    private Button add_day_button;

    private User user;
    private SharedPreferences sharedPreferences;
    private SharedPrefManager sharedPrefManager;
    private FireStoreManager fireStoreManager;

    public int[] weekSteps = new int[7];

    // For demonstration purpose
    private boolean demo = false;

    // using google fit api
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
        this.user = new User();
        this.sharedPreferences = getSharedPreferences("user_name", MODE_PRIVATE);
        this.sharedPrefManager = new SharedPrefManager(this.sharedPreferences, this.user);
        sharedPrefManager.retrieveData();
        sharedPrefManager.publishData();

        // When running test, change service key to TEST_SERVICE
        if (getIntent().getStringExtra(FITNESS_SERVICE_KEY) != null) {
            fitnessServiceKey = getIntent().getStringExtra(FITNESS_SERVICE_KEY);
        } else {
            this.fireStoreManager = new FireStoreManager(this.user);
        }

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);

        // Initial setup for google fit service
        fitnessService.setup();

        // Click on the update button will update steps count
        update_step_button = findViewById(R.id.update_button);
        update_step_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo = false;
                fitnessService.updateStepCount();
                // setBarChart();
            }
        });

        // manually chang the goal
        update_goal_button = findViewById(R.id.goal_update_button);
        update_goal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog("Set Up New Goal", "Input your new goal here:", true);
            }
        });

        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState(v);
            }
        });

        friend_list_button = (Button) findViewById(R.id.friend_list_button);
        friend_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToFriendList();
            }
        });

        set_email_button = (Button) findViewById( R.id.set_email_button);
        set_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog("Set Up User Email","Input your email address here:", false);
            }
        });

        walk_history_button = (Button) findViewById(R.id.run_history_button);
        walk_history_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showBarChart(view);
            }
        });

        add_steps_button = (Button) findViewById(R.id.add_steps_button);
        add_steps_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                demo = true;
                setCompleteContent(user.getTotalSteps() + 500);
            }
        });

        add_day_button = (Button) findViewById(R.id.add_day_button);
        add_day_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                demo = true;
                int today = user.getCurrentDay();
                user.setCurrentDay(today + 1);
                user.compareCurrentDay(today);
            }
        });

        // get current walking stats including goal, steps completed and step remaining
        goal_content = findViewById(R.id.goal_content);
        complete_content = findViewById(R.id.complete_content);
        remaining_content = findViewById(R.id.remaining_content);
        exercise_time_content = findViewById(R.id.exercise_time_content);
        exercise_speed_content = findViewById(R.id.exercise_speed_content);
        exercise_step_content = findViewById(R.id.exercise_step_content);
        exercise_label = findViewById(R.id.exercise_label);
        exercise_time_label = findViewById(R.id.exercise_time_label);
        exercise_speed_label = findViewById(R.id.exercise_speed_label);
        exercise_step_label = findViewById(R.id.exercise_step_label);

        this.setGoalContent(this.user.getGoal());
        if (!demo) {
            fitnessService.updateStepCount();
        }

        // when not in intentional walk mode
        if(state == 0) {
            setExerciseVisibility(View.INVISIBLE);
        }
    }

    /* keep track of stats when the app is running in background */
    private class walkUpdateTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            final long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
            if (!demo) {
                fitnessService.updateStepCount();
            }
            // create new intentional walk object
            user.setCurExercise(new Exercise(currentTime), true);
            int start_step = user.getTotalSteps();

            // while walking
            while (state == 1){
                try{
                    Thread.sleep(1000);
                    String[] publishable = new String[3];

                    if (!demo) {
                        fitnessService.updateStepCount();
                    }

                    // set the steps and time
                    user.getCurExercise().setStep(user.getTotalSteps() - start_step);
                    user.getCurExercise().setTime(Calendar.getInstance().getTimeInMillis() / 1000);

                    // allocate stats into corresponding slot
                    publishable[0] = "" + user.getCurExercise().getStep();
                    publishable[1] = "" + user.getCurExercise().getTimeElapsed();
                    publishable[2] = "" + user.getCurExercise().getSpeed();
                    publishProgress(publishable);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            return "Congratulations! You walked "+user.getCurExercise().getStep()+" steps during this workout.";
        }

        /* Keep track of stats when in intentional walk */
        protected void onProgressUpdate(String... text){
            exercise_step_content.setText(text[0] + " steps");
            exercise_time_content.setText(text[1] + " seconds");
            exercise_speed_content.setText(text[2] + " km/h");
        }

        /* save the user stats using sharedPreferences */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            user.setExerciseSteps(user.getCurExercise().getStep(), true);
        }
    }

    private void switchToFriendList(){
        Intent intent = new Intent( this, FriendListActivity.class);
        intent.putExtra("user_email", user.getEmailAddress());
        startActivity(intent);
    }

    /* function to handle situation when switching between intentional walk and normal walk */
    private void switchState(View v){
        // when in normal walk
        if(state == 0){
            state = 1;
            start_button.setText("End");
            setExerciseVisibility(View.VISIBLE);
            walkUpdateTask runner = new walkUpdateTask();
            runner.execute();
            start_button.setTextColor(Color.parseColor("#FF0000"));
            Toast.makeText(this, "Start Exercising!", Toast.LENGTH_SHORT).show();
        }

        // when in intentional walk
        else{
            state = 0;
            setExerciseVisibility(View.INVISIBLE);
            start_button.setText("Start");
            start_button.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void setExerciseVisibility(int visibility) {
        this.exercise_time_content.setVisibility(visibility);
        this.exercise_speed_content.setVisibility(visibility);
        this.exercise_step_content.setVisibility(visibility);
        this.exercise_label.setVisibility(visibility);
        this.exercise_speed_label.setVisibility(visibility);
        this.exercise_time_label.setVisibility(visibility);
        this.exercise_step_label.setVisibility(visibility);
    }

    public void setCompleteContent(int stepCount) {
        this.user.setTotalSteps(stepCount, true);
        complete_content.setText(String.valueOf(stepCount));
        this.setRemainingContent();
        if (user.getGoal() != user.getTotalSteps()) {
            displayEncouragement();
        }
    }

    public void displayEncouragement() {
        int size = this.user.getWalkHistory().size();
        // Check whether there is data from yesterday
        if ( size > 1) {
            int improvement = this.user.getTotalSteps() - (int)this.user.getWalkHistory().get(size - 2);
            if (improvement > 0 && improvement % 500 == 0) {
                Toast.makeText(this, "Congratulations! You have improved " + improvement + " steps!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setRemainingContent() {
        int remaining = this.user.getGoal() - this.user.getTotalSteps();
        if(remaining > 0){
            remaining_content.setText(String.valueOf(remaining));
        }
        // show "DONE!" when the goal is achieved and make a toast message to remind the user
        else if (!remaining_content.getText().toString().equals("DONE!")) {
            remaining_content.setText("DONE!");
            promptDialog("Set Up New Goal", "Input your new goal here:", true);
            Toast.makeText(this, "Congratulations! You have completed today's goal!", Toast.LENGTH_LONG).show();
        }
    }

    public void setGoalContent(int goal) {
        goal_content.setText(String.valueOf(goal));
    }

    /* function to show the bar chart recording user's past week's work out */
    public void showBarChart(View view){
        Intent intent=new Intent(this,BarActivity.class);
        intent.putExtra("source","default");
        startActivity(intent);
    }

    /* function to show the dialog to prompt user to enter new goal */
    private void promptDialog ( String title, String message, boolean isGoal ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input as integer;
        if( isGoal ) {
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        builder.setView(input);

        // Set up the positive button ("Yes" button)
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( isGoal ) {
                    int newGoalStep;
                    try {
                        newGoalStep = Integer.parseInt(input.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                    user.setGoal(newGoalStep, true);
                    setGoalContent(user.getGoal());
                    setRemainingContent();
                } else {
                    String userEmail;
                    userEmail = input.getText().toString();

                    user.setEmailAddress(userEmail, true);
                    Toast.makeText(MainActivity.this, "Successfully set up the email! You can add friend now.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // set the negative button ("No" button)
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // show the dialog
        builder.show();
    }

    //Auto update on Resume
    @Override
    public void onResume() {
        super.onResume();
        if (!demo) {
            fitnessService.updateStepCount();
        }
    }

    public User getUser() {
        return this.user;
    }
}