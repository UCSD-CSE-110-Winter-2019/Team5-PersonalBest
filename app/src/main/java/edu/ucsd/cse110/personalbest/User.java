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
        return curExercise;
    }

    public String getEmailAddress() {
        return emailAddress;
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

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        this.notifyObservers();
    }
}
