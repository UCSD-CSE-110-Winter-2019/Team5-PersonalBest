package edu.ucsd.cse110.personalbest;

public interface FitnessUpdateServiceDelegate {
    void updateFields(Goal goal,IncidentalStep currentSteps, IntentionalStep currentWalk);
}
