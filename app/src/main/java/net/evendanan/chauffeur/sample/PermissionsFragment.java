package net.evendanan.chauffeur.sample;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.evendanan.chauffeur.lib.permissions.PermissionsFragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.permissions.PermissionsRequest;
import net.evendanan.chauffeur.lib.permissions.PermissionsUtil;

import java.util.Locale;


public class PermissionsFragment extends Fragment implements View.OnClickListener {

    @Nullable
    private PermissionsFragmentChauffeurActivity mOwningActivity;

    public PermissionsFragment() {
    }

    public static PermissionsFragment newInstance() {
        PermissionsFragment fragment = new PermissionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOwningActivity = (PermissionsFragmentChauffeurActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwningActivity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.fragment_ask_for_contacts).setOnClickListener(this);
        view.findViewById(R.id.fragment_ask_for_location).setOnClickListener(this);
        view.findViewById(R.id.fragment_ask_for_both).setOnClickListener(this);
        view.findViewById(R.id.fragment_ask_for_contacts_via_intent).setOnClickListener(this);
        view.findViewById(R.id.fragment_ask_for_contacts_via_notification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOwningActivity == null) return;
        switch (v.getId()) {
            case R.id.fragment_ask_for_contacts:
                mOwningActivity.startPermissionsRequest(new FragmentContactsPermissionRequest());
                break;
            case R.id.fragment_ask_for_location:
                mOwningActivity.startPermissionsRequest(new FragmentLocationPermissionRequest());
                break;
            case R.id.fragment_ask_for_both:
                mOwningActivity.startPermissionsRequest(new FragmentBothPermissionsRequest());
                break;
            case R.id.fragment_ask_for_contacts_via_intent:
                Intent directCallIntent = PermissionsFragmentChauffeurActivity.createIntentToPermissionsRequest(getContext(), PermissionsMainActivity.class, PermissionsMainActivity.PermissionRequestCodes.ContactsIntent.getRequestCode(), Manifest.permission.READ_CONTACTS);
                if (directCallIntent != null) {
                    Toast.makeText(getContext(), "Fragment asks for both via intent", Toast.LENGTH_SHORT).show();
                    startActivity(directCallIntent);
                } else {
                    Toast.makeText(getContext(), "Fragment has both. No need to start Intent", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fragment_ask_for_contacts_via_notification:
                Intent notificationCallIntent = PermissionsFragmentChauffeurActivity.createIntentToPermissionsRequest(getContext(), PermissionsMainActivity.class, PermissionsMainActivity.PermissionRequestCodes.ContactsNotification.getRequestCode(), Manifest.permission.READ_CONTACTS);
                if (notificationCallIntent != null) {
                    Toast.makeText(getContext(), "Fragment asks for both via notification", Toast.LENGTH_SHORT).show();
                    PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), R.id.fragment_ask_for_contacts_via_notification, notificationCallIntent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
                    builder.setTicker("Contacts permissions required");
                    builder.setSmallIcon(R.drawable.ic_request_contacts);
                    builder.setContentIntent(pendingIntent);
                    builder.setContentTitle("Contacts");
                    builder.setAutoCancel(true);
                    builder.setDefaults(Notification.DEFAULT_ALL);
                    builder.setContentText("We need to read you contacts information to better personalize your experience.");
                    NotificationManagerCompat.from(getContext()).notify(R.id.fragment_ask_for_contacts_via_notification, builder.build());
                } else {
                    Toast.makeText(getContext(), "Fragment has both. No need to start notification", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private class FragmentContactsPermissionRequest extends PermissionsRequest.PermissionsRequestBase {

        protected FragmentContactsPermissionRequest() {
            super(PermissionsMainActivity.PermissionRequestCodes.Fragment_Contacts.getRequestCode(), Manifest.permission.READ_CONTACTS);
        }

        @Override
        public void onPermissionsGranted() {
            Toast.makeText(getContext(), "Fragment READ_CONTACTS was granted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
            Toast.makeText(getContext(), String.format(Locale.US, "Fragment READ_CONTACTS was denied! Granted %d, denied %d, declined %d.",
                    grantedPermissions.length, deniedPermissions.length, declinedPermissions.length),
                    Toast.LENGTH_SHORT).show();

        }
    }

    private class FragmentLocationPermissionRequest extends PermissionsRequest.PermissionsRequestBase {
        protected FragmentLocationPermissionRequest() {
            super(PermissionsMainActivity.PermissionRequestCodes.Fragment_Location.getRequestCode(), Manifest.permission.ACCESS_FINE_LOCATION);
        }

        @Override
        public void onPermissionsGranted() {
            Toast.makeText(getContext(), "Fragment ACCESS_FINE_LOCATION was granted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
            if (deniedPermissions.length > 0) {
                Toast.makeText(getContext(), "Fragment ACCESS_FINE_LOCATION was denied!", Toast.LENGTH_SHORT).show();
            }

            if (declinedPermissions.length > 0) {
                Toast.makeText(getContext(), "Fragment ACCESS_FINE_LOCATION was declined!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FragmentBothPermissionsRequest extends PermissionsRequest.PermissionsRequestBase {
        private final DialogInterface.OnClickListener mOnRetryClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOwningActivity != null)
                    mOwningActivity.startPermissionsRequest(FragmentBothPermissionsRequest.this);
            }
        };
        private final DialogInterface.OnClickListener mOnCancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Fragment BOTH was denied!", Toast.LENGTH_SHORT).show();
            }
        };
        private final DialogInterface.OnClickListener mOnShowSettingsClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOwningActivity != null)
                    PermissionsUtil.startAppPermissionsActivity(mOwningActivity);
            }
        };

        protected FragmentBothPermissionsRequest() {
            super(PermissionsMainActivity.PermissionRequestCodes.Fragment_Both.getRequestCode(),
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS);
        }

        @Override
        public void onPermissionsGranted() {
            Toast.makeText(getContext(), "Fragment BOTH were granted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Permissions Required");

            if (declinedPermissions.length > 0) {
                builder.setMessage("You gotta understand! You can not use this feature if we\n" +
                        "are not allowed to read your contacts and current location!\n" +
                        "Please enable the required permissions in Settings.");
                builder.setPositiveButton("Take me there", mOnShowSettingsClickListener);
            } else {
                builder.setMessage("For a better user experience, we want to know a bit about you.\n" +
                        "If we know where you are and who are you friends, we can give you\n" +
                        "a much better suggestions.");
                builder.setPositiveButton("Okay", mOnRetryClickListener);
            }

            builder.setNegativeButton("No, thanks.", mOnCancelClickListener);
            builder.show();
        }
    }


}
