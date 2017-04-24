package nl.ordina.kijkdoos.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.BluetoothConnectionFragment;
import nl.ordina.kijkdoos.view.control.AbstractControlFragment;
import nl.ordina.kijkdoos.view.search.SearchViewBoxActivity;

import static android.bluetooth.BluetoothAdapter.STATE_ON;
import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

public class BluetoothDisabledActivity extends AppCompatActivity {

    @BindView(R.id.enableBluetoothButton)
    Button enableBluetoothButton;
    private BluetoothConnectionFragment bluetoothConnectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_disabled);
        ButterKnife.bind(this);

        bluetoothConnectionFragment = new BluetoothConnectionFragment();
        getSupportFragmentManager().beginTransaction().add(bluetoothConnectionFragment, null).commit();

        bluetoothConnectionFragment.addConnectionEventHandler(state -> state == STATE_ON, state -> startSearchViewBoxActivity());
    }

    @OnClick(R.id.enableBluetoothButton)
    public void enableBluetooth() {
        AbstractBluetoothService.askUserToEnableBluetooth(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AbstractBluetoothService.REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            bluetoothConnectionFragment.clearConnectionEventHandlers();
            startSearchViewBoxActivity();
        }
    }

    private void startSearchViewBoxActivity() {
        final Intent intent = new Intent(this, SearchViewBoxActivity.class);
        startActivity(intent);

        finish();
    }
}
