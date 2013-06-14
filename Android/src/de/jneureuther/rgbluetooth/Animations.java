package de.jneureuther.rgbluetooth;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemClickListener;

public class Animations extends Fragment {
	
	ListView listView;
	String effect;
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.animations, container, false);
    
        listView = (ListView) view.findViewById(R.id.listView2);
        final SeekBar bar2 = (SeekBar)view.findViewById(R.id.speedbar);
        
        String[] values = new String[] { "RGB Fade", "Pulse"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            R.layout.animations_listview, values);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (listView.getAdapter().getItem(arg2).toString() == "RGB Fade"){
					((SendActivity) getActivity()).sendEffect("P" + String.valueOf(100 - bar2.getProgress()));
				}
				if (listView.getAdapter().getItem(arg2).toString() == "Pulse"){
					((SendActivity) getActivity()).sendEffect("F" + String.valueOf(100 - bar2.getProgress()));
				}
			}
        	
        });
    	
        return view;
    }
 
}
