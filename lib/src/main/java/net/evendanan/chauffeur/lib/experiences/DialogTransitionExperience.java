package net.evendanan.chauffeur.lib.experiences;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.R;
import net.evendanan.chauffeur.lib.SimpleTransitionExperience;

/*package*/ class DialogTransitionExperience extends SimpleTransitionExperience implements Parcelable {

    public DialogTransitionExperience() {
        super(R.anim.ui_experience_dialog_add_in, 0, 0, R.anim.ui_experience_dialog_pop_out);
    }

    protected DialogTransitionExperience(Parcel in) {
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

    public static final Creator<DialogTransitionExperience> CREATOR = new Creator<DialogTransitionExperience>() {
        @Override
        public DialogTransitionExperience createFromParcel(Parcel in) {
            return new DialogTransitionExperience(in);
        }

        @Override
        public DialogTransitionExperience[] newArray(int size) {
            return new DialogTransitionExperience[size];
        }
    };

    @Override
    public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
        //using _add_ instead of _replace_ here to make sure the previous fragment is still shown.
        transaction.add(containerId, fragment);
        transaction.addToBackStack(null);
    }
}
