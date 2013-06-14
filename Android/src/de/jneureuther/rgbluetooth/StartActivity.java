package de.jneureuther.rgbluetooth;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity{

	private static final int REQUEST_ENABLE_BT = 1;
	ListView listView;
	protected Object mActionMode;
	public int selectedItem = -1;
	ArrayAdapter<String> adapter;
	ArrayList<String> lst;
    private static final int EXTRA_DEVICE_ADDRESS = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
    {		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView) findViewById(R.id.listView2);
        String[] values = new String[] { "MoodLamp", "AmbiLight", "Stehlampe" };
        lst = new ArrayList<String>();
        lst.addAll(Arrays.asList(values));
        adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, lst);
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
        
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                int position, long id) {

              if (mActionMode != null) {
                return false;
              }
              selectedItem = position;

              // Start the CAB using the ActionMode.Callback defined above
              mActionMode = StartActivity.this
                  .startActionMode(mActionModeCallback);
              view.setSelected(true);
              return true;
            }
          });
    
    }
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

	    // Called when the action mode is created; startActionMode() was called
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	      // Inflate a menu resource providing context menu items
	      MenuInflater inflater = mode.getMenuInflater();
	      // Assumes that you have "contexual.xml" menu resources
	      inflater.inflate(R.menu.actionbar_delete, menu);
	      return true;
	    }

	    // Called each time the action mode is shown. Always called after
	    // onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	      return false; // Return false if nothing is done
	    }

	    // Called when the user selects a contextual menu item
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	      switch (item.getItemId()) {
	      case R.id.menu_delete:
	        adapter.remove(adapter.getItem(selectedItem));
	        adapter.notifyDataSetChanged();
	        // Action picked, so close the CAB
	        mode.finish();
	        return true;
	      default:
	        return false;
	      }
	    }

	    // Called when the user exits the action mode
	    public void onDestroyActionMode(ActionMode mode) {
	      mActionMode = null;
	      selectedItem = -1;
	    }

	  };
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_add, menu);
        return true;
      }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
			case R.id.menu_add:
				Intent intent = new Intent (this, DeviceListActivity.class);
				startActivityForResult(intent, EXTRA_DEVICE_ADDRESS);
			return true;
    	}
    return false;
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
    	  
    	  if (resultCode == Activity.RESULT_OK){
    		  String btname = data.getExtras()
    		 			       		.getString(DeviceListActivity.EXTRA_DEVICE_NAME);
    		  adapter.add(btname);
    		  adapter.notifyDataSetChanged();
    	  }
    	  Log.d("Haha", "");
    	  
    }
    	  
    public void checkBt(){
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
        }
    
}
	
          