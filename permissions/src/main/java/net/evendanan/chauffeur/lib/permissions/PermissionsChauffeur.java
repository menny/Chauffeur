package net.evendanan.chauffeur.lib.permissions;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;

import net.evendanan.chauffeur.lib.FragmentChauffeur;

public interface PermissionsChauffeur extends FragmentChauffeur {
    void startPermissionsRequest(PermissionsRequest permissionsRequest);

    /**
     * Will start the App-Permissions window for this app.
     * If device is running Android prior to Marshmallow (API level 23), this will be an no-op.
     * Make sure you call {@link PermissionsUtil#isUsingMarshmallowPermissionsModel()} to verify that this is needed.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void startAppPermissionsActivity();

    /**
     * Checks if the specific permission was declined by the user (user has denied it, and asked to never be asked again).
     */
    boolean isPermissionDeclinedByUser(@NonNull String permission);
}
