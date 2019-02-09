package edu.ucsd.cse110.personalbest;

public class IntentionalStep implements Step {
    private int step;
    private long timeCurrent; //Change to relevant time class if necessary
    private long timeStart; //Change to relevant time class if necessary
    private long timeElapsed; //Change to relevant time class if necessary
    //Dummy default constructor; do not use
    IntentionalStep(){
        this.step=0;
    }
    //Constructor for user pressing start
    IntentionalStep(long timeStart){
        this.step=0;
        this.timeStart=timeStart;
    }
    //Constructor for intentional step collection for the bar chart statistics; create one when one
    //intentional walk is finished and update this when others are finished
    IntentionalStep(int step,long timeElapsed){
        this.step=step;
        this.timeStart=0; //Don't care
        this.timeCurrent=0; //Don't care
        this.timeElapsed=timeElapsed;
    }
    public void setStep(int step) {
        this.step = step;
    }
    public void setTime(long timeCurrent){
        this.timeCurrent = timeCurrent;
        this.timeElapsed = this.timeCurrent-this.timeStart;
    }
    public int getStep(){
        return this.step;
    }
    public long getTimeElapsed(){
        return this.timeCurrent-this.timeStart;
    }
    public long getSpeed(){
        return this.step/this.timeElapsed;
    }
}
