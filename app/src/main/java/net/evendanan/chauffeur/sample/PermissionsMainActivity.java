package net.evendanan.chauffeur.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import net.evendanan.chauffeur.lib.permissions.PermissionsFragmentChauffeurActivity;

public class PermissionsMainActivity extends PermissionsFragmentChauffeurActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions_activity_main);
        findViewById(R.id.activity_ask_for_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startPermissionsRequest(PermissionRequestCodes.Activity_Contacts.getRequestCode(), Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getApplicationContext(), "READ_CONTACTS request flow started", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "READ_CONTACTS already granted", Toast.LENGTH_SHORT).show();
                }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionRequestCodes.Activity_Contacts.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS was denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS was granted!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PermissionsMainActivity.PermissionRequestCodes.ContactsIntent.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS via intent was denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS via intent was granted!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PermissionsMainActivity.PermissionRequestCodes.ContactsNotification.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS via notification was denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "READ_CONTACTS via notification was granted!", Toast.LENGTH_SHORT).show();
            }

        }
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
}
