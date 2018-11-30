package net.evendanan.chauffeur.lib.permissions;

import androidx.annotation.NonNull;

public interface PermissionsRequest {

    /**
     * The request-code associated with this permissions-request
     *
     * @return an int between 1..255
     */
    int getRequestCode();

    /**
     * The {@link android.Manifest.permission} requested in this request
     */
    @NonNull
    String[] getRequestedPermissions();

    /**
     * Called if all permissions were granted (or were granted before)
     */
    void onPermissionsGranted();

    /**
     * Called if the user has denied or declined at least one permission of the requested permissions.
     * At this point you have two options:
     * 1) show a dialog with a rationale for the requested permission and give the user an option
     * to grant the permission again. Makes sense if this is a critical permission to have.
     * 2) cancel the operation that requires this permission.
     * @param grantedPermissions permissions the user has approved.
     * @param deniedPermissions permissions the user has denied.
     * @param declinedPermissions permissions the user has denied and asked NOT to be asked again.
     */
    void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions);


    abstract class PermissionsRequestBase implements PermissionsRequest {
        private final int mRequestCode;
        @NonNull
        private final String[] mRequestedPermissions;

        protected PermissionsRequestBase(int requestCode, @NonNull String... permissions) {
            mRequestCode = requestCode;
            mRequestedPermissions = permissions;
        }

        @Override
        public int getRequestCode() {
            return mRequestCode;
        }

        @Override
        @NonNull
        public String[] getRequestedPermissions() {
            return mRequestedPermissions;
        }
    }
}
