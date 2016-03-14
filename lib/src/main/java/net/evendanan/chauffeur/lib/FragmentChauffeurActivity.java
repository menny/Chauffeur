/*
 * Copyright (c) 2013 Menny Even-Danan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evendanan.chauffeur.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public abstract class FragmentChauffeurActivity extends AppCompatActivity implements FragmentChauffeur {

    private static final String INTENT_FRAGMENT_ACTION = "FragmentChauffeurActivity_INTENT_FRAGMENT_ACTION";
    private static final String KEY_FRAGMENT_CLASS_TO_ADD = "FragmentChauffeurActivity_KEY_FRAGMENT_CLASS_TO_ADD";
    private static final String KEY_FRAGMENT_ARGS_TO_ADD = "FragmentChauffeurActivity_KEY_FRAGMENT_ARGS_TO_ADD";
    private static final String KEY_FRAGMENT_ANIMATION = "FragmentChauffeurActivity_KEY_FRAGMENT_ANIMATION";

    @NonNull
    public static Intent createStartActivityIntentForAddingFragmentToUi(@NonNull Context context, @NonNull Class<? extends FragmentChauffeurActivity> mainActivityClass, @NonNull Fragment fragment, @NonNull TransitionExperience transitionExperience) {
        Intent intent = new Intent(context, mainActivityClass);
        intent.setAction(INTENT_FRAGMENT_ACTION);
        intent.putExtra(KEY_FRAGMENT_CLASS_TO_ADD, fragment.getClass());
        Bundle fragmentArgs = fragment.getArguments();
        if (fragmentArgs != null) {
            intent.putExtra(KEY_FRAGMENT_ARGS_TO_ADD, fragmentArgs);
        }
        intent.putExtra(KEY_FRAGMENT_ANIMATION, transitionExperience);

        return intent;
    }

    private boolean mIsActivityShown = false;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mIsActivityShown = true;
        if (savedInstanceState == null) {
            Bundle activityArgs = getIntent().getExtras();
            if (activityArgs == null || (!INTENT_FRAGMENT_ACTION.equals(getIntent().getAction()))) {
                //setting up the root of the UI.
                getSupportFragmentManager().popBackStack(ROOT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(getFragmentRootUiElementId(), createRootFragmentInstance());
                //bookmarking, so I can return easily.
                transaction.addToBackStack(ROOT_FRAGMENT_TAG);
                transaction.commit();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleFragmentIntentValues();
    }

    private void handleFragmentIntentValues() {
        Bundle activityArgs = getIntent().getExtras();
        if (activityArgs != null && (INTENT_FRAGMENT_ACTION.equals(getIntent().getAction()))) {
            Object fragmentClassKeyValue = activityArgs.get(KEY_FRAGMENT_CLASS_TO_ADD);
            if (fragmentClassKeyValue instanceof Class<?>) {
                Class<? extends Fragment> fragmentClass = (Class<? extends Fragment>) fragmentClassKeyValue;
                //not sure that this is a best-practice, but I still need to remove this from the activity's args
                //so this process will not repeat itself
                activityArgs.remove(KEY_FRAGMENT_CLASS_TO_ADD);
                try {
                    Fragment fragment = fragmentClass.newInstance();
                    if (activityArgs.containsKey(KEY_FRAGMENT_ARGS_TO_ADD)) {
                        fragment.setArguments(activityArgs.getBundle(KEY_FRAGMENT_ARGS_TO_ADD));
                        activityArgs.remove(KEY_FRAGMENT_ARGS_TO_ADD);
                    }
                    TransitionExperience experience = activityArgs.getParcelable(KEY_FRAGMENT_ANIMATION);
                    if (experience != null) {
                        addFragmentToUi(fragment, experience);
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @IdRes
    protected abstract int getFragmentRootUiElementId();

    @NonNull
    protected abstract Fragment createRootFragmentInstance();

    @Override
    public void returnToRootFragment(boolean immediately) {
        if (!mIsActivityShown) return;

        if (immediately)
            getSupportFragmentManager().popBackStackImmediate(ROOT_FRAGMENT_TAG, 0 /*don't pop the root*/);
        else
            getSupportFragmentManager().popBackStack(ROOT_FRAGMENT_TAG, 0 /*don't pop the root*/);
    }

    /**
     * Adds the given fragment into the UI using the specified UI-context animation.
     *
     * @param fragment   any generic Fragment. For the ExpandedItem animation it is best to use a PassengerFragment
     * @param experience the animation UI experience to use for this transition.
     */
    @Override
    public void addFragmentToUi(@NonNull Fragment fragment, @NonNull TransitionExperience experience) {
        if (!mIsActivityShown) return;

        experience.onPreTransaction(this, fragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        experience.onTransactionReady(this, fragment, transaction);
        experience.onAddFragmentToContainer(this, fragment, transaction, getFragmentRootUiElementId());

        transaction.commit();

        experience.onPostTransactionCommit(this, fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //the UI is empty. I can safely finish the activity
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsActivityShown = true;
        //now, checking if there is a request to add a fragment on-top of this one.
        handleFragmentIntentValues();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsActivityShown = false;
    }

    @Override
    public final boolean isChaufferActivityVisible() {
        return mIsActivityShown;
    }
}
