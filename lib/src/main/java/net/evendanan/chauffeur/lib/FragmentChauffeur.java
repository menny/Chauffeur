package net.evendanan.chauffeur.lib;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface FragmentChauffeur {
    String ROOT_FRAGMENT_TAG = "FragmentChauffeur_ROOT_FRAGMENT_TAG";

    void returnToRootFragment(boolean immediately);

    void addFragmentToUi(@NonNull Fragment fragment, @NonNull TransitionExperience experience);

    boolean isChaufferActivityVisible();
}
