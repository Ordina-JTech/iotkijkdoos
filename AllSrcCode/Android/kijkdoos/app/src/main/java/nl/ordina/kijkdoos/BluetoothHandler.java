package nl.ordina.kijkdoos;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.Set;

/**
 The Android platform includes support for the Bluetooth network stack, which allows a device to
 wirelessly exchange data with other Bluetooth devices. The application framework provides access
 to the Bluetooth functionality through the Android Bluetooth APIs. These APIs let applications
 wirelessly connect to other Bluetooth devices, enabling point-to-point and multipoint wireless
 features.

 Using the Bluetooth APIs, an Android application can perform the following:
     Scan for other Bluetooth devices
     Query the local Bluetooth adapter for paired Bluetooth devices
     Establish RFCOMM channels
     Connect to other devices through service discovery
     Transfer data to and from other devices
     Manage multiple connections

 This class focuses on Bluetooth Low Energy
    For Bluetooth devices with low power requirements, Android 4.3 (API level 18) introduces API
    support for Bluetooth Low Energy. To learn more, see Bluetooth Low Energy.
    https://developer.android.com/guide/topics/connectivity/bluetooth-le.html

    Android 4.3 (API level 18) introduces built-in platform support for Bluetooth Low Energy (BLE)
    in the central role and provides APIs that apps can use to discover devices, query for services,
    and transmit information.

    Common use cases include the following:
        - Transferring small amounts of data between nearby devices.
        - Interacting with proximity sensors like Google Beacons to give users a customized
            experience based on their current location.

    In contrast to Classic Bluetooth, Bluetooth Low Energy (BLE) is designed to provide significantly
    lower power consumption. This allows Android apps to communicate with BLE devices that have
    stricter power requirements, such as proximity sensors, heart rate monitors, and fitness devices.

 */
public class BluetoothHandler {
    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private Activity mActivity= null;

    public BluetoothHandler(Activity activity) {
        mActivity = activity;
    }

    /*  Get/initialize the BluetoothAdapter.

        The BluetoothAdapter is required for any and all Bluetooth activity. The BluetoothAdapter
        represents the device's own Bluetooth adapter (the Bluetooth radio). There's one Bluetooth
        adapter for the entire system, and your application can interact with it using this object.

        The snippet below shows how to get the adapter. Note that this approach uses
        getSystemService() to return an instance of BluetoothManager, which is then used to get
        the adapter. Android 4.3 (API Level 18) introduces BluetoothManager:
            private BluetoothAdapter mBluetoothAdapter;
        */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager)mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
            // Initializes Bluetooth adapter.
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
        // if mBluetoothAdapter == null then Device does not support Bluetooth
        return (mBluetoothAdapter == null) ? false : true;
    }

    /*  Enable Bluetooth.

        Next, you need to ensure that Bluetooth is enabled. Call isEnabled() to check whether
        Bluetooth is currently enabled. If this method returns false, then Bluetooth is disabled.
        To request that Bluetooth be enabled, call startActivityForResult(), passing in
        an ACTION_REQUEST_ENABLE intent action. This call issues a request to enable
        Bluetooth through the system settings (without stopping your application).
     */
    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /*  Finding Devices (https://developer.android.com/guide/topics/connectivity/bluetooth.html#EnablingDiscoverability)

        Using the BluetoothAdapter, you can find remote Bluetooth devices either through device
        discovery or by querying the list of paired devices.

        Device discovery is a scanning procedure that searches the local area for Bluetooth-enabled
        devices and requests some information about each one. This process is sometimes referred to
        as discovering, inquiring, or scanning. However, a nearby Bluetooth device responds to a
        discovery request only if it is currently accepting information requests by being
        discoverable. If a device is discoverable, it will respond to the discovery request by
        sharing some information, such as the device's name, its class, and its unique MAC address.
        Using this information, the device that is performing the discovery process can then
        choose to initiate a connection to the discovered device.
        ...

        Note that there is a difference between being paired and being connected:
            - To be paired means that two devices are aware of each other's existence, have a shared
             link-key that can be used for authentication, and are capable of establishing an
             encrypted connection with each other.
            - To be connected means that the devices currently share an RFCOMM channel and are able
             to transmit data with each other. The current Android Bluetooth API's require devices
             to be paired before an RFCOMM connection can be established. Pairing is automatically
             performed when you initiate an encrypted connection with the Bluetooth APIs.
    */

    /*  Querying paired devices

        Before performing device discovery, it's worth querying the set of paired devices to see
        if the desired device is already known. To do so, call getBondedDevices(). This will return
        a set of BluetoothDevice objects representing paired devices. For example, you can query
        all paired devices and get the name and MAC address of each device, as the following code
        snippet demonstrates:
    */
    public void queryPairedDevices() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    }

    public void discoverDevices() {

    }

    public void findBLEDevices() {
        
    }
}
