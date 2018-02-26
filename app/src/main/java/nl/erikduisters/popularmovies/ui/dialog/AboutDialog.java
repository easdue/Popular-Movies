package nl.erikduisters.popularmovies.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import nl.erikduisters.popularmovies.R;
import timber.log.Timber;

/**
 * Created by Erik Duisters on 24-02-2018.
 */

//TODO: Save and restore scrollview state (e.g. for restoring scroll position when the activity was killed)
public class AboutDialog extends DialogFragment {
    public interface DismissListener {
        void onDismiss();
    }

    private DismissListener listener;

    public static AboutDialog newInstance() {
        return new AboutDialog();
    }

    public void setListener(DismissListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //noinspection ConstantConditions
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.about)
                .setView(R.layout.dialog_about)
                .setPositiveButton(R.string.ok, null);

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Timber.e("onDismiss()");

        if (listener != null) {
            listener.onDismiss();
        }
    }
}
