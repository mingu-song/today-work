package sa.devming.todaywork;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AboutRowDialog.AboutRowDialogListener {
    private EditText workSiteET;
    private EditText equipNameRow1, equipNameRow2, equipNameRow3, equipNameRow4, equipNameRow5;
    private EditText equipDescRow1, equipDescRow2, equipDescRow3, equipDescRow4, equipDescRow5;
    private EditText equipCntRow1, equipCntRow2, equipCntRow3, equipCntRow4, equipCntRow5;
    private EditText workerNameRow1, workerNameRow2, workerNameRow3, workerNameRow4, workerNameRow5;
    private EditText workerDescRow1, workerDescRow2, workerDescRow3, workerDescRow4, workerDescRow5;
    private EditText workerCntRow1, workerCntRow2, workerCntRow3, workerCntRow4, workerCntRow5;
    private TextView equipCntSum, workerCntSum;
    private View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workSiteET = findViewById(R.id.workSiteET);

        //equipment
        equipNameRow1 = findViewById(R.id.equipNameRow1);
        equipNameRow2 = findViewById(R.id.equipNameRow2);
        equipNameRow3 = findViewById(R.id.equipNameRow3);
        equipNameRow4 = findViewById(R.id.equipNameRow4);
        equipNameRow5 = findViewById(R.id.equipNameRow5);

        equipDescRow1 = findViewById(R.id.equipDescRow1);
        equipDescRow2 = findViewById(R.id.equipDescRow2);
        equipDescRow3 = findViewById(R.id.equipDescRow3);
        equipDescRow4 = findViewById(R.id.equipDescRow4);
        equipDescRow5 = findViewById(R.id.equipDescRow5);

        equipCntRow1 = findViewById(R.id.equipCntRow1);
        equipCntRow2 = findViewById(R.id.equipCntRow2);
        equipCntRow3 = findViewById(R.id.equipCntRow3);
        equipCntRow4 = findViewById(R.id.equipCntRow4);
        equipCntRow5 = findViewById(R.id.equipCntRow5);

        equipCntSum = findViewById(R.id.equipCntSum);

        //worker
        workerNameRow1 = findViewById(R.id.workerNameRow1);
        workerNameRow2 = findViewById(R.id.workerNameRow2);
        workerNameRow3 = findViewById(R.id.workerNameRow3);
        workerNameRow4 = findViewById(R.id.workerNameRow4);
        workerNameRow5 = findViewById(R.id.workerNameRow5);

        workerDescRow1 = findViewById(R.id.workerDescRow1);
        workerDescRow2 = findViewById(R.id.workerDescRow2);
        workerDescRow3 = findViewById(R.id.workerDescRow3);
        workerDescRow4 = findViewById(R.id.workerDescRow4);
        workerDescRow5 = findViewById(R.id.workerDescRow5);

        workerCntRow1 = findViewById(R.id.workerCntRow1);
        workerCntRow2 = findViewById(R.id.workerCntRow2);
        workerCntRow3 = findViewById(R.id.workerCntRow3);
        workerCntRow4 = findViewById(R.id.workerCntRow4);
        workerCntRow5 = findViewById(R.id.workerCntRow5);

        workerCntSum = findViewById(R.id.workerCntSum);

        mainLayout = findViewById(R.id.mainLayout);

        adMob();
        sumWorkCount();
    }

    private void sumWorkCount(){
        addTextChangeListener(equipCntRow1,AboutRowDialog.ChangeList.ADD_EQUIP);
        addTextChangeListener(equipCntRow2,AboutRowDialog.ChangeList.ADD_EQUIP);
        addTextChangeListener(equipCntRow3,AboutRowDialog.ChangeList.ADD_EQUIP);
        addTextChangeListener(equipCntRow4,AboutRowDialog.ChangeList.ADD_EQUIP);
        addTextChangeListener(equipCntRow5,AboutRowDialog.ChangeList.ADD_EQUIP);
        addTextChangeListener(workerCntRow1,AboutRowDialog.ChangeList.ADD_WORKER);
        addTextChangeListener(workerCntRow2,AboutRowDialog.ChangeList.ADD_WORKER);
        addTextChangeListener(workerCntRow3,AboutRowDialog.ChangeList.ADD_WORKER);
        addTextChangeListener(workerCntRow4,AboutRowDialog.ChangeList.ADD_WORKER);
        addTextChangeListener(workerCntRow5,AboutRowDialog.ChangeList.ADD_WORKER);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void onClickShareBT(View v){
        checkAllowPermissions();
    }

    public void onClickEditBT(View v) {
        DialogFragment dialog = new AboutRowDialog();
        dialog.show(getSupportFragmentManager(), "AboutRowDialog");
    }

    @Override
    public void onSelected(AboutRowDialog.ChangeList selected) {
        if (AboutRowDialog.ChangeList.ADD_EQUIP == selected) {
            LinearLayout equipForm = findViewById(R.id.equipForm);
            GridLayout newView = (GridLayout)getLayoutInflater().inflate(R.layout.add_equip_row, null);
            equipForm.addView(newView);
            addTextChangeListener(newView.findViewById(R.id.addEquipCnt), selected);
        } else if (AboutRowDialog.ChangeList.DEL_EQUIP == selected) {
            LinearLayout equipForm = findViewById(R.id.equipForm);
            int cnt = equipForm.getChildCount();
            if (cnt > 0) {
                View view = equipForm.getChildAt(cnt - 1);
                ((EditText)view.findViewById(R.id.addEquipCnt)).setText("0");
                equipForm.removeView(view);
            } else {
                Toast.makeText(this, getString(R.string.no_more_del), Toast.LENGTH_SHORT).show();
            }
        } else if (AboutRowDialog.ChangeList.ADD_WORKER == selected) {
            LinearLayout workerForm = findViewById(R.id.workerForm);
            GridLayout newView = (GridLayout)getLayoutInflater().inflate(R.layout.add_worker_row, null);
            workerForm.addView(newView);
            addTextChangeListener(newView.findViewById(R.id.addWorkerCnt), selected);
        } else if (AboutRowDialog.ChangeList.DEL_WORKER == selected) {
            LinearLayout workerForm = findViewById(R.id.workerForm);
            int cnt = workerForm.getChildCount();
            if (cnt > 0) {
                View view = workerForm.getChildAt(cnt - 1);
                ((EditText)view.findViewById(R.id.addWorkerCnt)).setText("0");
                workerForm.removeView(view);
            } else {
                Toast.makeText(this, getString(R.string.no_more_del), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addTextChangeListener(View view, final AboutRowDialog.ChangeList selected) {
        if (view instanceof EditText) {
            EditText cnt = (EditText)view;
            cnt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.length() > 0) {
                        if (AboutRowDialog.ChangeList.ADD_EQUIP == selected) {
                            int sum = Integer.parseInt(equipCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                            equipCntSum.setText(String.valueOf(sum));
                        } else if (AboutRowDialog.ChangeList.ADD_WORKER == selected) {
                            int sum = Integer.parseInt(workerCntSum.getText().toString()) - Integer.parseInt(charSequence.toString());
                            workerCntSum.setText(String.valueOf(sum));
                        }
                    }
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length() > 0) {
                        if (AboutRowDialog.ChangeList.ADD_EQUIP == selected) {
                            int sum = Integer.parseInt(equipCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                            equipCntSum.setText(String.valueOf(sum));
                        } else if (AboutRowDialog.ChangeList.ADD_WORKER == selected) {
                            int sum = Integer.parseInt(workerCntSum.getText().toString()) + Integer.parseInt(editable.toString());
                            workerCntSum.setText(String.valueOf(sum));
                        }
                    }
                }
            });
        }
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
        editor.putString("workerDescRow1", workerDescRow1.getText().toString());
        editor.putString("workerDescRow2", workerDescRow2.getText().toString());
        editor.putString("workerDescRow3", workerDescRow3.getText().toString());
        editor.putString("workerDescRow4", workerDescRow4.getText().toString());
        editor.putString("workerDescRow5", workerDescRow5.getText().toString());
        editor.putString("workerCntRow1", workerCntRow1.getText().toString());
        editor.putString("workerCntRow2", workerCntRow2.getText().toString());
        editor.putString("workerCntRow3", workerCntRow3.getText().toString());
        editor.putString("workerCntRow4", workerCntRow4.getText().toString());
        editor.putString("workerCntRow5", workerCntRow5.getText().toString());
        editor.apply();
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
        workerDescRow1.setText(preferences.getString("workerDescRow1",""));
        workerDescRow2.setText(preferences.getString("workerDescRow2",""));
        workerDescRow3.setText(preferences.getString("workerDescRow3",""));
        workerDescRow4.setText(preferences.getString("workerDescRow4",""));
        workerDescRow5.setText(preferences.getString("workerDescRow5",""));
        workerCntRow1.setText(preferences.getString("workerCntRow1",""));
        workerCntRow2.setText(preferences.getString("workerCntRow2",""));
        workerCntRow3.setText(preferences.getString("workerCntRow3",""));
        workerCntRow4.setText(preferences.getString("workerCntRow4",""));
        workerCntRow5.setText(preferences.getString("workerCntRow5",""));
    }

    private Bitmap getBitmapFromScrollView() {
        Bitmap bitmap = Bitmap.createBitmap(mainLayout.getWidth(), mainLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = mainLayout.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        mainLayout.draw(canvas);
        return bitmap;
    }

    private void takeScreenshot(){
        try{
            View root = getWindow().getDecorView().getRootView();
            root.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
            Bitmap bitmap = getBitmapFromScrollView();
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
            e.printStackTrace();
        }
    }

    private void shareImage(File imageFile){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
        intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), "sa.devming.todaywork.fileprovider", imageFile));

        Intent chooser = Intent.createChooser(intent, "");
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(chooser);
    }

    private void checkAllowPermissions() {
        if (PermissionUtil.checkPermissions(this, PermissionUtil.PERMISSIONS[0])
                && PermissionUtil.checkPermissions(this, PermissionUtil.PERMISSIONS[1])) {
            takeScreenshot();
        } else {
            PermissionUtil.requestExternalPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.REQUEST_CODE) {
            if (PermissionUtil.verifyPermission(grantResults)) {
                takeScreenshot();
            } else {
                Toast.makeText(this,getResources().getText(R.string.need_permissions),Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void adMob() {
        AdView mAdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
    }
}
