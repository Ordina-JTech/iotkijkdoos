package nl.ordina.kijkdoos;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.bluetooth.AbstractBluetoothService;
import nl.ordina.kijkdoos.bluetooth.BluetoothDeviceWrapper;
import nl.ordina.kijkdoos.bluetooth.DeviceFoundListener;

import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

public class SearchViewBoxActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DeviceFoundListener {

    @Inject
    AbstractBluetoothService bluetoothService;

    @BindView(R.id.viewBoxList)
    ListView viewBoxList;
    private ArrayAdapter<String> viewBoxListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_box);
        ButterKnife.bind(this);
        getViewBoxApplication(this).getApplicationComponent().inject(this);

        viewBoxListAdapter = new ArrayAdapter<>(this, R.layout.listitem_device, R.id.device_name);
        viewBoxList.setAdapter(viewBoxListAdapter);
        viewBoxList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        bluetoothService.searchDevices(this);
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewBoxActivity.class));
    }

    @Override
    public void onDeviceFound(BluetoothDeviceWrapper bluetoothDevice) {
        String address = bluetoothDevice.getDevice().getAddress();
        viewBoxListAdapter.add(address);
    }
}
