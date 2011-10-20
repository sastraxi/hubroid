/**
 * 
 */
package net.idlesoft.android.apps.github;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.idlesoft.libraries.ghapi.GitHubAPI;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public interface ThingsCommonToAllMankind {
    /**
     * Create a GitHubClient (egit-github) instance and authenticate if possible
     *
     * @return GitHubClient instance
     */
    public GitHubClient getGitHubClient();

    /**
     * Create a GitHubAPI (ghapi) instance and authenticate if possible
     *
     * @return GitHubAPI instance
     */
    public GitHubAPI getGApi();

    /**
     * Get a SharedPreferences instance
     *
     * @return SharedPreferences instance
     */
    public SharedPreferences getPrefs();

    /**
     * Get an Editor instance
     *
     * @return Editor instance
     */
    public Editor getPrefsEditor();
}
