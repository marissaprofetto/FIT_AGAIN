package com.example.fitfurlife;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 2;
    private BLEController bleController;
    private static final int REQUEST_PERMISSIONS = 101;
    private ActivityResultLauncher<Intent> enableBtResultLauncher;



    private void requestPermissionsIfNeeded() {
        List<String> requiredPermissions = new ArrayList<>();
        requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_SCAN);
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }

        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS);
        } else {
            // All permissions are granted, proceed with initializing BLE
            initBLE();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate started.");
        // Initialize the ActivityResultLauncher
        enableBtResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Bluetooth has been enabled, initialize BLE operation.
                        bleController.init();
                    } else {
                        // Handle the case where the user declined to enable Bluetooth
                    }
                });

        requestPermissionsIfNeeded();
        databaseHelper dbHelper = new databaseHelper(this);
        if (dbHelper.getAllDogProfiles().isEmpty()) {
            // No profiles exist, show profile creation fragment
            loadCreationFragment();
        } else {
            // Profiles exist, show profile selection fragment
            loadSelectionFragment();
        }
    }

    private void loadCreationFragment() {
        Log.d("MainActivity", "Loading fragment: Creation");
        profileCreationFrag addProfileFragment = new profileCreationFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, addProfileFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    private void loadSelectionFragment() {
        Log.d("MainActivity", "Loading fragment: Creation");
        profileSelectionFrag addProfileFragment = new profileSelectionFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragmentContainer, addProfileFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }



    private void initBLE() {
        // Initialize BLEController
        bleController = BLEController.getInstance(this);

        // Check if Bluetooth is enabled
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtResultLauncher.launch(enableBtIntent);
        } else {
            // Bluetooth is enabled, initialize BLE operation
            bleController.init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, initialize BLE.
                    initBLE();
                } else {
                    // Permission denied, show a message to the user.
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            // Bluetooth has been enabled, initialize BLE operation.
            bleController.init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleController.disconnect(); // Ensure resources are released properly
    }

}
