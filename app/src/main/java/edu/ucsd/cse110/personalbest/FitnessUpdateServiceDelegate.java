package edu.ucsd.cse110.personalbest;

public interface FitnessUpdateServiceDelegate {
    void updateFields(int goal, int currentSteps, Exercise currentWalk);
}
