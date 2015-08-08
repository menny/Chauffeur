package net.evendanan.chauffeur.sample;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.TransitionExperience;
import net.evendanan.chauffeur.lib.TransitionExperiences;

public class MainActivity extends FragmentChauffeurActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
