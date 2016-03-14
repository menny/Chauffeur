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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class PermissionsFragmentChauffeurActivity extends FragmentChauffeurActivity {

    private static final String TAG = "Permissions";
    private static final String INTENT_PERMISSION_ACTION = "PermissionsFragmentChauffeurActivity_INTENT_PERMISSION_ACTION";
    private static final String PERMISSION_ARG_REQUEST_ID = "PermissionsFragmentChauffeurActivity_PERMISSION_ARG_REQUEST_ID";
    private static final String PERMISSION_ARG_REQUIRED_PERMISSIONS = "PermissionsFragmentChauffeurActivity_PERMISSION_ARG_REQUIRED_PERMISSIONS";

    private final SparseArrayCompat<PermissionsRequest> mPermissionsRequestsInProgress = new SparseArrayCompat<>();

    /**
     * Creates an Intent that asks PermissionsFragmentChauffeurActivity to start a request for permissions.
     * NOTE: The result of this request will be in the context of the PermissionsFragmentChauffeurActivity!
     * You should use this method to initiate requests from Services (for example).
     *
     * @return an Intent that starts PermissionsFragmentChauffeurActivity, or null if the permissions are already granted.
     */
    @Nullable
    public static Intent createIntentToPermissionsRequest(@NonNull Context context, @NonNull Class<? extends PermissionsFragmentChauffeurActivity> mainActivity, int requestId, @NonNull String... permissions) {
        List<String> requiredPermissions = filterGrantedPermissions(context, permissions);
        if (requiredPermissions.size() > 0) {
            Intent intent = new Intent(context, mainActivity);
            intent.setAction(INTENT_PERMISSION_ACTION);
            intent.putExtra(PERMISSION_ARG_REQUEST_ID, requestId);
            intent.putExtra(PERMISSION_ARG_REQUIRED_PERMISSIONS, permissions);
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
     */
    public void startPermissionsRequest(PermissionsRequest permissionsRequest) {
        List<String> requiredPermissions = filterGrantedPermissions(this, permissionsRequest.getRequestedPermissions());
        if (requiredPermissions.size() > 0) {
            //asking for permissions
            mPermissionsRequestsInProgress.put(permissionsRequest.getRequestCode(), permissionsRequest);
            ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[requiredPermissions.size()]), permissionsRequest.getRequestCode());
        } else {
            permissionsRequest.onPermissionsGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsRequest request = mPermissionsRequestsInProgress.get(requestCode);
        if (request != null) {
            //this is a request made using PermissionsRequest
            //and it is no longer "in-progress"
            mPermissionsRequestsInProgress.remove(requestCode);
            if (allPermissionsResultsAreGranted(grantResults)) {
                request.onPermissionsGranted();
            } else {
                ArrayList<String> grantedPermissions = new ArrayList<>(permissions.length - 1);
                ArrayList<String> deniedPermissions = new ArrayList<>(permissions.length - 1);
                ArrayList<String> declinedPermissions = new ArrayList<>(permissions.length - 1);
                for (int permissionIndex = 0; permissionIndex < grantResults.length; permissionIndex++) {
                    final int grantResult = grantResults[permissionIndex];
                    final String permission = permissions[permissionIndex];
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissions.add(permission);
                    } else {
                        //if the result is DENIED and the OS says "do not show rationale",
                        // it means the user has ticked "Don't ask me again".
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                            deniedPermissions.add(permission);
                        } else {
                            declinedPermissions.add(permission);
                        }
                    }
                }
                request.onPermissionsDenied(
                        grantedPermissions.toArray(new String[grantedPermissions.size()]),
                        deniedPermissions.toArray(new String[deniedPermissions.size()]),
                        declinedPermissions.toArray(new String[declinedPermissions.size()]));
            }
        }
    }

    private boolean allPermissionsResultsAreGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) return false;
        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlePermissionsRequestIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPermissionsRequestsInProgress.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the Activity is not alive, onNewIntent will not be called.
        //so we should also handle it here.
        handlePermissionsRequestIntent(getIntent());
    }

    private void handlePermissionsRequestIntent(Intent intent) {
        if (INTENT_PERMISSION_ACTION.equals(intent.getAction())) {
            int requestId = intent.getIntExtra(PERMISSION_ARG_REQUEST_ID, 0);
            String[] permissions = intent.getStringArrayExtra(PERMISSION_ARG_REQUIRED_PERMISSIONS);
            intent.setAction(null/*not going to process this action again*/);
            if (requestId == 0 || permissions == null || permissions.length == 0) return;
            PermissionsRequest request = createPermissionRequestFromIntentRequest(requestId, permissions, intent);
            //asking for permissions
            startPermissionsRequest(request);
        }
    }

    /**
     * Creates a {@link PermissionsRequest} from the intercepted Intent (which was created using {@link #createIntentToPermissionsRequest(Context, Class, int, String...)}.
     * Default implementation will return an implementation with no-op callbacks, override this method if you want to do
     * something more specific.
     *
     * @param requestId   request-id parsed from the Intent.
     * @param permissions permissions array parsed from the Intent.
     * @param intent      the intercepted Intent
     */
    @NonNull
    protected PermissionsRequest createPermissionRequestFromIntentRequest(int requestId, @NonNull String[] permissions, @NonNull Intent intent) {
        return new PermissionsRequest.PermissionsRequestBase(requestId, permissions) {
            @Override
            public void onPermissionsGranted() {
                /*no-op*/
            }

            @Override
            public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
                /*no-op*/
            }
        };
    }

    /**
     * Will start the App-Permissions window for this app.
     * If device is running Android prior to Marshmallow (API level 23), this will be an no-op.
     */
    public static void startAppPermissionsActivity(@NonNull Context context) {
        if (isUsingMarshmallowPermissionsModel()) {
            final Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        }
    }

    /**
     * Returns true if device is running Marshmallow (API level 23) or higher.
     */
    public static boolean isUsingMarshmallowPermissionsModel() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
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
