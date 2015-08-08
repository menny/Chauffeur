package net.evendanan.chauffeur.lib;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class TransitionExperiences {
    public static final TransitionExperience ROOT_FRAGMENT_EXPERIENCE_TRANSITION = new SimpleTransitionExperience(
            R.anim.ui_context_root_add_in, R.anim.ui_context_root_add_out,
            R.anim.ui_context_root_pop_in, R.anim.ui_context_root_pop_out) {
        @Override
        public void onPreTransaction(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment) {
            super.onPreTransaction(fragmentChauffeurActivity, fragment);
            //clearing the stack we currently have.
            fragmentChauffeurActivity.getSupportFragmentManager().popBackStack(FragmentChauffeurActivity.ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @Override
        public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
            transaction.replace(containerId, fragment);
            transaction.addToBackStack(FragmentChauffeurActivity.ROOT_FRAGMENT_TAG);
        }
    };


    public static final TransitionExperience DEEPER_EXPERIENCE_TRANSITION = new SimpleTransitionExperience(
            R.anim.ui_context_deeper_add_in, R.anim.ui_context_deeper_add_out,
            R.anim.ui_context_deeper_pop_in, R.anim.ui_context_deeper_pop_out);

    public static final TransitionExperience OUTSIDE_ON_TOP_EXPERIENCE_TRANSITION = new SimpleTransitionExperience(
            R.anim.ui_experience_on_top_add_in, 0, 0, R.anim.ui_experience_on_top_pop_out) {
        @Override
        public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
            //using _add_ instead of _replace_ here to make sure the previous fragment is still shown.
            transaction.add(containerId, fragment);
            transaction.addToBackStack(null);
        }
    };

    public static final TransitionExperience DIALOG_EXPERIENCE_TRANSITION = new SimpleTransitionExperience(
            R.anim.ui_experience_dialog_add_in, 0, 0, R.anim.ui_experience_dialog_pop_out) {

        @Override
        public void onAddFragmentToContainer(@NonNull FragmentChauffeurActivity fragmentChauffeurActivity, @NonNull Fragment fragment, @NonNull FragmentTransaction transaction, @IdRes int containerId) {
            //using _add_ instead of _replace_ here to make sure the previous fragment is still shown.
            transaction.add(containerId, fragment);
            transaction.addToBackStack(null);
        }
    };
}
