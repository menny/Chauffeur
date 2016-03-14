package net.evendanan.chauffeur.lib;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public interface FragmentChauffeur {
    String ROOT_FRAGMENT_TAG = "FragmentChauffeur_ROOT_FRAGMENT_TAG";

    void returnToRootFragment(boolean immediately);

    void addFragmentToUi(@NonNull Fragment fragment, @NonNull TransitionExperience experience);

    boolean isChaufferActivityVisible();
}
