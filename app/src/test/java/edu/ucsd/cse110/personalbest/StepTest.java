package edu.ucsd.cse110.personalbest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.personalbest.fitness.GoogleFitAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class StepTest {
    private static final String TEST_SERVICE = "TEST_SERVICE";

    private MainActivity activity;
    private TextView textSteps;
    private TextView goal_content;
    private TextView remaining_content;
    private Button btnUpdateSteps;
    private long nextStepCount;

    @Before
    public void setUp() throws Exception {
        FitnessServiceFactory.put(TEST_SERVICE, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(MainActivity mainActivity) {
                return new TestFitnessService(mainActivity);
            }
        });
        activity = Robolectric.buildActivity(MainActivity.class).create().get();


        textSteps = activity.findViewById(R.id.complete_content);
        btnUpdateSteps = activity.findViewById(R.id.tmp_update_button);
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
        assertEquals("", textSteps.getText().toString());
        assertEquals("5000", goal_content.getText().toString());
        assertEquals("", remaining_content.getText().toString());
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
    }
}