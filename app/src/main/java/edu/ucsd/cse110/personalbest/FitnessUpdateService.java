package edu.ucsd.cse110.personalbest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import edu.ucsd.cse110.personalbest.fitness.FitnessService;
import edu.ucsd.cse110.personalbest.fitness.FitnessServiceFactory;
import edu.ucsd.cse110.personalbest.fitness.GoogleFitAdapter;

public class FitnessUpdateService extends Service {
    private final IBinder iBinder=new FitnessBinder();
    private FitnessUpdateServiceDelegate fitnessUpdateServiceDelegate;

    public class FitnessBinder extends Binder {
        public FitnessUpdateService getService() {
            return FitnessUpdateService.this;
        }
    }

    final class UpdateThread implements Runnable{
        int startId;
        public UpdateThread(int startId){
            this.startId=startId;
        }

        @Override
        public void run() {
            synchronized (this){

            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public void setDelegate(FitnessUpdateServiceDelegate fitnessServiceDelegate){
        this.fitnessUpdateServiceDelegate=fitnessServiceDelegate;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
