package nl.ordina.kijkdoos.search;

import android.bluetooth.BluetoothDevice;

import org.junit.Before;
import org.junit.Test;

import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.search.SearchViewBoxActivity;
import nl.ordina.kijkdoos.search.ViewBoxListAdapter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by coenhoutman on 23/02/2017.
 */
public class ViewBoxListAdapterTest {

    private ViewBoxListAdapter viewBoxListAdapter;

    @Before
    public void setUpViewBoxListAdapter() throws Exception {
        final SearchViewBoxActivity mockedSearchViewBoxActivity = mock(SearchViewBoxActivity.class);
        viewBoxListAdapter = new ViewBoxListAdapter(mockedSearchViewBoxActivity);
        viewBoxListAdapter = spy(viewBoxListAdapter);

        doNothing().when(viewBoxListAdapter).notifyDataSetChanged();
    }

    @Test
    public void addViewBoxRemoteController() throws Exception {
        final BluetoothDevice mockedBluetoothDevice = mock(BluetoothDevice.class);
        final ViewBoxRemoteController viewBoxRemoteController = new ViewBoxRemoteController(mockedBluetoothDevice);
        final BluetoothDevice mockedBluetoothDevice2 = mock(BluetoothDevice.class);
        final ViewBoxRemoteController viewBoxRemoteController2 = new ViewBoxRemoteController(mockedBluetoothDevice2);

        viewBoxListAdapter.addViewBoxRemoteController(viewBoxRemoteController);
        viewBoxListAdapter.addViewBoxRemoteController(viewBoxRemoteController2);

        assertEquals(2, viewBoxListAdapter.getCount());
    }

    @Test
    public void addViewBoxRemoteControllerIgnoreDuplicates() throws Exception {
        final BluetoothDevice mockedBluetoothDevice = mock(BluetoothDevice.class);
        final ViewBoxRemoteController viewBoxRemoteController = new ViewBoxRemoteController(mockedBluetoothDevice);

        viewBoxListAdapter.addViewBoxRemoteController(viewBoxRemoteController);
        viewBoxListAdapter.addViewBoxRemoteController(viewBoxRemoteController);

        assertEquals(1, viewBoxListAdapter.getCount());
    }

    @Test
    public void addViewBoxRemoteControllerIgnoreNull() throws Exception {
        viewBoxListAdapter.addViewBoxRemoteController(null);
        assertEquals(0, viewBoxListAdapter.getCount());
    }
}