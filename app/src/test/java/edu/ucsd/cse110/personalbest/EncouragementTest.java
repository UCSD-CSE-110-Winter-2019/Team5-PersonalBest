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

import java.util.ArrayList;

import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class EncouragementTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView complete_content;
    private TextView goal_content;
    private TextView remaining_content;
    private Button update_button;
    private int nextStepCount;

    private User test_user;

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
        test_user = activity.getUser();

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
        assertEquals("0", complete_content.getText().toString());
        assertEquals("5000", remaining_content.getText().toString());
    }

    /*
        Given goal is set to 5000
        When the update button is clicked
        Then goal content should be 0
        And complete content should be 5000
        And remaining content should be 5000
     */
    @Test
    public void initialUpdateTest() {
        ArrayList<Integer> walkhistroy = new ArrayList<>();
        walkhistroy.add(3000);
        walkhistroy.add(0);
        test_user.setWalkHistory(walkhistroy,false);

        nextStepCount = 3499;
        test_user.setTotalSteps(nextStepCount, false);
        update_button.performClick();
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("3499", complete_content.getText().toString());
        assertEquals("1501", remaining_content.getText().toString());
        assertEquals(null, ShadowToast.getTextOfLatestToast());

        nextStepCount = 3500;
        test_user.setTotalSteps(nextStepCount, false);
        update_button.performClick();
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("3500", complete_content.getText().toString());
        assertEquals("1500", remaining_content.getText().toString());
        assertEquals("Congratulations! You have improved 500 steps!", ShadowToast.getTextOfLatestToast());

        nextStepCount = 4500;
        test_user.setTotalSteps(nextStepCount, false);
        update_button.performClick();
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("4500", complete_content.getText().toString());
        assertEquals("500", remaining_content.getText().toString());
        assertEquals("Congratulations! You have improved 1500 steps!", ShadowToast.getTextOfLatestToast());
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
            mainActivity.setCompleteContent(nextStepCount);
            mainActivity.setRemainingContent();
        }

        @Override
        public void getStepHistory(){

        }
    }
}