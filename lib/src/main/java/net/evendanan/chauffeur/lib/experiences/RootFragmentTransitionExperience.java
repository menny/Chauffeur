package net.evendanan.chauffeur.lib.experiences;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.R;
import net.evendanan.chauffeur.lib.SimpleTransitionExperience;

/*package*/ class RootFragmentTransitionExperience extends SimpleTransitionExperience implements Parcelable {
    public RootFragmentTransitionExperience() {
        super(R.anim.ui_context_root_add_in, R.anim.ui_context_root_add_out, R.anim.ui_context_root_pop_in, R.anim.ui_context_fade_out);
    }

    protected RootFragmentTransitionExperience(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RootFragmentTransitionExperience> CREATOR = new Creator<RootFragmentTransitionExperience>() {
        @Override
        public RootFragmentTransitionExperience createFromParcel(Parcel in) {
            return new RootFragmentTransitionExperience(in);
        }

        @Override
        public RootFragmentTransitionExperience[] newArray(int size) {
            return new RootFragmentTransitionExperience[size];
        }
    };

    @Override
    public void onPreTransaction(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment) {
        super.onPreTransaction(fragmentChauffeurActivity, fragment);
        final FragmentManager supportFragmentManager = fragmentChauffeurActivity.getSupportFragmentManager();

        supportFragmentManager.popBackStack(FragmentChauffeurActivity.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(FragmentChauffeurActivity.ROOT_FRAGMENT_TAG);
    }
}
