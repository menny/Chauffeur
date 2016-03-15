package net.evendanan.chauffeur.lib.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionsUtil {


    /**
     * Returns true if device is running Marshmallow (API level 23) or higher.
     */
    public static boolean isUsingMarshmallowPermissionsModel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
    /**
     * Will start the App-Permissions window for this app.
     * If device is running Android prior to Marshmallow (API level 23), this will be an no-op.
     * Make sure you call {@link #isUsingMarshmallowPermissionsModel()} to verify that this is needed.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void startAppPermissionsActivity(@NonNull Context context) {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    /**
     * Checks if the specific permission was declined by the user (user has denied it, and asked to never be asked again).
     */
    public static boolean isPermissionDeclinedByUser(@NonNull Activity activity, @NonNull String permission) {
        if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(activity, permission)) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }

        return false;
    }
}
