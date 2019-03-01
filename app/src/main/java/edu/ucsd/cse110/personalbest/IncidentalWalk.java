package edu.ucsd.cse110.personalbest;

/* incidental step class */
public class IncidentalWalk implements Step {
    private int step;
    IncidentalWalk(){
        this.step=0;
    }
    IncidentalWalk(int step){this.step=step;}
    public void setStep(int step){
        this.step=step;
    }
    public int getStep(){
        return this.step;
    }
}
