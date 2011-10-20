
package net.idlesoft.android.apps.github.activities;

import net.idlesoft.android.apps.github.HubroidApplication;
import net.idlesoft.android.apps.github.R;
import net.idlesoft.android.apps.github.ThingsCommonToAllMankind;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.idlesoft.libraries.ghapi.GitHubAPI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.KeyEvent;
import android.webkit.WebView;

public class BaseActivity extends FragmentActivity implements ThingsCommonToAllMankind {
    protected static final int NO_LAYOUT = -1;

    protected SharedPreferences mPrefs;

    protected SharedPreferences.Editor mPrefsEditor;

    protected String mUsername;

    protected String mPassword;

    protected GitHubAPI mGApi;

    /**
     * onCreate method for BaseActivity
     *
     * Handles setting up initial layout, preferences, and GitHub API libraries
     *
     * @param icicle
     * @param layout - Pass NO_LAYOUT (-1) if you don't want BaseActivity to set a layout
     */
    protected void onCreate(final Bundle icicle, final int layout) {
        super.onCreate(icicle);
        if (layout != NO_LAYOUT) {
            setContentView(layout);
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefsEditor = mPrefs.edit();

        mUsername = mPrefs.getString("username", "");
        mPassword = mPrefs.getString("password", "");

        mGApi = HubroidApplication.getGApiInstance();
        mGApi.authenticate(mUsername, mPassword);
    }

    /**
     * Default onCreate
     *
     * Calls onCreate with NO_LAYOUT set, for activities that want to setContentView themselves.
     */
    protected void onCreate(final Bundle icicle) {
        onCreate(icicle, NO_LAYOUT);
    }

    /**
     * Get a GitHubClient that has credentials set if they exist
     *
     * @return GitHubClient
     */
    public GitHubClient getGitHubClient() {
        return HubroidApplication.getGitHubClientInstance().setCredentials(mUsername, mPassword);
    }

    public void setupActionBar(final String pTitle, final boolean pShowSearch,
            final boolean pShowCreate) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            int displayFlags = ActionBar.DISPLAY_SHOW_TITLE;
            if (!(this instanceof Dashboard)) {
                displayFlags |= ActionBar.DISPLAY_SHOW_HOME;
            }
            actionBar.setDisplayOptions(displayFlags);

            actionBar.setDisplayShowHomeEnabled(true);
            //getMenuInflater().inflate(R.menu.actionbar, actionBar.asMenu());

            if (pTitle == null) {
                actionBar.setTitle(R.string.app_name);
            } else {
                actionBar.setTitle(pTitle);
            }

            //final Action createAction = (Action) actionBar.findAction(R.id.actionbar_item_create);
            //final Action searchAction = (Action) actionBar.findAction(R.id.actionbar_item_search);

            if (pShowCreate) {
              //  createAction.setEnabled(true);
                //createAction.setVisible(true);
            }

            if (pShowSearch) {
                //searchAction.setEnabled(true);
                //searchAction.setVisible(true);
            }
        }
    }

    public void setupActionBar() {
        setupActionBar("Hubroid", true, false);
    }

    public void setupActionBar(final String pTitle) {
        setupActionBar(pTitle, true, false);
    }

    public void setupActionBar(final boolean pShowSearch) {
        setupActionBar("Hubroid", pShowSearch, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create preferences action
        menu.add(0, 0, 0, "Options")
            .setIcon(android.R.drawable.ic_menu_preferences)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        menu.add(0, 1, 0, "Logout")
            .setIcon(android.R.drawable.ic_lock_power_off)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case 1:
                mPrefsEditor.clear().commit();
                startActivity(new Intent(this, Hubroid.class));
                return true;
            case 0:
                //startActivity(new Intent(this, HubroidPreferences.class));
                return true;
        }
        return true;
    }

    public boolean volumeZoom(KeyEvent event, WebView view) {
        // Only enable volume zooming in files if it's set in preferences
        if (mPrefs.getBoolean(getString(R.string.preferences_key_files_volume_zoom), false)) {
            int action = event.getAction();
            int keyCode = event.getKeyCode();
            switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    view.zoomIn();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    view.zoomOut();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public GitHubAPI getGApi() {
        return mGApi;
    }

    @Override
    public SharedPreferences getPrefs() {
        return mPrefs;
    }

    @Override
    public Editor getPrefsEditor() {
        return mPrefsEditor;
    }
}
