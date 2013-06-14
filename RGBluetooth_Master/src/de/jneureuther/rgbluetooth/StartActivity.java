package de.jneureuther.rgbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StartActivity extends Activity{

	private static final int REQUEST_ENABLE_BT = 1;
	ListView listView;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView) findViewById(R.id.listView1);
        String[] values = new String[] { "MoodLamp", "AmbiLight", "Stehlampe" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(StartActivity.this, SendActivity.class);
				intent.putExtra("name", listView.getAdapter().getItem(arg2).toString());
                startActivity(intent);
			}
        	
        });
    
    }
	
	public void onStart(){
		super.onStart();
		checkBt();
	}
	
	public void onStop(){
		super.onStop();
		stopService(new Intent(this, BtService.class));
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	  // TODO Auto-generated method stub
    	  if(requestCode == REQUEST_ENABLE_BT){
    		  checkBt();
    	  }
    	  
    }
    	  
    public void checkBt(){
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
        }
    
}
	
          