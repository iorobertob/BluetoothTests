package com.example.io.mymap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ListView listView ;

    BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void clickBluetooth(View view) {
        //Toast.makeText(this, "Cancel pressed", Toast.LENGTH_LONG).show();

        listView = (ListView) findViewById(R.id.list);

        if(bluetooth == null)
        {
            // Continue with bluetooth setup.
            Toast.makeText(this, "Null Bluetooth Device ", Toast.LENGTH_LONG).show();
        }

        String status = null;
        if (bluetooth.isEnabled()) {
            // Enabled. Work with Bluetooth.
            String mydeviceaddress = bluetooth.getAddress();
            String mydevicename = bluetooth.getName();
            status = mydevicename + " : " + mydeviceaddress;

            Toast.makeText(this, status, Toast.LENGTH_LONG).show();

            Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
            List<String> mArrayAdapter = new ArrayList<String>();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    //Toast.makeText(this, mArrayAdapter, Toast.LENGTH_LONG).show();
                }
            }

            // Create an Array Adapter to Add Devices to
            ArrayAdapter<String> itemsAdapter;
            itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, mArrayAdapter);

            //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.textView4, mArrayAdapter);
            listView.setAdapter(itemsAdapter);

        }
        else
        {
            // Disabled. Do something else.
            Toast.makeText(this, "Bluetooth Disabled ", Toast.LENGTH_LONG).show();

        }

        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }

    List<String> mArrayAdapter = new ArrayList<String>();
    public void scanBluetooth(View view){


        bluetooth.startDiscovery();

        BroadcastReceiver mReceiver1 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                List<String> mArrayAdapter1 = new ArrayList<String>();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter1.add(device.getName() + "\n" + device.getAddress());
                    mArrayAdapter = mArrayAdapter1;
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver1, filter);

        // Create an Array Adapter to Add Devices to

        ArrayAdapter<String> itemsAdapter;
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, mArrayAdapter);

        //itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.textView4, mArrayAdapter);
        listView.setAdapter(itemsAdapter);

    }



}
