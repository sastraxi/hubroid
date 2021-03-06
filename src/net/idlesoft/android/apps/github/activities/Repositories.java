/**
 * Hubroid - A GitHub app for Android
 *
 * Copyright (c) 2011 Eddie Ringle.
 *
 * Licensed under the New BSD License.
 */

package net.idlesoft.android.apps.github.activities;

import net.idlesoft.android.apps.github.R;
import net.idlesoft.android.apps.github.activities.tabs.MyRepos;
import net.idlesoft.android.apps.github.activities.tabs.PushableRepos;
import net.idlesoft.android.apps.github.activities.tabs.WatchedRepos;
import net.idlesoft.android.apps.github.utils.GravatarCache;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Repositories extends BaseTabActivity {
    private static final String TAG_MY_REPOS = "my_repos";

    private static final String TAG_WATCHED_REPOS = "watched_repos";

    private static final String TAG_PUSHABLE_REPOS = "pushable_repos";

    public static final int CONTEXT_MENU_DETAILS = 1;
    public static final int CONTEXT_MENU_BRANCHES = 2;
    public static final int CONTEXT_MENU_ISSUES = 3;
    public static final int CONTEXT_MENU_FORKS = 4;
    public static final int CONTEXT_MENU_OWNER = 5;

    private String mTarget;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle, R.layout.repositories);

        setupActionBar();

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTarget = extras.getString("target");
        }
        if ((mTarget == null) || mTarget.equals("")) {
            mTarget = mUsername;
        }

        final ImageView gravatar = (ImageView) findViewById(R.id.iv_repositories_gravatar);

        gravatar.setImageBitmap(GravatarCache.getDipGravatar(mTarget, 38.0f, getResources()
                .getDisplayMetrics().density));
        ((TextView) findViewById(R.id.tv_page_title)).setText(mTarget);

        gravatar.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                final Intent i = new Intent(Repositories.this, Profile.class);
                i.putExtra("username", mTarget);
                startActivity(i);
            }
        });

        Intent intent = new Intent(getApplicationContext(), MyRepos.class);
        intent.putExtra("target", mTarget);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_MY_REPOS)
                .setIndicator(buildIndicator(R.string.my_repos)).setContent(intent));

        intent = new Intent(getApplicationContext(), WatchedRepos.class);
        intent.putExtra("target", mTarget);
        mTabHost.addTab(mTabHost.newTabSpec(TAG_WATCHED_REPOS)
                .setIndicator(buildIndicator(R.string.watched_repos)).setContent(intent));

        /*
         * If we're viewing lists of logged in user's own repositories, show
         * those he/she has push access to
         */
        if (mTarget.equals(mUsername)) {
            intent = new Intent(getApplicationContext(), PushableRepos.class);
            mTabHost.addTab(mTabHost.newTabSpec(TAG_PUSHABLE_REPOS)
                    .setIndicator(buildIndicator(R.string.pushable_repos)).setContent(intent));
        }
    }
}
