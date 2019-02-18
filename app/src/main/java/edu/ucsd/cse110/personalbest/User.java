package edu.ucsd.cse110.personalbest;

import java.lang.UnsupportedOperationException;

public class User {

    //member variables
    public static int trackLength=7; //The number of days we track a user
    public static final int startingGoal=5000; //The starting goal of a new user
    private Goal goal;
    private IncidentalStep stepsToday;
    private IntentionalStep currentWalk;
    private IncidentalStep[] stepsHistory;
    private IntentionalStep[] walkHistory;
    private Goal[] goalHistory;

    // User constrcutor
    User(){
        this.goal=new Goal(startingGoal);
        this.stepsToday=new IncidentalStep();
        this.stepsHistory=new IncidentalStep[trackLength];
        this.walkHistory=new IntentionalStep[trackLength];
        this.goalHistory=new Goal[trackLength];
    }
}
