package nl.ordina.kijkdoos.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.annimon.stream.function.Consumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.Getter;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

public class ViewBoxRemoteControllerService extends Service {
    private final IBinder binder = new LocalBinder();

    @Nullable
    @Getter
    private ViewBoxRemoteController viewBoxRemoteController;

    private ExecutorService backgroundThread;

    @Override
    public void onCreate() {
        super.onCreate();

        backgroundThread = Executors.newSingleThreadExecutor();
    }

    @Nullable
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
                    final Future<Object> connection = backgroundThread
                            .submit(() -> {
                                viewBoxRemoteController.connect(ViewBoxRemoteControllerService.this, null);

                                while (!viewBoxRemoteController.isConnected() && !Thread.interrupted()) {
                                }

                                return null;
                            });

                    connection.get(5, TimeUnit.SECONDS);

                    if (onConnectedConsumer != null) {
                        onConnectedConsumer.accept(null);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    if (onErrorConsumer != null) {
                        onErrorConsumer.accept(null);
                    }
                }
            }
        }.start();
    }

    public void disconnect() {
        if (viewBoxRemoteController == null) return;

        viewBoxRemoteController.setDisconnectConsumer(null);
        viewBoxRemoteController.reset(aVoid -> {
            viewBoxRemoteController.disconnect();
            viewBoxRemoteController = null;
        });
    }

    public static ServiceConnection bind(Context context, final Consumer<ViewBoxRemoteControllerService> connectConsumer,
                                         final Consumer<Void> disconnectConsumer) {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                final ViewBoxRemoteControllerService viewBoxRemoteControllerService = ((LocalBinder) service).getService();
                connectConsumer.accept(viewBoxRemoteControllerService);
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
