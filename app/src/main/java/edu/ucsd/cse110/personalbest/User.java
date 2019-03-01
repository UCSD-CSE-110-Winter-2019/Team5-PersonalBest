package edu.ucsd.cse110.personalbest;

public class User {

    //member variables
    public static int trackLength=7; //The number of days we track a user
    public static final int startingGoal=5000; //The starting goal of a new user
    private Goal goal;
    private IncidentalWalk stepsToday;
    private IntentionalWalk currentWalk;
    private IncidentalWalk[] stepsHistory;
    private IntentionalWalk[] walkHistory;
    private Goal[] goalHistory;

    // User constrcutor
    User(){
        this.goal=new Goal(startingGoal);
        this.stepsToday=new IncidentalWalk();
        this.stepsHistory=new IncidentalWalk[trackLength];
        this.walkHistory=new IntentionalWalk[trackLength];
        this.goalHistory=new Goal[trackLength];
    }
}
