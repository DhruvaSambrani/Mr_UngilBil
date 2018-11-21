package com.dhruva.ungeelbill;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Activity {


    private int toAdd;
    static public String entriesFilePath;
    static public String itemsFilePath;
    private HashMap<String, Object> items = new HashMap<>();
    private ArrayList<String> spinner = new ArrayList<>();
    private Spinner spinner1;
    private EditText name;
    private EditText amount;
    private TextView fileList;
    private TextView monthTotal;
    private ProgressBar progressbar1;
    private TextView monthMax;
    private TextView total;
    private SharedPreferences data;
    private Intent i = new Intent();
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            } else {
                initializeLogic();
            }
        } else {
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
        spinner1 = findViewById(R.id.spinner1);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);
        Button add = findViewById(R.id.add);
        Button reset = findViewById(R.id.reset);
        fileList = findViewById(R.id.filelist);
        monthTotal = findViewById(R.id.month_total);
        progressbar1 = findViewById(R.id.progressbar1);
        monthMax = findViewById(R.id.monthmax);
        total = findViewById(R.id.total);
        ImageView imageview1 = findViewById(R.id.imageview1);
        data = getSharedPreferences("data", Activity.MODE_PRIVATE);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
                name.setText(spinner.get(_param3));
                amount.setText(items.get(spinner.get(_param3)).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (!name.getText().toString().isEmpty() && !amount.getText().toString().isEmpty()) {
                    toAdd = Integer.parseInt(amount.getText().toString());
                    FileUtil.writeFile(entriesFilePath, FileUtil.readFile(entriesFilePath).concat("\n".concat(name.getText().toString().concat(":".concat(amount.getText().toString())))));
                    data.edit().putInt("sum", toAdd + data.getInt("sum", 0)).apply();
                    _rewrite();
                } else {
                    SketchwareUtil.showMessage(getApplicationContext(), "Please Enter a Name and Amount");
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                data.edit().putInt("sum", 0).apply();
                _rewrite();
            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i.setAction(Intent.ACTION_VIEW);
                i.setClass(getApplicationContext(), HelpActivity.class);
                startActivity(i);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void initializeLogic() {
        Calendar cal = Calendar.getInstance();
        entriesFilePath = FileUtil.getExternalStorageDir()+"/"+getResources().getString(R.string.AppName)+"/"+(new SimpleDateFormat("yyyyMM").format(cal.getTime())+".txt");
        itemsFilePath = FileUtil.getExternalStorageDir()+"/"+getResources().getString(R.string.AppName)+"/items.txt";
        //entriesFilePath = FileUtil.getExternalStorageDir()+"/"+"RuPay"+"/"+(new SimpleDateFormat("yyyyMM").format(cal.getTime())+".txt");
        //itemsFilePath = FileUtil.getExternalStorageDir()+"/"+"RuPay"+"/items.txt";
        _initialiseSpinner();
        _rewrite();
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        SketchwareUtil.showMessage(getApplicationContext(), "Thanks for Using");
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        _rewrite();
    }

    private void _initialiseSpinner() {
        items = new HashMap<>();
        items.put("", "");
        String[] temp = FileUtil.readFile(itemsFilePath).split("\n");
        for (String a : temp) {
            a=a.trim();
            String[] b = a.split(":");
            if (b.length == 2) {
                items.put(b[0], b[1]);
            }
        }
        SketchwareUtil.getAllKeysFromMap(items, spinner);
        spinner1.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, spinner));
    }

    private void _findMonth() {
        int sumMonth = 0;
        String strings = FileUtil.readFile(entriesFilePath);
        String[] temp = strings.split("\n");
        for (String i : temp) {
            if (!i.isEmpty()) {
                sumMonth += Integer.parseInt(i.split(":")[1]);
            }
        }
        data.edit().putInt("month", sumMonth).apply();
    }

    private void _rewrite() {
        _initialiseSpinner();
        _findMonth();
        fileList.setText(FileUtil.readFile(entriesFilePath));
        monthTotal.setText(String.valueOf(data.getInt("month", 0)));
        monthMax.setText(String.valueOf(data.getInt("max", 5000)));
        progressbar1.setMax(data.getInt("max", 5000));
        progressbar1.setProgress(data.getInt("month", 0));
        total.setText(String.valueOf(data.getInt("sum", 0)));
        if (data.getInt("max", 5000) < data.getInt("month", 0)) {
            progressbar1.setBackgroundColor(0xFFFF5722);
        } else {
            progressbar1.setBackgroundColor(Color.TRANSPARENT);
        }
        amount.setText("");
        name.setText("");
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
                _result.add((double) _arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

}
