/**
 * Hubroid - A GitHub app for Android
 *
 * Copyright (c) 2011 Eddie Ringle.
 *
 * Licensed under the New BSD License.
 */

package net.idlesoft.android.apps.github.activities;

import net.idlesoft.android.apps.github.fragments.LoginFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.Menu;

public class Login extends BaseActivity {

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);

        // Load LoginFragment and add it to the stack
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(android.R.id.content, new LoginFragment(), "login").commit();
        fm.executePendingTransactions();

        // Don't show the Action Bar on the login page
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Don't show the options menu in Login activity
        return false;
    }
}
