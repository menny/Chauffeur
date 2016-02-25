package net.evendanan.chauffeur.sample;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.evendanan.chauffeur.lib.FragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.experiences.TransitionExperiences;

public class BasicFragment extends Fragment {
    private static final String ARG_TITLE = "BasicFragment_title";
    private static final String ARG_TEXT = "BasicFragment_text";

    private String mTitle;
    private String mText;
    private final View.OnClickListener mNavigationClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.go_to_root:
                    //either use an Intent to navigate to another fragment
                    Fragment rootFragment = BasicFragment.newInstance("Root", "This is a root fragment.");
                    Intent goToRootIntent = FragmentChauffeurActivity.createStartActivityIntentForAddingFragmentToUi(getActivity(), MainActivity.class, rootFragment, TransitionExperiences.ROOT_FRAGMENT_EXPERIENCE_TRANSITION);
                    getActivity().startActivity(goToRootIntent);
                    break;
                case R.id.go_to_root_2:
                    //or directly call the API
                    Fragment root2Fragment = BasicFragment.newInstance("A Different Root", "This is a completely different root fragment.");
                    FragmentChauffeurActivity activityForRoot = (FragmentChauffeurActivity) getActivity();
                    activityForRoot.addFragmentToUi(root2Fragment, TransitionExperiences.ROOT_FRAGMENT_EXPERIENCE_TRANSITION);
                    break;
                case R.id.go_to_root_3:
                    Fragment root3Fragment = BasicFragment.newInstance("A Sub-Root", "This is a root fragment, on-top the original root.");
                    FragmentChauffeurActivity activityForSubRoot = (FragmentChauffeurActivity) getActivity();
                    activityForSubRoot.addFragmentToUi(root3Fragment, TransitionExperiences.SUB_ROOT_FRAGMENT_EXPERIENCE_TRANSITION);
                    break;
                case R.id.go_deeper:
                    Fragment deeperFragment = BasicFragment.newInstance("Deeper from " + mTitle, "This is a deeper fragment, which came from " + mTitle);
                    FragmentChauffeurActivity activityForGoDeeper = (FragmentChauffeurActivity) getActivity();
                    activityForGoDeeper.addFragmentToUi(deeperFragment, TransitionExperiences.DEEPER_EXPERIENCE_TRANSITION);
                    break;
                case R.id.go_on_top:
                    Fragment onTopFragment = BasicFragment.newInstance("On-top " + mTitle, "This is a On-Top experience fragment for " + mTitle);
                    FragmentChauffeurActivity activityForOnTop = (FragmentChauffeurActivity) getActivity();
                    activityForOnTop.addFragmentToUi(onTopFragment, TransitionExperiences.OUTSIDE_ON_TOP_EXPERIENCE_TRANSITION);
                    break;
                case R.id.go_dialog:
                    Fragment dialogFragment = BasicFragment.newInstance("Dialog " + mTitle, "This is a dialog experience fragment for " + mTitle);
                    Intent goToDialogIntent = FragmentChauffeurActivity.createStartActivityIntentForAddingFragmentToUi(getActivity(), MainActivity.class, dialogFragment, TransitionExperiences.DIALOG_EXPERIENCE_TRANSITION);
                    getActivity().startActivity(goToDialogIntent);
                    break;
                case R.id.go_to_permissions:
                    startActivity(new Intent(getContext(), PermissionsMainActivity.class));
                    break;
            }
        }
    };

    public static BasicFragment newInstance(String title, String text) {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(ARG_TITLE);
        mText = getArguments().getString(ARG_TEXT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView textView = (TextView) view.findViewById(R.id.text);
        titleView.setText(mTitle);
        textView.setText(mText);

        view.findViewById(R.id.go_to_root).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_to_root_2).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_to_root_3).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_deeper).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_dialog).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_on_top).setOnClickListener(mNavigationClickHandler);
        view.findViewById(R.id.go_to_permissions).setOnClickListener(mNavigationClickHandler);
    }
}
