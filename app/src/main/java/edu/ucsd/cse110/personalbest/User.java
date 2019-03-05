package edu.ucsd.cse110.personalbest;

public class User {

    //member variables
    public static int trackLength=7; //The number of days we track a user
    public static final int startingGoal=5000; //The starting goal of a new user
    private Goal goal;
    private Walk currentWalk;
    private Exercise currentExercise;
    private Walk[] walkHistory;
    private Exercise[] exerciseHistory;
    private Goal[] goalHistory;
    private int height;

    // User constrcutor
    User(){
    }

    public Goal getGoal(){
        return goal;
    }

    public Goal[] getGoalHistory(){
        return goalHistory;
    }

    public Walk getCurrentWalk(){
        return currentWalk;
    }

    public Walk[] getWalkHistory(){
        return walkHistory;
    }

    public Exercise getCurrentExercise(){
        return currentExercise;
    }

    public Exercise[] getExerciseHistory(){
        return exerciseHistory;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height=height;
    }

    public void setGoal(Goal goal){
        this.goal=goal;
    }

    public void setGoalHistory(Goal[] goalHistory){
        this.goalHistory=goalHistory;
    }

    public void setCurrentWalk(Walk currentWalk){
        this.currentWalk=currentWalk;
    }

    public void setCurrentExercise(Exercise currentExercise){
        this.currentExercise=currentExercise;
    }

    public void setWalkHistory(Walk[] walkHistory){
        this.walkHistory=walkHistory;
    }

    public void setExerciseHistory(Exercise[] exerciseHistory){
        this.exerciseHistory=exerciseHistory;
    }
}
