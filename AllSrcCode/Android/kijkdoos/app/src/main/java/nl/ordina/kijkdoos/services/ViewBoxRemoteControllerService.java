package nl.ordina.kijkdoos.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Consumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.dagger.BackgroundServiceFactory;
import nl.ordina.kijkdoos.threading.BackgroundService;

public class ViewBoxRemoteControllerService extends Service {
    private final IBinder binder = new LocalBinder();

    @Getter
    private ViewBoxRemoteController viewBoxRemoteController;

    @Inject
    BackgroundService backgroundService;

    @Override
    public void onCreate() {
        super.onCreate();

        BackgroundServiceFactory.getComponent().inject(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void connect(ViewBoxRemoteController viewBoxRemoteController, Consumer<Void> onConnectedConsumer, Consumer<Void> onErrorConsumer) {
        this.viewBoxRemoteController = viewBoxRemoteController;

        new Thread() {
            @Override
            public void run() {
                try {
                    final Future<Object> connection = backgroundService.getExecutorService()
                            .submit(() -> {
                                viewBoxRemoteController.connect(ViewBoxRemoteControllerService.this, onConnectedConsumer);

                                while (!viewBoxRemoteController.isConnected() && !Thread.interrupted()) {
                                }

                                return null;
                            });

                    connection.get(5, TimeUnit.SECONDS);
                    onConnectedConsumer.accept(null);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    if (onErrorConsumer != null) {
                        onErrorConsumer.accept(null);
                    }
                }
            }
        }.start();
    }

    public void disconnect(ViewBoxRemoteController viewBoxRemoteController) {
        viewBoxRemoteController.setDisconnectConsumer(null);
        viewBoxRemoteController.reset(aVoid -> viewBoxRemoteController.disconnect());
    }

    public static ServiceConnection bind(Context context, final Consumer<IBinder> connectConsumer,
                                         final Consumer<Void> disconnectConsumer) {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                connectConsumer.accept(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                disconnectConsumer.accept(null);
            }
        };
        context.bindService(new Intent(context, ViewBoxRemoteControllerService.class), serviceConnection, BIND_AUTO_CREATE);

        return serviceConnection;
    }

    public class LocalBinder extends Binder {
        public ViewBoxRemoteControllerService getService() {
            return ViewBoxRemoteControllerService.this;
        }
    }
}
