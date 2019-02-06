package edu.ucsd.cse110.personalbest;

public class Goal implements Step {
    private int goal;
    Goal(){
        this.goal=0;
    }
    Goal(int goal){
        this.goal=goal;
    }
    public void setStep(int goal){
        this.goal=goal;
    }
    public int getStep(){
        return this.goal;
    }
}
