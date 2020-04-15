package sa.devming.todaywork;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AboutRowDialog.AboutRowDialogListener {
    private EditText workSiteET, descET, dateET;
    private EditText equipCntRow1, equipCntRow2, equipCntRow3, equipCntRow4, equipCntRow5;
    private EditText workerCntRow1, workerCntRow2, workerCntRow3, workerCntRow4, workerCntRow5;
    private TextView equipCntSum, workerCntSum;
    private View mainLayout;

    private int addEquipCnt = 0;
    private int addWorkerCnt = 0;

    private int recoverIndex = 0;

    private Calendar calendar = Calendar.getInstance();
    private int selectYear, selectMonth, selectDay;
    private String myFormat = "yyyy.MM.dd";
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        workSiteET = findViewById(R.id.workSiteET);
        descET = findViewById(R.id.descET);
        dateET = findViewById(R.id.dateET);
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickDateBT(v);
            }
        });
        selectYear = calendar.get(Calendar.YEAR);
        selectMonth = calendar.get(Calendar.MONTH);
        selectDay = calendar.get(Calendar.DAY_OF_MONTH);
        dateET.setText(sdf.format(calendar.getTime()));

        //equip
        equipCntRow1 = findViewById(R.id.equipCntRow1);
        equipCntRow2 = findViewById(R.id.equipCntRow2);
        equipCntRow3 = findViewById(R.id.equipCntRow3);
        equipCntRow4 = findViewById(R.id.equipCntRow4);
        equipCntRow5 = findViewById(R.id.equipCntRow5);

        equipCntSum = findViewById(R.id.equipCntSum);

        //worker
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

    public void onclickDateBT(View v) {
        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateET.setText(sdf.format(calendar.getTime()));

                selectDay = dayOfMonth;
                selectMonth = month;
                selectYear = year;
            }
        }, selectYear, selectMonth, selectDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    @Override
    public void onSelected(AboutRowDialog.ChangeList selected) {
        if (AboutRowDialog.ChangeList.ADD_EQUIP == selected) {
            LinearLayout equipForm = findViewById(R.id.equipForm);
            GridLayout newView = (GridLayout)getLayoutInflater().inflate(R.layout.add_equip_row, null);
            equipForm.addView(newView);
            addTextChangeListener(newView.findViewById(R.id.addEquipCnt), selected);
            addEquipCnt++;
        } else if (AboutRowDialog.ChangeList.DEL_EQUIP == selected) {
            LinearLayout equipForm = findViewById(R.id.equipForm);
            int cnt = equipForm.getChildCount();
            if (cnt > 0) {
                View view = equipForm.getChildAt(cnt - 1);
                ((EditText)view.findViewById(R.id.addEquipCnt)).setText("0");
                equipForm.removeView(view);
                addEquipCnt--;
            } else {
                Toast.makeText(this, getString(R.string.no_more_del), Toast.LENGTH_SHORT).show();
            }
        } else if (AboutRowDialog.ChangeList.ADD_WORKER == selected) {
            LinearLayout workerForm = findViewById(R.id.workerForm);
            GridLayout newView = (GridLayout)getLayoutInflater().inflate(R.layout.add_worker_row, null);
            workerForm.addView(newView);
            addTextChangeListener(newView.findViewById(R.id.addWorkerCnt), selected);
            addWorkerCnt++;
        } else if (AboutRowDialog.ChangeList.DEL_WORKER == selected) {
            LinearLayout workerForm = findViewById(R.id.workerForm);
            int cnt = workerForm.getChildCount();
            if (cnt > 0) {
                View view = workerForm.getChildAt(cnt - 1);
                ((EditText)view.findViewById(R.id.addWorkerCnt)).setText("0");
                workerForm.removeView(view);
                addWorkerCnt--;
            } else {
                Toast.makeText(this, getString(R.string.no_more_del), Toast.LENGTH_SHORT).show();
            }
        } else if (AboutRowDialog.ChangeList.DEL_ALL == selected) {
            ViewGroup vg = findViewById(R.id.activity_main);
            clearForm(vg);
        }
    }

    private void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if (view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
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

    private void getAllText(ViewGroup vg, StringBuilder sb) {
        for (int i = 0, count = vg.getChildCount(); i < count; ++i) {
            View view = vg.getChildAt(i);
            if (view instanceof EditText) {
                String et = ((EditText)view).getText().toString();
                if (et != null && et.length() > 0)
                    sb.append(et).append("|");
                else
                    sb.append("@|");
            }

            if (view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                getAllText((ViewGroup) view, sb);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences("todayWork", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("workSiteET", workSiteET.getText().toString());
        editor.putString("descET", descET.getText().toString());
        editor.putString("dateET", dateET.getText().toString());
        editor.putString("addEquipCnt", addEquipCnt+"");
        editor.putString("addWorkerCnt", addWorkerCnt+"");

        StringBuilder mainGrid = new StringBuilder();
        ViewGroup mainGridVG = findViewById(R.id.mainGrid);
        getAllText(mainGridVG, mainGrid);
        editor.putString("mainGrid", mainGrid.toString());

        StringBuilder equipForm = new StringBuilder();
        ViewGroup equipFormVG = findViewById(R.id.equipForm);
        getAllText(equipFormVG, equipForm);
        editor.putString("equipForm", equipForm.toString());

        StringBuilder mainGrid2 = new StringBuilder();
        ViewGroup mainGrid2VG = findViewById(R.id.mainGrid2);
        getAllText(mainGrid2VG, mainGrid2);
        editor.putString("mainGrid2", mainGrid2.toString());

        StringBuilder workerForm = new StringBuilder();
        ViewGroup workerFormVG = findViewById(R.id.workerForm);
        getAllText(workerFormVG, workerForm);
        editor.putString("workerForm", workerForm.toString());

        StringBuilder mainGrid3 = new StringBuilder();
        ViewGroup mainGrid3VG = findViewById(R.id.mainGrid3);
        getAllText(mainGrid3VG, mainGrid3);
        editor.putString("mainGrid3", mainGrid3.toString());

        editor.apply();
    }

    private void controlAdditionalForm(AboutRowDialog.ChangeList type, int cnt) {
        for (int i=0 ; i<cnt ; i++) {
            onSelected(type);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("todayWork", Activity.MODE_PRIVATE);

        workSiteET.setText(preferences.getString("workSiteET",""));
        descET.setText(preferences.getString("descET", ""));
        dateET.setText(preferences.getString("dateET", sdf.format(calendar.getTime())));
        int equipCnt = Integer.parseInt(preferences.getString("addEquipCnt", "0"));
        int workerCnt = Integer.parseInt(preferences.getString("addWorkerCnt", "0"));

        // 현재 추가된 row 가 남아있다면 모두 삭제
        LinearLayout eForm = findViewById(R.id.equipForm);
        int remainF = eForm.getChildCount();
        controlAdditionalForm(AboutRowDialog.ChangeList.DEL_EQUIP, remainF);

        LinearLayout wForm = findViewById(R.id.workerForm);
        int remainW = wForm.getChildCount();
        controlAdditionalForm(AboutRowDialog.ChangeList.DEL_WORKER, remainW);

        // addEquipCnt 만큼 레이아웃 추가
        controlAdditionalForm(AboutRowDialog.ChangeList.ADD_EQUIP, equipCnt);
        // addWorkerCnt 만틈 레이아웃 추가
        controlAdditionalForm(AboutRowDialog.ChangeList.ADD_WORKER, workerCnt);

        String[] mainGrid = preferences.getString("mainGrid","").split("[|]");
        String[] equipForm = preferences.getString("equipForm","").split("[|]");
        String[] mainGrid2 = preferences.getString("mainGrid2","").split("[|]");
        String[] workerForm = preferences.getString("workerForm","").split("[|]");
        String[] mainGrid3 = preferences.getString("mainGrid3","").split("[|]");

        if (isNotEmptyArr(mainGrid)) {
            ViewGroup mainGridVG = findViewById(R.id.mainGrid);
            setAllText(mainGridVG, mainGrid);
        }

        if (isNotEmptyArr(equipForm)) {
            ViewGroup equipFormVG = findViewById(R.id.equipForm);
            setAllText(equipFormVG, equipForm);
        }

        if (isNotEmptyArr(mainGrid2)) {
            ViewGroup mainGrid2VG = findViewById(R.id.mainGrid2);
            setAllText(mainGrid2VG, mainGrid2);
        }

        if (isNotEmptyArr(workerForm)) {
            ViewGroup workerFormVG = findViewById(R.id.workerForm);
            setAllText(workerFormVG, workerForm);
        }

        if (isNotEmptyArr(mainGrid3)) {
            ViewGroup mainGrid3VG = findViewById(R.id.mainGrid3);
            setAllText(mainGrid3VG, mainGrid3);
        }

        preferences.edit().clear().apply();
    }

    private boolean isNotEmptyArr(String[] arr) {
        if (arr == null || arr.length == 0)
            return false;

        if (arr.length == 1 && arr[0].equals("")) {
            return false;
        }

        return true;
    }

    private void setAllText(ViewGroup vg, final String[] strArr) {
        recoverIndex = 0;
        goSetAllText(vg, strArr);
    }

    private void goSetAllText(ViewGroup vg, final String[] strArr) {
        for (int i = 0, count = vg.getChildCount(); i < count; ++i) {
            View view = vg.getChildAt(i);
            if (view instanceof EditText) {
                String value = strArr[recoverIndex++];
                ((EditText)view).setText("@".equals(value) ? "" : value);
            }

            if (view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0)) {
                goSetAllText((ViewGroup) view, strArr);
            }
        }
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
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        AdView mAdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);
    }
}
