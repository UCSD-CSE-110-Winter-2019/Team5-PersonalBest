package edu.ucsd.cse110.personalbest.fitness;

// Code adapted from lab4-google-fit-master
public interface FitnessService {
    int getRequestCode();
    void setup();
    void updateStepCount();
    void getStepHistory();
}
