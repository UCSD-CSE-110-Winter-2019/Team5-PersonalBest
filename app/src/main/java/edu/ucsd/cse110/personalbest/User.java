package edu.ucsd.cse110.personalbest;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class User implements ISubject<IUserObserver> {
    public static final String TAG = "UserClass";
    public static final int MS_IN_DAY = 86400000;
    public static final int MS_IN_HOUR = 3600000;

    private Collection<IUserObserver> observers;

    private String emailAddress;

    private int goal;
    // Walk is object for the  total step
    private int totalSteps;
    // Exercise is the object for the exercise part
    private int exerciseSteps;
    private Exercise curExercise;

    private int currentDay;
    private ArrayList<Integer> goalHistory;
    private ArrayList<Integer> walkHistory;
    private ArrayList<Integer> exerciseHistory;

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
    User() {
        this.observers = new ArrayList<IUserObserver>();
        this.emailAddress = "default";
        this.goal = 0;
        this.totalSteps = 0;
        this.curExercise = new Exercise();
        this.currentDay = (int) ((Calendar.getInstance().getTimeInMillis() - 7 * MS_IN_HOUR) / MS_IN_DAY);
        Log.i(TAG, (Calendar.getInstance().getTimeInMillis() - 7 * MS_IN_HOUR) + "");
        Log.i(TAG, this.currentDay + "");
        this.goalHistory = new ArrayList<>();
        this.walkHistory = new ArrayList<>();
        this.exerciseHistory = new ArrayList<>();
    }

    public int getGoal() {
        return this.goal;
    }

    public int getTotalSteps() {
        return this.totalSteps;
    }

    public int getExerciseSteps() {
        return this.exerciseSteps;
    }

    public Exercise getCurExercise() {
        return this.curExercise;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public int getCurrentDay() {
        return this.currentDay;
    }

    public ArrayList<Integer> getGoalHistory() {
        return this.goalHistory;
    }

    public ArrayList<Integer> getWalkHistory() {
        return this.walkHistory;
    }

    public ArrayList<Integer> getExerciseHistory() {
        return this.exerciseHistory;
    }

    public void compareCurrentDay(int currentDay) {
        for (int i = 0; i < this.currentDay - currentDay; i++) {
            this.goalHistory.add(0);
            this.walkHistory.add(0);
            this.exerciseHistory.add(0);
        }
        this.notifyObservers();
    }

    public void setGoal(int goal, boolean notify) {
        this.goal = goal;
        if (this.goalHistory.size() > 0) {
            this.goalHistory.set(goalHistory.size() - 1, goal);
        }
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setTotalSteps(int curSteps, boolean notify) {
        this.totalSteps = curSteps;
        if (this.walkHistory.size() > 0) {
            this.walkHistory.set(walkHistory.size() - 1, curSteps);
        }
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setExerciseSteps(int exerciseSteps, boolean notify) {
        this.exerciseSteps = exerciseSteps;
        if (this.walkHistory.size() > 0) {
            int previous_exercies = this.exerciseHistory.get(exerciseHistory.size() - 1);
            this.exerciseHistory.set(exerciseHistory.size() - 1, previous_exercies + exerciseSteps);
        }
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setCurExercise(Exercise curExercise, boolean notify) {
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

    public void setGoalHistory(ArrayList<Integer> goalHistory, boolean notify) {
        this.goalHistory = goalHistory;
        if (notify) {
            this.notifyObservers();
        }

    }

    public void setExerciseHistory(ArrayList<Integer> exerciseHistory,boolean notify) {
        this.exerciseHistory = exerciseHistory;
        if (notify) {
            this.notifyObservers();
        }
    }

    public void setWalkHistory(ArrayList<Integer> walkHistory,boolean notify) {
        this.walkHistory = walkHistory;
        if (notify) {
            this.notifyObservers();
        }
    }


    // For demo purpose
    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
        this.notifyObservers();
    }

}
