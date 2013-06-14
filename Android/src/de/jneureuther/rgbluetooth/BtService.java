package de.jneureuther.rgbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BtService extends Service {
	
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    boolean connected = false;
    private static final String TAG = "MyService";
    IBinder mBinder = new LocalBinder();
@Override
    public IBinder onBind(Intent intent) {
        return mBinder;
        }

	public class LocalBinder extends Binder {
	  public BtService getServerInstance() {
	   return BtService.this;
	  }
	 }
    @Override
    public void onCreate() {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
        try 
        {
            findBT();
            openBT();
        }
        catch (IOException ex) { }
        }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        if (connected){
        try {
			closeBT();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
        }
    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        }
    
    void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
        }
        
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("Serial Adaptor")) 
                {
                    mmDevice = device;
                    break;
                }
            }
        }
    }
    
    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        
        
        connected = true;
    }
    
    void sendData(String msg) throws IOException
    {
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        //myLabel.setText("Data Sent");
        Log.d ("RGB", msg);
    }
    
    void closeBT() throws IOException
    {
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();        
        connected = false;
    }
 
}