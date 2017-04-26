package nl.ordina.kijkdoos.bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Predicate;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import nl.ordina.kijkdoos.bluetooth.discovery.AbstractBluetoothDiscoveryService;

import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

/**
 * Created by coenhoutman on 27/03/2017.
 */

public class BluetoothConnectionFragment extends Fragment {

    @Inject
    AbstractBluetoothDiscoveryService bluetoothService;

    private BroadcastReceiver receiver;
    private final Map<Predicate<Integer>, Consumer<Integer>> connectionEventHandlers;

    public BluetoothConnectionFragment() {
        connectionEventHandlers = new HashMap<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewBoxApplication(this).getApplicationComponent().inject(this);

        registerBluetoothBroadcastReceiver();
    }

    private void registerBluetoothBroadcastReceiver() {
        final IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final int state = intent.getIntExtra(EXTRA_STATE, BluetoothAdapter.ERROR);

                Stream.of(connectionEventHandlers.entrySet()).filter(entry -> entry.getKey().test(state))
                        .forEach(entry -> entry.getValue().accept(state));
            }
        };
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void addConnectionEventHandler(Predicate<Integer> predicate, Consumer<Integer> action) {
        connectionEventHandlers.put(predicate, action);
    }

    public static BluetoothConnectionFragment add(FragmentActivity activity) {
        final BluetoothConnectionFragment bluetoothConnectionFragment = new BluetoothConnectionFragment();
        activity.getSupportFragmentManager().beginTransaction().add(bluetoothConnectionFragment, null).commit();

        return bluetoothConnectionFragment;
    }

    public void clearConnectionEventHandlers() {
        synchronized (connectionEventHandlers) {
            connectionEventHandlers.clear();
        }
    }
}
