package com.grupo4.encuesta

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast


class BlueActivity : AppCompatActivity() {
    val TAG: String = "BlueActivity"

    lateinit var shareButton: Button
    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var blueToothEnablerIntent: Intent
    val requestCodeForEnable = 1
    lateinit var arrayAdapter: ArrayAdapter<String>
    var availDevices = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blue)

        shareButton = findViewById(R.id.shareButton)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        blueToothEnablerIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

        blueToothShare()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == requestCodeForEnable) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(applicationContext, "BlueTooth a sido activado.", Toast.LENGTH_LONG).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext, "El evento fue cancelado.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun blueToothShare() {
        shareButton.setOnClickListener {

            turnBlueToothOn()
            //searchForDevices() // problema
            discoverable()
            turnBlueToothOff()
        }
    }


    private fun discoverable() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)

        val intentFilter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(receiver2, intentFilter)
    }

    private fun turnBlueToothOff() {
        if (mBluetoothAdapter.isEnabled) {
            mBluetoothAdapter.disable()
        }
    }

    private fun searchForDevices() {
        mBluetoothAdapter.startDiscovery()
        val intentFilter: IntentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver1, intentFilter)
        arrayAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, availDevices)


        val intent: Intent = Intent(this, AvailableDevices::class.java)
        startActivity(intent)
    }

    private fun turnBlueToothOn() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(applicationContext, "Este dispositivo no tiene bluetooth.", Toast.LENGTH_LONG).show()
        } else {
            if (!mBluetoothAdapter.isEnabled) {
                startActivityForResult(blueToothEnablerIntent, requestCodeForEnable)
            }
        }
    }

    val receiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var action: String = intent.action

            // Find Devices
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                var device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                availDevices.add(device.name)   // add name to list
                arrayAdapter.notifyDataSetChanged()
            }

        }
    }

    val receiver2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            var action: String = intent.action

            // Find Devices
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                val mode: Int = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)

                when (mode) {
                    // is in discoverable mode
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.")
                    // not in discoerable mode
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE -> Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.")
                    BluetoothAdapter.SCAN_MODE_NONE -> Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.")
                    BluetoothAdapter.STATE_CONNECTING -> Log.d(TAG, "mBroadcastReceiver2: Connecting....")
                    BluetoothAdapter.STATE_CONNECTED -> Log.d(TAG, "mBroadcastReceiver2: Connected.")
                }


                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                availDevices.add(device.name)   // add name to list
                arrayAdapter.notifyDataSetChanged()
            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver1)
        unregisterReceiver(receiver2)
    }

}


