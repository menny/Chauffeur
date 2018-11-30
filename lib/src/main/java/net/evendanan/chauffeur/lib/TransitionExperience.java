package net.evendanan.chauffeur.lib;

import android.os.Parcelable;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

/**
 * Provides a {@link FragmentTransaction} behavior.
 * For most cases, using {@link TransitionExperiences} can provide most of the experiences. If you want
 * more control, consider using {@link SimpleTransitionExperience}.
 */
public interface TransitionExperience extends Parcelable {
    void onPreTransaction(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment);

    void onTransactionReady(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction);

    void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId);

    void onPostTransactionCommit(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment);
}
