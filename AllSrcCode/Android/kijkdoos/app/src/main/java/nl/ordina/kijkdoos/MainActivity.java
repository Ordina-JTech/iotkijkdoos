package nl.ordina.kijkdoos;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import nl.ordina.kijkdoos.bluetoothlegatt.DeviceScanActivity;

public class MainActivity extends AppCompatActivity implements
        LightFragment.OnFragmentInteractionListener,
        SoundFragment.OnFragmentInteractionListener,
        MovingFragment.OnFragmentInteractionListener {

    private static final int REQUEST_ENABLE_BT = 2017 ;
    private BluetoothHandler bluetoothHandler =  null;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.idFABbleutooth);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Start Bluetooth settings menu...", Toast.LENGTH_SHORT).show();
//                setupBluetoothWhenNeeded();
//
//            }
//        });
        Toast.makeText(this, "Have fun using this app!", Toast.LENGTH_SHORT).show();
    }

    public void setupBluetoothWhenNeeded() {
        if (bluetoothHandler == null) {
            bluetoothHandler = new BluetoothHandler(this);
            if (!bluetoothHandler.initialize()) {
                try {
                    this.wait(10000);
                    Toast.makeText(this, "Your mobile device does not support Bluetooth, this app will not start.", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.exit(0);
                }


            }
            if (!bluetoothHandler.isBluetoothEnabled()) {
                // Ensures Bluetooth is available on the device and it is enabled. If not,
                // displays a dialog requesting user permission to enable Bluetooth.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            bluetoothHandler.findBLEDevices();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_refresh) {
            return true;
        }
        Intent intent;
        switch (id) {
            case R.id.menu_scan:
                intent = new Intent(this, DeviceScanActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_stop:
                intent = new Intent(this, DeviceScanActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: from LightFragment
        // TODO: from SoundFragment
        // TODO: from MovingFragment
        Toast.makeText(this, "TODO: From Light,Sound or Moving Fragment: ", Toast.LENGTH_SHORT).show();
        System.out.println("Is this ever printed?");
    }

     /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                default:
                case 0:
                    return LightFragment.newInstance(LightFragment.ARG_PARAM1, LightFragment.ARG_PARAM2);
                case 1:
                    return SoundFragment.newInstance(SoundFragment.ARG_PARAM1, SoundFragment.ARG_PARAM2);
                case 2:
                    return MovingFragment.newInstance(MovingFragment.ARG_PARAM1, MovingFragment.ARG_PARAM2);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Light";
                case 1:
                    return "Sound";
                case 2:
                    return "Movement";
            }
            return null;
        }
    }
}
