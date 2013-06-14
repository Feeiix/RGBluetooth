package de.jneureuther.rgbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import de.jneureuther.rgbluetooth.BtService.LocalBinder;
import android.animation.AnimatorSet.Builder;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SendActivity extends Activity{

	public static Context appContext;
    public static ProgressDialog progressDialog;
    int fspeed = 1;
    static int color;
    float[] hsv;
    static float value = 1;
    boolean mBounded;
    BtService mService;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);

        Intent intent = getIntent();
        
       //ActionBar gets initiated
        ActionBar actionbar = getActionBar();
      //Adding the "NavigatingUp" Button
        actionbar.setSubtitle(intent.getStringExtra("name"));
        actionbar.setDisplayHomeAsUpEnabled(true);
      //Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      //initiating both tabs and set text to it.
        ActionBar.Tab ColorWheelTab = actionbar.newTab().setText("ColorWheel");
        ActionBar.Tab AnimationsTab = actionbar.newTab().setText("Animations");
 
     //create the two fragments we want to use for display content
        Fragment ColorWheelFragment = new ColorWheel();
        Fragment AnimationsFragment = new Animations();
 
    //set the Tab listener. Now we can listen for clicks.
        ColorWheelTab.setTabListener(new MyTabsListener(ColorWheelFragment));
        AnimationsTab.setTabListener(new MyTabsListener(AnimationsFragment));
 
   //add the two tabs to the actionbar
        actionbar.addTab(ColorWheelTab);
        actionbar.addTab(AnimationsTab);
        
        color = getPreferences("Color");
        hsv = new float[3];
        value = 1;
        
		startService(new Intent(this, BtService.class));
  	  	Intent mIntent = new Intent(this, BtService.class);
  	  	bindService(mIntent, mConnection, BIND_AUTO_CREATE);
  	  	mBounded = true;
		
		}  
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_settings, menu);
        return true;
      }
    
    public void onStart(){
    	super.onStart();
    }
    
    public void onResume(){
    	super.onResume();
    }
    
    public void onStop(){
    	super.onStop();
    	if (!this.isFinishing()){
    	Toast.makeText(SendActivity.this, "Service is running in background!", Toast.LENGTH_LONG).show();
    	}
    	editPreferences();
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	if(mBounded) {
		   unbindService(mConnection);
		   mBounded = false;
    	}
    	stopService(new Intent(this, BtService.class));
    	editPreferences();
    }
    
    public void editPreferences(){
  	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(SendActivity.this);
  	    SharedPreferences.Editor editor = app_preferences.edit();
    	editor.putInt("Color", color);
        editor.commit();
    }
    
    public int getPreferences(String key){
  	    SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(SendActivity.this);
  	    SharedPreferences.Editor editor = app_preferences.edit();
  	    return app_preferences.getInt("key", Color.BLACK);
    }

    
    public void onColorPicked(View view, int newColor) {

    }
    
    public void sendRGB(){
    	
    	Color.colorToHSV(color, hsv);
    	hsv[2] = value;
		int sendcolor = Color.HSVToColor(hsv);
		String rgb = "C" + String.valueOf(Color.red(sendcolor)) + "," + String.valueOf(Color.green(sendcolor)) + "," + String.valueOf(Color.blue(sendcolor));
		try {
			mService.sendData(rgb);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendHSV(){
    	
    	Color.colorToHSV(color, hsv);
    	hsv[2] = value;
    	String send = "C" + Float.toString(hsv[0]) + "," + Float.toString(hsv[1]) + "," + Float.toString(hsv[2]);
    	Log.d("HSV", send);
		try {
			mService.sendData(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendEffect(String send){
		try {
			mService.sendData(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


	
	ServiceConnection mConnection = new ServiceConnection() {
		  
		  public void onServiceDisconnected(ComponentName name) {
		   Toast.makeText(SendActivity.this, "Service is disconnected", 1000).show();
		   mBounded = false;
		   mService = null;
		  }
		  
		  public void onServiceConnected(ComponentName name, IBinder service) {
		   Toast.makeText(SendActivity.this, "Service is connected", 1000).show();
		   mBounded = true;
		   LocalBinder mLocalBinder = (LocalBinder)service;
		   mService = mLocalBinder.getServerInstance();
		  }

		 };

    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
			case android.R.id.home:
				finish();
			return true;
    	}
    return false;
    }

    class MyTabsListener implements ActionBar.TabListener{
    	public Fragment fragment;
 
    	public MyTabsListener(Fragment fragment) {
    		this.fragment = fragment;
    	}
 
    	public void onTabReselected(Tab tab, FragmentTransaction ft) {
    	}
 
    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
    		ft.replace(R.id.fragment_container, fragment);
    	}
 
    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    		ft.remove(fragment);
    	}
 
    }
    
}