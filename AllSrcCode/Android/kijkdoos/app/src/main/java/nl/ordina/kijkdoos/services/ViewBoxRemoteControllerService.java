package nl.ordina.kijkdoos.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

public class ViewBoxRemoteControllerService extends Service {
    private final IBinder binder = new LocalBinder();

    public ViewBoxRemoteControllerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        ViewBoxRemoteControllerService getService() {
            return ViewBoxRemoteControllerService.this;
        }
    }
}
