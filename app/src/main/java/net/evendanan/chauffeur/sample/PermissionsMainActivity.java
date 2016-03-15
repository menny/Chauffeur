package net.evendanan.chauffeur.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import net.evendanan.chauffeur.lib.permissions.PermissionsFragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.permissions.PermissionsRequest;

import java.util.Locale;

public class PermissionsMainActivity extends PermissionsFragmentChauffeurActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions_activity_main);
        findViewById(R.id.activity_ask_for_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPermissionsRequest(new ActivityContactsPermissionRequest());
            }
        });
        findViewById(R.id.navigate_to_app_permissions_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppPermissionsActivity();
            }
        });
    }

    @Override
    @IdRes
    protected int getFragmentRootUiElementId() {
        return R.id.fragments_content_layout;
    }

    @Override
    @NonNull
    protected Fragment createRootFragmentInstance() {
        return PermissionsFragment.newInstance();
    }

    @NonNull
    @Override
    protected PermissionsRequest createPermissionRequestFromIntentRequest(int requestId, @NonNull String[] permissions, @NonNull Intent intent) {
        if (requestId == PermissionRequestCodes.ContactsNotification.getRequestCode()) {
            return new PermissionsRequest.PermissionsRequestBase(requestId, permissions) {
                @Override
                public void onPermissionsGranted() {
                    Toast.makeText(getApplicationContext(), "READ_CONTACTS via notification was granted!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
                    Toast.makeText(getApplicationContext(),
                            String.format(Locale.US, "READ_CONTACTS via notification was denied! Granted %d, denied %d, declined %d.",
                                    grantedPermissions.length, deniedPermissions.length, declinedPermissions.length),
                            Toast.LENGTH_SHORT).show();
                }
            };
        } else {
            return super.createPermissionRequestFromIntentRequest(requestId, permissions, intent);
        }
    }

    public enum PermissionRequestCodes {
        Activity_Contacts,
        Fragment_Contacts,
        Fragment_Location,
        Fragment_Both,
        ContactsIntent,
        ContactsNotification;

        public int getRequestCode() {
            return ordinal() + 1;
        }
    }

    private class ActivityContactsPermissionRequest extends PermissionsRequest.PermissionsRequestBase {

        protected ActivityContactsPermissionRequest() {
            super(PermissionRequestCodes.Activity_Contacts.getRequestCode(), Manifest.permission.READ_CONTACTS);
        }

        @Override
        public void onPermissionsGranted() {
            Toast.makeText(getApplicationContext(), "READ_CONTACTS granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
            Toast.makeText(getApplicationContext(),
                    String.format(Locale.US, "READ_CONTACTS was denied! Granted %d, denied %d, declined %d.",
                            grantedPermissions.length, deniedPermissions.length, declinedPermissions.length),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
