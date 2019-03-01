package edu.ucsd.cse110.personalbest;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowToast;

import android.widget.Button;
import android.widget.TextView;

import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class stepTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView textSteps;
    private TextView goal_content;
    private TextView remaining_content;
    private Button btnUpdateSteps;
    private Button btnUpdateSteps2;
    private long nextStepCount;
    private long nextStepCount2;

    @Before
    public void setUp() throws Exception {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity);
            }
        });
        Intent intent = new Intent(RuntimeEnvironment.application, MainActivity.class);
        intent.putExtra(MainActivity.FITNESS_SERVICE_KEY, TEST_SERVICE);
        activity = Robolectric.buildActivity(MainActivity.class, intent).create().get();

        textSteps = activity.findViewById(R.id.complete_content);
        btnUpdateSteps = activity.findViewById(R.id.update_button);
        goal_content = activity.findViewById(R.id.goal_content);
        remaining_content = activity.findViewById(R.id.remaining_content);
        nextStepCount = 1337;


    }

    @Test
    public void initialTest() {
        assertEquals("", textSteps.getText().toString());
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("", remaining_content.getText().toString());
    }

    @Test
    public void updateTest() {
        btnUpdateSteps.performClick();
        assertEquals("1337", textSteps.getText().toString());
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("3663", remaining_content.getText().toString());
    }

    @Test
    public void completeTest1(){

        nextStepCount = 5000;
        goal_content = activity.findViewById(R.id.goal_content);
        remaining_content = activity.findViewById(R.id.remaining_content);
        btnUpdateSteps.performClick();
        assertEquals("5000", textSteps.getText().toString());
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("DONE!", remaining_content.getText().toString());
        assertEquals("Congratulations! You have completed today's goal!", ShadowToast.getTextOfLatestToast());

    }

    @Test
    public void completeTest2(){

        nextStepCount = 5001;
        goal_content = activity.findViewById(R.id.goal_content);
        remaining_content = activity.findViewById(R.id.remaining_content);
        btnUpdateSteps.performClick();
        assertEquals("5001", textSteps.getText().toString());
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("DONE!", remaining_content.getText().toString());
        assertEquals("Congratulations! You have completed today's goal!", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void setGoal(){
        activity.setGoalCount(6000);
        goal_content = activity.findViewById(R.id.goal_content);
        btnUpdateSteps.performClick();
        assertEquals("6000", goal_content.getText().toString());
    }

    @Test
    public void updateGoal(){
        activity.setGoalCount(5000);
        goal_content = activity.findViewById(R.id.goal_content);
        btnUpdateSteps.performClick();
        assertEquals("5000", goal_content.getText().toString());
        activity.setGoalCount(6000);
        goal_content = activity.findViewById(R.id.goal_content);
        btnUpdateSteps.performClick();
        assertEquals("6000", goal_content.getText().toString());
    }

    private class TestFitnessService implements FitnessService {
        private static final String TAG = "[TestFitnessService]: ";
        private MainActivity mainActivity;

        public TestFitnessService(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public int getRequestCode() {
            return 0;
        }

        @Override
        public void setup() {
            System.out.println(TAG + "setup");
        }

        @Override
        public void updateStepCount() {
            System.out.println(TAG + "updateStepCount");
            mainActivity.setStepCount(nextStepCount);
        }

        @Override
        public void getStepHistory(){

        }
    }
}