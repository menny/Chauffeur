/*
 * Copyright (c) 2013 Menny Even-Danan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evendanan.chauffeur.lib.permissions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionsFragmentChauffeurActivity extends FragmentChauffeurActivity {

    private static final String TAG = "Permissions";
    private static final String PERMISSION_ARG_REQUEST_ID = "PermissionsFragmentChauffeurActivity_PERMISSION_ARG_REQUEST_ID";
    private static final String PERMISSION_ARG_REQUIRED_PERMISSIONS = "PermissionsFragmentChauffeurActivity_PERMISSION_ARG_REQUIRED_PERMISSIONS";

    /**
     * Creates an Intent that asks PermissionsFragmentChauffeurActivity to start a request for permissions.
     * NOTE: The result of this request will be in the context of the PermissionsFragmentChauffeurActivity!
     * You should use this method to initiate requests from Services (for example).
     * @return an Intent that starts PermissionsFragmentChauffeurActivity, or null if the permissions are already granted.
     */
    @Nullable
    public static Intent createIntentToPermissionsRequest(@NonNull Context context, @NonNull Class<? extends PermissionsFragmentChauffeurActivity> mainActivity, int requestId, @NonNull String... permissions) {
        List<String> requiredPermissions = filterGrantedPermissions(context, permissions);
        if (requiredPermissions.size() > 0) {
            Intent intent = new Intent(context, mainActivity);
            intent.putExtra(PERMISSION_ARG_REQUEST_ID, requestId);
            intent.putExtra(PERMISSION_ARG_REQUIRED_PERMISSIONS, requiredPermissions.toArray(new String[requiredPermissions.size()]));
            return intent;
        } else {
            return null;
        }
    }

    @NonNull
    private static List<String> filterGrantedPermissions(@NonNull Context context, @NonNull String... permissions) {
        ArrayList<String> requiredPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission);
                Log.i(TAG, "Will ask for permission '" + permission + "'.");
            }
        }

        return requiredPermissions;
    }

    /**
     * Start a permissions request flow for the provided permissions.
     * <p/>
     * This method should only be called by this Activity! If you want to ask a permission from a Fragment, use
     * {@link #startPermissionsRequestFromFragment(Fragment, int, String...)}
     * @param requestId   you should know where to handle the provided request ID (a Fragment or this Activity).
     * @param permissions should be part of {@link android.Manifest.permission}.
     * @return true if permissions-request-flow has started, false if all permissions are already granted.
     */
    protected boolean startPermissionsRequest(int requestId, @NonNull String... permissions) {
        List<String> requiredPermissions = filterGrantedPermissions(this, permissions);
        if (requiredPermissions.size() > 0) {
            //asking for permissions
            ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[requiredPermissions.size()]), requestId);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Start a permissions request flow for the provided permissions from a Fragment.
     * <p/>
     * This will ensure that the result will be sent to the given fragment.
     * This method should only be called by fragment owned by this Activity! If you want to ask a permission for this Activity, use
     * {@link #startPermissionsRequest(int, String...)}
     * @param requestId   you should know where to handle the provided request ID (a Fragment or this Activity).
     * @param permissions should be part of {@link android.Manifest.permission}.
     * @return true if permissions-request-flow has started, false if all permissions are already granted.
     */
    public boolean startPermissionsRequestFromFragment(Fragment fragment, int requestId, @NonNull String... permissions) {
        List<String> requiredPermissions = filterGrantedPermissions(this, permissions);
        if (requiredPermissions.size() > 0) {
            //asking for permissions
            fragment.requestPermissions(requiredPermissions.toArray(new String[requiredPermissions.size()]), requestId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePermissionsRequestIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the Activity is not alive, onNewIntent will not be called.
        //so we should also handle it here.
        handlePermissionsRequestIntent(getIntent());
    }

    private void handlePermissionsRequestIntent(Intent intent) {
        int requestId = intent.getIntExtra(PERMISSION_ARG_REQUEST_ID, 0);
        String[] permissions = intent.getStringArrayExtra(PERMISSION_ARG_REQUIRED_PERMISSIONS);
        if (requestId != 0 && permissions != null && permissions.length > 0) {
            //removing processed details
            intent.removeExtra(PERMISSION_ARG_REQUEST_ID);
            intent.removeExtra(PERMISSION_ARG_REQUIRED_PERMISSIONS);
            //asking for permissions
            ActivityCompat.requestPermissions(this, permissions, requestId);
        }
    }

    /**
     * Will start the App-Permissions window for this app.
     * If device is running Android prior to Marshmallow (API level 23), this will be an no-op.
     */
    public void startAppPermissionsActivity() {
        if (isUsingMarshmallowPermissionsModel()) {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);
        }
    }

    /**
     * Returns true if device is running Marshmallow (API level 23) or higher.
     */
    public boolean isUsingMarshmallowPermissionsModel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
