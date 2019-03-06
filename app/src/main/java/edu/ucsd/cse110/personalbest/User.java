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
    private int goal;
    // Walk is object for the  total step
    private int curSteps;
    // Exercise is the object for the exercise part
    private Exercise curExercise;

    // User constrcutor
    User(){
        this.observers = new ArrayList<IUserObserver>();
        this.goal = 0;
        this.curSteps = 0;
        this.curExercise = new Exercise();
    }

    public int getGoal(){
        return this.goal;
    }


    public int getCurSteps(){
        return this.curSteps;
    }

    public Exercise getCurExercise(){
        return curExercise;
    }

    public void setGoal(int goal){
        this.goal = goal;
        this.notifyObservers();
    }

    public void setCurSteps(int curSteps){
        this.curSteps = curSteps;
        this.notifyObservers();
    }

    public void setCurExercise(Exercise curExercise){
        this.curExercise = curExercise;
        this.notifyObservers();
    }
}
