package net.evendanan.chauffeur.sample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;

public class MainActivity extends FragmentChauffeurActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate " + getClass().getName());
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart " + getClass().getName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop " + getClass().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy " + getClass().getName());
    }

    @Override
    @IdRes
    protected int getFragmentRootUiElementId() {
        return R.id.fragments_content_layout;
    }

    @Override
    @NonNull
    protected Fragment createRootFragmentInstance() {
        return BasicFragment.newInstance("Root", "This fragment represents the root of the app.");
    }
}
