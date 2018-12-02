package com.dhruva.ungeelbill;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class HelpActivity extends Activity {


	private TextView textview2;
	private EditText itemname;
	private EditText cost_max;
	private SharedPreferences data;
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.help);
		initialize();
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
			|| checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
				requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
			}
			else {
				initializeLogic();
			}
		}
		else {
			initializeLogic();
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize() {
		textview2 = findViewById(R.id.textview2);
		itemname = findViewById(R.id.itemname);
		cost_max = findViewById(R.id.cost_max);
		Button button1 = findViewById(R.id.button1);
		Button changeMax =  findViewById(R.id.changemax);
        data = getSharedPreferences("data", Activity.MODE_PRIVATE);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				String itemName=itemname.getText().toString();
				String costMax=cost_max.getText().toString();
				if (!costMax.isEmpty()&& !itemName.isEmpty()){
                    FileUtil.writeFile(MainActivity.itemsFilePath, FileUtil.readFile(MainActivity.itemsFilePath)+itemName+":"+costMax+"\n");
                    SketchwareUtil.showMessage(getApplicationContext(), itemName+" "+costMax+"Item added");
                }
				else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter Item Name and Cost");
                }
			}
		});
		
		changeMax.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
                String Max=cost_max.getText().toString();
                if (!Max.isEmpty()){
                    data.edit().putInt("max", Integer.parseInt(cost_max.getText().toString())).apply();
                    SketchwareUtil.showMessage(getApplicationContext(), "Max Changed");
                }
                else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Enter Max Value");
                }
			}
		});
	}
	private void initializeLogic() {
		textview2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/indieflower.ttf"), Typeface.BOLD);
		textview2.setText("This is an app to help keep track of expenses that people incur.\nJust choose any entry in the dropdown or type in a name and an amount. Contact me to add further entries to dropdown.\nButtons help set the amount faster than typing. Use clear to clear the entry.\nUse add to take down an entry in a file and also add to the \"TOTAL\". The reset button resets the Total to 0, but DOES NOT remove entries from the file. To actually remove an entry from the file, add an entry with same name but negative amount. Further analysis of entries can be done using Python(will be provided) or any other language. File will be present in Internal Memory/RuPay. Files are named according to month and new files are created every month.\n\nContact me (ms18163@iisermohali.ac.in) for further queries.");
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
