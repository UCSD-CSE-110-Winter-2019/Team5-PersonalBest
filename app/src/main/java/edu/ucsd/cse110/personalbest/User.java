package edu.ucsd.cse110.personalbest;

import java.util.ArrayList;
import java.util.Collection;

public class User implements ISubject<IUserObserver>{
    public static final int DAY_OF_MONTH = 30;

    private Collection<IUserObserver> observers;

    @Override
    public void register(IUserObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(IUserObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IUserObserver observer : this.observers) {
            observer.onDataChange(this);
        }
    }

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
        this.observers = new ArrayList<IUserObserver>();
        this.goal = new Goal();
        this.currentWalk = new Walk();
        this.currentExercise = new Exercise();
        this.walkHistory = new Walk[DAY_OF_MONTH];
        this.exerciseHistory = new Exercise[DAY_OF_MONTH];
        this.goalHistory = new Goal[DAY_OF_MONTH];
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
        this.notifyObservers();
    }

    public void setGoal(Goal goal){
        this.goal=goal;
        this.notifyObservers();
    }

    public void setGoalHistory(Goal[] goalHistory){
        this.goalHistory=goalHistory;
        this.notifyObservers();
    }

    public void setCurrentWalk(Walk currentWalk){
        this.currentWalk=currentWalk;
        this.notifyObservers();
    }

    public void setCurrentExercise(Exercise currentExercise){
        this.currentExercise=currentExercise;
        this.notifyObservers();
    }

    public void setWalkHistory(Walk[] walkHistory){
        this.walkHistory=walkHistory;
        this.notifyObservers();
    }

    public void setExerciseHistory(Exercise[] exerciseHistory){
        this.exerciseHistory=exerciseHistory;
        this.notifyObservers();
    }
}
