package net.evendanan.chauffeur.lib.experiences;

import net.evendanan.chauffeur.lib.R;
import net.evendanan.chauffeur.lib.SimpleTransitionExperience;
import net.evendanan.chauffeur.lib.TransitionExperience;

/**
 * A few basic experiences
 */
public class TransitionExperiences {
    public static final TransitionExperience ROOT_FRAGMENT_EXPERIENCE_TRANSITION = new RootFragmentTransitionExperience();

    public static final TransitionExperience DEEPER_EXPERIENCE_TRANSITION = new SimpleTransitionExperience(
            R.anim.ui_context_deeper_add_in, R.anim.ui_context_deeper_add_out,
            R.anim.ui_context_deeper_pop_in, R.anim.ui_context_deeper_pop_out);

    public static final TransitionExperience OUTSIDE_ON_TOP_EXPERIENCE_TRANSITION = new OutsideOnTopTransitionExperience();

    public static final TransitionExperience DIALOG_EXPERIENCE_TRANSITION = new DialogTransitionExperience();

}
