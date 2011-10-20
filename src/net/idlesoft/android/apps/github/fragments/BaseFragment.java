package net.idlesoft.android.apps.github.fragments;

import net.idlesoft.android.apps.github.ProgrammerException;
import net.idlesoft.android.apps.github.ThingsCommonToAllMankind;
import net.idlesoft.android.apps.github.activities.BaseActivity;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.idlesoft.libraries.ghapi.GitHubAPI;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements ThingsCommonToAllMankind {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(getActivity() instanceof BaseActivity)) {
            throw new ProgrammerException("BaseFragments should only attach to derived BaseActivities!");
        }
    }

    public BaseActivity theActivity() {
        return (BaseActivity)getActivity();
    }

    public SharedPreferences getPrefs() {
        return theActivity().getPrefs();
    }

    @Override
    public GitHubClient getGitHubClient() {
        return theActivity().getGitHubClient();
    }

    @Override
    public GitHubAPI getGApi() {
        return theActivity().getGApi();
    }

    @Override
    public Editor getPrefsEditor() {
        return theActivity().getPrefsEditor();
    }
}
