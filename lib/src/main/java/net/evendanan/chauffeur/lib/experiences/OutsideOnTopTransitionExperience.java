package net.evendanan.chauffeur.lib.experiences;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.R;
import net.evendanan.chauffeur.lib.SimpleTransitionExperience;

/*package*/  class OutsideOnTopTransitionExperience extends SimpleTransitionExperience implements Parcelable {
    public OutsideOnTopTransitionExperience() {
        super(R.anim.ui_experience_on_top_add_in, 0, 0, R.anim.ui_experience_on_top_pop_out);
    }

    protected OutsideOnTopTransitionExperience(Parcel in) {
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

    public static final Creator<OutsideOnTopTransitionExperience> CREATOR = new Creator<OutsideOnTopTransitionExperience>() {
        @Override
        public OutsideOnTopTransitionExperience createFromParcel(Parcel in) {
            return new OutsideOnTopTransitionExperience(in);
        }

        @Override
        public OutsideOnTopTransitionExperience[] newArray(int size) {
            return new OutsideOnTopTransitionExperience[size];
        }
    };

    @Override
    public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
        //using _add_ instead of _replace_ here to make sure the previous fragment is still shown.
        transaction.add(containerId, fragment);
        transaction.addToBackStack(null);
    }
}
