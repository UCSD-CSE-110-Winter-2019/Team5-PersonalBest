package edu.ucsd.cse110.personalbest;

/* Goal class */
public class Goal implements Step {
    private int goal;

    Goal(){
        this.goal = 0;
    }

    Goal(int goal){
        this.goal = goal;
    }

    public void setStep(int goal){
        this.goal = goal;
    }

    public int getStep(){
        return this.goal;
    }

    public boolean isAchieved( int step ) {
        if( step >= goal ) {
            return true;
        }
        return false;
    }
}
