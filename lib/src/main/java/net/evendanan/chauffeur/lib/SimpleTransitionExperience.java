package net.evendanan.chauffeur.lib;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * A simple naive implementation of {@link TransitionExperience}.
 * Uses {@link FragmentTransaction#replace(int, Fragment)} to add fragments into the transaction.
 *
 * This class is {@code abstract}, you should extend it, and provide the animation resources for each
 * stage.
 */
public class SimpleTransitionExperience implements TransitionExperience, Parcelable {

    @AnimRes
    private final int mEnterFragmentAddInAnimationId;

    @AnimRes
    private final int mEnterFragmentAddOutAnimationId;

    @AnimRes
    private final int mExitFragmentPopInAnimationId;

    @AnimRes
    private final int mExitFragmentPopOutAnimationId;


    public SimpleTransitionExperience(@AnimRes int enterFragmentAddInAnimationId, @AnimRes int enterFragmentAddOutAnimationId,
                                      @AnimRes int exitFragmentPopInAnimationId, @AnimRes int exitFragmentPopOutAnimationId) {
        mEnterFragmentAddInAnimationId = enterFragmentAddInAnimationId;
        mEnterFragmentAddOutAnimationId = enterFragmentAddOutAnimationId;
        mExitFragmentPopInAnimationId = exitFragmentPopInAnimationId;
        mExitFragmentPopOutAnimationId = exitFragmentPopOutAnimationId;
    }

    protected SimpleTransitionExperience(Parcel in) {
        mEnterFragmentAddInAnimationId = in.readInt();
        mEnterFragmentAddOutAnimationId = in.readInt();
        mExitFragmentPopInAnimationId = in.readInt();
        mExitFragmentPopOutAnimationId = in.readInt();
    }

    @Override
    public void onPreTransaction(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment) {

    }

    @Override
    public void onTransactionReady(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction) {
        transaction.setCustomAnimations(
                mEnterFragmentAddInAnimationId,
                mEnterFragmentAddOutAnimationId,
                mExitFragmentPopInAnimationId,
                mExitFragmentPopOutAnimationId);
    }

    @Override
    public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
    }

    @Override
    public void onPostTransactionCommit(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment) {

    }

    public static final Creator<SimpleTransitionExperience> CREATOR = new Creator<SimpleTransitionExperience>() {
        @Override
        public SimpleTransitionExperience createFromParcel(Parcel in) {
            return new SimpleTransitionExperience(in);
        }

        @Override
        public SimpleTransitionExperience[] newArray(int size) {
            return new SimpleTransitionExperience[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mEnterFragmentAddInAnimationId);
        dest.writeInt(mEnterFragmentAddOutAnimationId);
        dest.writeInt(mExitFragmentPopInAnimationId);
        dest.writeInt(mExitFragmentPopOutAnimationId);
    }
}
