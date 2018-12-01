package sa.devming.todaywork;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

class PermissionUtil {
    static final int REQUEST_CODE = 1;
    static final String [] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    static boolean checkPermissions(Activity activity, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(activity, permission);
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }

    static void requestExternalPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, REQUEST_CODE);
    }

    static boolean verifyPermission(int[] grantResults) {
        if (grantResults.length < 1) {
            return false;
        }
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
