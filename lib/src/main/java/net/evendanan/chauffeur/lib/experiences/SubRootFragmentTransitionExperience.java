package net.evendanan.chauffeur.lib.experiences;

import android.os.Parcel;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;

/*package*/ class SubRootFragmentTransitionExperience extends RootFragmentTransitionExperience {
    public SubRootFragmentTransitionExperience() {
    }

    protected SubRootFragmentTransitionExperience(Parcel in) {
        super(in);
    }

    public static final Creator<SubRootFragmentTransitionExperience> CREATOR = new Creator<SubRootFragmentTransitionExperience>() {
        @Override
        public SubRootFragmentTransitionExperience createFromParcel(Parcel in) {
            return new SubRootFragmentTransitionExperience(in);
        }

        @Override
        public SubRootFragmentTransitionExperience[] newArray(int size) {
            return new SubRootFragmentTransitionExperience[size];
        }
    };

    @Override
    public void onPreTransaction(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment) {
        //clearing the stack we currently have, but keeping the previous root
        fragmentChauffeurActivity.getSupportFragmentManager().popBackStack(FragmentChauffeurActivity.ROOT_FRAGMENT_TAG, 0/*don't pop root*/);
    }

    @Override
    public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
    }
}
