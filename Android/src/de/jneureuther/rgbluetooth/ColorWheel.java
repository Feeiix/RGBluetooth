package de.jneureuther.rgbluetooth;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ColorWheel extends Fragment {
	 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.colorwheel, container, false);
        SeekBar bar1 = (SeekBar)view.findViewById(R.id.brightnessbar);
        ColorCircle mColorCircle = (ColorCircle)view.findViewById(R.id.colorcircle);
        mColorCircle.setColor(SendActivity.color);
        bar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        
        public void onProgressChanged(SeekBar seekBar, int progress,
        		boolean fromUser) {
        	switch (seekBar.getId()){
        	
        		case R.id.brightnessbar:
        		
        			float helper = progress;
        			SendActivity.value = helper/100;
        			((SendActivity) getActivity()).sendRGB();
        			
        		break;
        		
        	}
        }

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}});
        
        mColorCircle.setOnColorChangedListener(new OnColorChangedListener(){
            public void onColorChanged(View view, int newColor) {
            	SendActivity.color = newColor;
            	((SendActivity) getActivity()).sendHSV();
            }

			@Override
			public void onColorPicked(View view, int newColor) {
				// TODO Auto-generated method stub
				
			}
        }
        );

        return view;
    }
    
}
 
