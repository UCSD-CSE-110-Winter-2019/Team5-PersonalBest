package edu.ucsd.cse110.personalbest;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;

import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class DisplayCongratsTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView complete_content;
    private TextView goal_content;
    private TextView remaining_content;
    private Button update_button;
    private int goal;
    private long nextStepCount;

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

        complete_content = activity.findViewById(R.id.complete_content);
        update_button = activity.findViewById(R.id.update_button);
        goal_content = activity.findViewById(R.id.goal_content);
        remaining_content = activity.findViewById(R.id.remaining_content);
    }

    /*
        Given goal is set to 5000
        When the program just initialize
        Then goal content should be 5000
        And complete content should be empty
        And remaining content should be 5000
     */
    @Test
    public void initializeTest() {
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("", complete_content.getText().toString());
        assertEquals("", remaining_content.getText().toString());
    }

    /*
        Given goal is set to 5000
        When 4999 steps are taken
        And update button is clicked
        Then goal content should be 5000
        And complete content should be 4999
        And remaining content should be 1
        And there should be no congrats message
     */
    @Test
    public void noCongratsTest() {
        nextStepCount = 4999;
        update_button.performClick();
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("4999", complete_content.getText().toString());
        assertEquals("1", remaining_content.getText().toString());
        assertNull(ShadowToast.getTextOfLatestToast());
    }

    /*
        Given goal is set to 5000
        When 5000 steps are taken
        And update button is clicked
        Then goal content should be 5000
        And complete content should be 5000
        And remaining content should be DONE!
        And there should be congrats message
     */
    @Test
    public void congratsTest() {
        nextStepCount = 5000;
        update_button.performClick();
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("5000", complete_content.getText().toString());
        assertEquals("DONE!", remaining_content.getText().toString());
        assertEquals("Congratulations! You have completed today's goal!", ShadowToast.getTextOfLatestToast());
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