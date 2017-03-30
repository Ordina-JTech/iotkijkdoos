package nl.ordina.kijkdoos.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.view.search.SearchViewBoxActivity;

import static nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService.REQUEST_ENABLE_BLUETOOTH;

public class BluetoothDisabledActivity extends AppCompatActivity {

    @BindView(R.id.enableBluetoothButton)
    Button enableBluetoothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_disabled);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.enableBluetoothButton)
    public void enableBluetooth() {
        AbstractBluetoothService.askUserToEnableBluetooth(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {

                final Intent intent = new Intent(this, SearchViewBoxActivity.class);
                startActivity(intent);

                finish();
            }
        }
    }
}
