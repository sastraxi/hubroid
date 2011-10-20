package net.idlesoft.android.apps.github.fragments;

import net.idlesoft.android.apps.github.R;
import net.idlesoft.android.apps.github.activities.Dashboard;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.UserService;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class LoginFragment extends BaseFragment implements LoaderCallbacks<Integer> {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    private class ProgressDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final ProgressDialog dialog = new ProgressDialog(theActivity());
            dialog.setMessage("Logging in");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            return dialog;
        }
    };

    private void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Remove old dialog if it exists
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        // Create and show a new dialog
        ProgressDialogFragment newDialog = new ProgressDialogFragment();
        newDialog.show(ft, "dialog");
    }

    private void hideDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Remove dialog fragment if it exists
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev).commitAllowingStateLoss();
        }
    }

    protected void startLoading(final Bundle args) {
        // Show a progress dialog
        showDialog();

        // Instantiate and start a new loader
        getSupportActivity().getSupportLoaderManager().initLoader(0, args, this);
    }

    protected void restartLoading(final Bundle args) {
        // Show a progress dialog
        showDialog();

        // Restart a loader that is already in progress
        getSupportActivity().getSupportLoaderManager().restartLoader(0, args, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LoaderManager lm = theActivity().getSupportLoaderManager();

        final Button loginBtn = (Button) theActivity().findViewById(R.id.btn_login_login);
        loginBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user = ((EditText) theActivity().findViewById(R.id.et_login_username))
                        .getText().toString();
                final String pass = ((EditText) theActivity().findViewById(R.id.et_login_password))
                        .getText().toString();
                if (user == null || pass == null || user.equals("") || pass.equals("")) {
                    Toast.makeText(theActivity(), "Login fields cannot be blank",
                            Toast.LENGTH_SHORT).show();
                } else {
                    final Bundle args = new Bundle();
                    args.putString("username", user);
                    args.putString("password", pass);
                    if (lm.getLoader(0) == null) {
                        startLoading(args);
                    } else {
                        restartLoading(args);
                    }
                }
            }
        });

        if (lm.getLoader(0) != null) {
            lm.initLoader(0, null, this);
        }
    }

    @Override
    public Loader<Integer> onCreateLoader(final int id, final Bundle args) {
        final AsyncTaskLoader<Integer> loader = new AsyncTaskLoader<Integer>(theActivity()) {
            @Override
            public Integer loadInBackground() {
                final UserService us = new UserService(getGitHubClient().setCredentials(
                        args.getString("username"), args.getString("password")));
                try {
                    User u = us.getUser();

                    if (u != null) {
                        getPrefsEditor().putString("username", u.getLogin());
                        getPrefsEditor().putString("password", args.getString("password"));
                        getPrefsEditor().commit();
                        return 200;
                    } else {
                        return -1;
                    }
                } catch (IOException e) {
                    if (e instanceof RequestException) {
                        return ((RequestException) e).getStatus();
                    } else {
                        e.printStackTrace();
                        return -2;
                    }
                }
            }
        };
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(final Loader<Integer> loader, final Integer result) {
        hideDialog();
        if (result == 401) {
            Toast.makeText(theActivity(), "Login details incorrect, try again", Toast.LENGTH_SHORT)
                    .show();
        } else if (result == 100) {
            Toast.makeText(theActivity(), "Login details cannot be empty", Toast.LENGTH_SHORT)
                    .show();
        } else if (result == 200) {
            Toast.makeText(theActivity(), "Login successful", Toast.LENGTH_SHORT).show();
            theActivity().startActivity(new Intent(theActivity(), Dashboard.class));
            theActivity().finish();
        } else {
            Toast.makeText(theActivity(), "Unknown login error: " + result, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onLoaderReset(final Loader<Integer> loader) {
    }
}
