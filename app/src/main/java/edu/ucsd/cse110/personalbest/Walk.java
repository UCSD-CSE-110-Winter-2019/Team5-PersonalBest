package edu.ucsd.cse110.personalbest;

/* incidental step class */
public class Walk implements Step {
    private int step;
    public Walk(){
        this.step=0;
    }
    public Walk(int step){this.step=step;}
    public void setStep(int step){
        this.step=step;
    }
    public int getStep(){
        return this.step;
    }
}
