package edu.ucsd.cse110.personalbest;

import java.util.ArrayList;
import java.util.Collection;

public class User implements ISubject<IUserObserver>{
    public static final int DAY_OF_MONTH = 30;

    private Collection<IUserObserver> observers;

    private String emailAddress;

    private int goal;
    // Walk is object for the  total step
    private int curSteps;
    // Exercise is the object for the exercise part
    private Exercise curExercise;

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
            observer.onDataChange();
        }
    }

    // User constrcutor
    User(){
        this.observers = new ArrayList<IUserObserver>();
        this.emailAddress = "default";
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
        return this.curExercise;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setGoal(int goal, boolean notify){
        this.goal = goal;
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setCurSteps(int curSteps, boolean notify){
        this.curSteps = curSteps;
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setCurExercise(Exercise curExercise, boolean notify){
        this.curExercise = curExercise;
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setEmailAddress(String emailAddress, boolean notify) {
        this.emailAddress = emailAddress;
        if (notify) {
            this.notifyObservers();
        }
    }
}
