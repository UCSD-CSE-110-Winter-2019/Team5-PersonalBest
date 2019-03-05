package edu.ucsd.cse110.personalbest.Helpers;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.Goal;
import edu.ucsd.cse110.personalbest.Step;
import edu.ucsd.cse110.personalbest.Walk;

public class StepIntHelper {
    public static int[] extractStepArray(Step[] stepArray){
        int length=stepArray.length;
        int[] retArray=new int[length];
        for(int i=0;i<length;i++){
            retArray[i]=stepArray[i].getStep();
        }
        return retArray;
    }

    public static Goal[] constructGoalArray(int[] intArray){
        int length=intArray.length;
        Goal[] retArray=new Goal[length];
        for(int i=0;i<length;i++){
            retArray[i].setStep(intArray[i]);
        }
        return retArray;
    }

    public static Walk[] constructWalkArray(int[] intArray){
        int length=intArray.length;
        Walk[] retArray=new Walk[length];
        for(int i=0;i<length;i++){
            retArray[i].setStep(intArray[i]);
        }
        return retArray;
    }

    public static Exercise[] constructExerciseArray(int[] intArray){
        int length=intArray.length;
        Exercise[] retArray=new Exercise[length];
        for(int i=0;i<length;i++){
            retArray[i].setStep(intArray[i]);
        }
        return retArray;
    }
}
