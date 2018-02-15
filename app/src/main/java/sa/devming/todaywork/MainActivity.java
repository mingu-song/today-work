package sa.devming.todaywork;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText workSiteET;
    private EditText equipNameRow1, equipNameRow2, equipNameRow3, equipNameRow4, equipNameRow5;
    private EditText equipDescRow1, equipDescRow2, equipDescRow3, equipDescRow4, equipDescRow5;
    private EditText equipCntRow1, equipCntRow2, equipCntRow3, equipCntRow4, equipCntRow5;
    private EditText workerNameRow1, workerNameRow2, workerNameRow3, workerNameRow4, workerNameRow5, workerNameRow6;
    private EditText workerDescRow1, workerDescRow2, workerDescRow3, workerDescRow4, workerDescRow5, workerDescRow6;
    private EditText workerCntRow1, workerCntRow2, workerCntRow3, workerCntRow4, workerCntRow5, workerCntRow6;
    private TextView equipCntSum, workerCntSum;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workSiteET = (EditText)findViewById(R.id.workSiteET);

        //equipment
        equipNameRow1 = (EditText)findViewById(R.id.equipNameRow1);
        equipNameRow2 = (EditText)findViewById(R.id.equipNameRow2);
        equipNameRow3 = (EditText)findViewById(R.id.equipNameRow3);
        equipNameRow4 = (EditText)findViewById(R.id.equipNameRow4);
        equipNameRow5 = (EditText)findViewById(R.id.equipNameRow5);

        equipDescRow1 = (EditText)findViewById(R.id.equipDescRow1);
        equipDescRow2 = (EditText)findViewById(R.id.equipDescRow2);
        equipDescRow3 = (EditText)findViewById(R.id.equipDescRow3);
        equipDescRow4 = (EditText)findViewById(R.id.equipDescRow4);
        equipDescRow5 = (EditText)findViewById(R.id.equipDescRow5);

        equipCntRow1 = (EditText)findViewById(R.id.equipCntRow1);
        equipCntRow2 = (EditText)findViewById(R.id.equipCntRow2);
        equipCntRow3 = (EditText)findViewById(R.id.equipCntRow3);
        equipCntRow4 = (EditText)findViewById(R.id.equipCntRow4);
        equipCntRow5 = (EditText)findViewById(R.id.equipCntRow5);
        equipCntSum = (TextView)findViewById(R.id.equipCntSum);

        //worker
        workerNameRow1 = (EditText)findViewById(R.id.workerNameRow1);
        workerNameRow2 = (EditText)findViewById(R.id.workerNameRow2);
        workerNameRow3 = (EditText)findViewById(R.id.workerNameRow3);
        workerNameRow4 = (EditText)findViewById(R.id.workerNameRow4);
        workerNameRow5 = (EditText)findViewById(R.id.workerNameRow5);
        workerNameRow6 = (EditText)findViewById(R.id.workerNameRow6);

        workerDescRow1 = (EditText)findViewById(R.id.workerDescRow1);
        workerDescRow2 = (EditText)findViewById(R.id.workerDescRow2);
        workerDescRow3 = (EditText)findViewById(R.id.workerDescRow3);
        workerDescRow4 = (EditText)findViewById(R.id.workerDescRow4);
        workerDescRow5 = (EditText)findViewById(R.id.workerDescRow5);
        workerDescRow6 = (EditText)findViewById(R.id.workerDescRow6);

        workerCntRow1 = (EditText)findViewById(R.id.workerCntRow1);
        workerCntRow2 = (EditText)findViewById(R.id.workerCntRow2);
        workerCntRow3 = (EditText)findViewById(R.id.workerCntRow3);
        workerCntRow4 = (EditText)findViewById(R.id.workerCntRow4);
        workerCntRow5 = (EditText)findViewById(R.id.workerCntRow5);
        workerCntRow6 = (EditText)findViewById(R.id.workerCntRow6);
        workerCntSum = (TextView)findViewById(R.id.workerCntSum);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        if (Build.VERSION.SDK_INT >= 23) {
            checkAllowPermissions();
        }
        adMob();
        sumWorkCount();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onClickShareBT(View v){
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        takeScreenshot();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("todayWork", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("workSiteET", workSiteET.getText().toString());

        editor.putString("equipNameRow1", equipNameRow1.getText().toString());
        editor.putString("equipNameRow2", equipNameRow2.getText().toString());
        editor.putString("equipNameRow3", equipNameRow3.getText().toString());
        editor.putString("equipNameRow4", equipNameRow4.getText().toString());
        editor.putString("equipNameRow5", equipNameRow5.getText().toString());
        editor.putString("equipDescRow1", equipDescRow1.getText().toString());
        editor.putString("equipDescRow2", equipDescRow2.getText().toString());
        editor.putString("equipDescRow3", equipDescRow3.getText().toString());
        editor.putString("equipDescRow4", equipDescRow4.getText().toString());
        editor.putString("equipDescRow5", equipDescRow5.getText().toString());
        editor.putString("equipCntRow1", equipCntRow1.getText().toString());
        editor.putString("equipCntRow2", equipCntRow2.getText().toString());
        editor.putString("equipCntRow3", equipCntRow3.getText().toString());
        editor.putString("equipCntRow4", equipCntRow4.getText().toString());
        editor.putString("equipCntRow5", equipCntRow5.getText().toString());

        editor.putString("workerNameRow1", workerNameRow1.getText().toString());
        editor.putString("workerNameRow2", workerNameRow2.getText().toString());
        editor.putString("workerNameRow3", workerNameRow3.getText().toString());
        editor.putString("workerNameRow4", workerNameRow4.getText().toString());
        editor.putString("workerNameRow5", workerNameRow5.getText().toString());
        editor.putString("workerNameRow6", workerNameRow6.getText().toString());
        editor.putString("workerDescRow1", workerDescRow1.getText().toString());
        editor.putString("workerDescRow2", workerDescRow2.getText().toString());
        editor.putString("workerDescRow3", workerDescRow3.getText().toString());
        editor.putString("workerDescRow4", workerDescRow4.getText().toString());
        editor.putString("workerDescRow5", workerDescRow5.getText().toString());
        editor.putString("workerDescRow6", workerDescRow6.getText().toString());
        editor.putString("workerCntRow1", workerCntRow1.getText().toString());
        editor.putString("workerCntRow2", workerCntRow2.getText().toString());
        editor.putString("workerCntRow3", workerCntRow3.getText().toString());
        editor.putString("workerCntRow4", workerCntRow4.getText().toString());
        editor.putString("workerCntRow5", workerCntRow5.getText().toString());
        editor.putString("workerCntRow6", workerCntRow6.getText().toString());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("todayWork", Activity.MODE_PRIVATE);

        workSiteET.setText(preferences.getString("workSiteET",""));

        equipNameRow1.setText(preferences.getString("equipNameRow1",""));
        equipNameRow2.setText(preferences.getString("equipNameRow2",""));
        equipNameRow3.setText(preferences.getString("equipNameRow3",""));
        equipNameRow4.setText(preferences.getString("equipNameRow4",""));
        equipNameRow5.setText(preferences.getString("equipNameRow5",""));
        equipDescRow1.setText(preferences.getString("equipDescRow1",""));
        equipDescRow2.setText(preferences.getString("equipDescRow2",""));
        equipDescRow3.setText(preferences.getString("equipDescRow3",""));
        equipDescRow4.setText(preferences.getString("equipDescRow4",""));
        equipDescRow5.setText(preferences.getString("equipDescRow5",""));
        equipCntRow1.setText(preferences.getString("equipCntRow1",""));
        equipCntRow2.setText(preferences.getString("equipCntRow2",""));
        equipCntRow3.setText(preferences.getString("equipCntRow3",""));
        equipCntRow4.setText(preferences.getString("equipCntRow4",""));
        equipCntRow5.setText(preferences.getString("equipCntRow5",""));

        workerNameRow1.setText(preferences.getString("workerNameRow1",""));
        workerNameRow2.setText(preferences.getString("workerNameRow2",""));
        workerNameRow3.setText(preferences.getString("workerNameRow3",""));
        workerNameRow4.setText(preferences.getString("workerNameRow4",""));
        workerNameRow5.setText(preferences.getString("workerNameRow5",""));
        workerNameRow6.setText(preferences.getString("workerNameRow6",""));
        workerDescRow1.setText(preferences.getString("workerDescRow1",""));
        workerDescRow2.setText(preferences.getString("workerDescRow2",""));
        workerDescRow3.setText(preferences.getString("workerDescRow3",""));
        workerDescRow4.setText(preferences.getString("workerDescRow4",""));
        workerDescRow5.setText(preferences.getString("workerDescRow5",""));
        workerDescRow6.setText(preferences.getString("workerDescRow6",""));
        workerCntRow1.setText(preferences.getString("workerCntRow1",""));
        workerCntRow2.setText(preferences.getString("workerCntRow2",""));
        workerCntRow3.setText(preferences.getString("workerCntRow3",""));
        workerCntRow4.setText(preferences.getString("workerCntRow4",""));
        workerCntRow5.setText(preferences.getString("workerCntRow5",""));
        workerCntRow6.setText(preferences.getString("workerCntRow6",""));
    }

    private void takeScreenshot(){
        try{
            View root = getWindow().getDecorView().getRootView();
            root.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
            root.setDrawingCacheEnabled(false);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk:mm:ss", Locale.KOREA);
            String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "todayWork";
            File dir = new File(mPath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File imageFile = new File(mPath, sdf.format(new Date()) + ".jpg");
            FileOutputStream output = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();

            shareImage(imageFile);
        } catch (Throwable e){
            Toast.makeText(this,getResources().getText(R.string.need_permissions),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void shareImage(File imageFile){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));

        Intent chooser = Intent.createChooser(intent, "");
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(chooser);
    }

    private void checkAllowPermissions() {
        String [] permissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0 ; i < permissions.length ; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED){
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (requestCode == 1){
            for (int i = 0 ; i < permissions.length ; i++){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한 승인", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + "권한 승인되지 않음", Toast.LENGTH_LONG).show();
                }
            }
        }*/
    }

    private void adMob(){
        AdView mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void sumWorkCount(){
        equipCntRow1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
        });

        equipCntRow2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
        });

        equipCntRow3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
        });

        equipCntRow4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
        });

        equipCntRow5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    equipCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });

        workerCntRow6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                    workerCntSum.setText(String.valueOf(sum));
                }
            }
        });
    }
}
