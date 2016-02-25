package net.evendanan.chauffeur.sample;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.evendanan.chauffeur.lib.permissions.PermissionsFragmentChauffeurActivity;


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
                if (mOwningActivity.startPermissionsRequestFromFragment(this, PermissionsMainActivity.PermissionRequestCodes.Fragment_Contacts.getRequestCode(), Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getContext(), "Fragment asks for READ_CONTACTS", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Fragment has READ_CONTACTS", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fragment_ask_for_location:
                if (mOwningActivity.startPermissionsRequestFromFragment(this, PermissionsMainActivity.PermissionRequestCodes.Fragment_Location.getRequestCode(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getContext(), "Fragment asks for ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Fragment has ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fragment_ask_for_both:
                if (mOwningActivity.startPermissionsRequestFromFragment(this, PermissionsMainActivity.PermissionRequestCodes.Fragment_Both.getRequestCode(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(getContext(), "Fragment asks for both", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Fragment has both", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mOwningActivity == null) return;
        if (requestCode == PermissionsMainActivity.PermissionRequestCodes.Fragment_Contacts.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getContext(), "Fragment READ_CONTACTS was denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Fragment READ_CONTACTS was granted!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PermissionsMainActivity.PermissionRequestCodes.Fragment_Location.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getContext(), "Fragment LOCATION was denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Fragment LOCATION was granted!", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PermissionsMainActivity.PermissionRequestCodes.Fragment_Both.getRequestCode()) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getContext(), "Fragment both were denied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Fragment both were granted!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
