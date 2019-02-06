package edu.ucsd.cse110.personalbest;

public class IncidentalStep implements Step {
    private int step;
    IncidentalStep(){
        this.step=0;
    }
    public void setStep(int step){
        this.step=step;
    }
    public int getStep(){
        return this.step;
    }
}
